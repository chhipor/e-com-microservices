package su35.setec.authservice.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su35.setec.authservice.dto.request.LoginRequest;
import su35.setec.authservice.dto.request.RefreshTokenRequest;
import su35.setec.authservice.dto.request.RegisterRequest;
import su35.setec.authservice.dto.response.AuthResponse;
import su35.setec.authservice.dto.response.UserResponse;
import su35.setec.authservice.entity.Permission;
import su35.setec.authservice.entity.RefreshToken;
import su35.setec.authservice.entity.Role;
import su35.setec.authservice.entity.User;
import su35.setec.authservice.exception.BadRequestException;
import su35.setec.authservice.exception.NotFoundException;
import su35.setec.authservice.mapper.UserMapper;
import su35.setec.authservice.repository.PermissionRepository;
import su35.setec.authservice.repository.RefreshTokenRepository;
import su35.setec.authservice.repository.RoleRepository;
import su35.setec.authservice.repository.UserRepository;
import su35.setec.authservice.security.CustomUserDetails;
import su35.setec.authservice.security.JwtService;
import su35.setec.authservice.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name("USER")
                        .description("Standard user role")
                        .build()));

        // Keep the default registration flow simple and safe: new users get USER only.
        Permission readPermission = permissionRepository.findByName("USER_READ")
                .orElseThrow(() -> new NotFoundException("Required permission USER_READ is missing"));
        userRole.getPermissions().add(readPermission);
        roleRepository.save(userRole);

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .accountNonLocked(true)
                .roles(Set.of(userRole))
                .build();

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        User user = principal.getUser();
        invalidateExistingRefreshTokens(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshTokenValue = jwtService.generateRefreshToken(user);
        persistRefreshToken(user, refreshTokenValue, jwtService.getRefreshTokenExpirationSeconds());

        return AuthResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .accessTokenExpiresInSeconds(jwtService.getAccessTokenExpirationSeconds())
                .refreshTokenExpiresInSeconds(jwtService.getRefreshTokenExpirationSeconds())
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new BadRequestException("Refresh token is invalid"));

        if (refreshToken.isRevoked() || refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new BadRequestException("Refresh token is expired or revoked");
        }

        User user = refreshToken.getUser();
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        persistRefreshToken(user, newRefreshToken, jwtService.getRefreshTokenExpirationSeconds());

        return AuthResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .accessTokenExpiresInSeconds(jwtService.getAccessTokenExpirationSeconds())
                .refreshTokenExpiresInSeconds(jwtService.getRefreshTokenExpirationSeconds())
                .user(userMapper.toResponse(user))
                .build();
    }

    private void invalidateExistingRefreshTokens(User user) {
        List<RefreshToken> activeTokens = refreshTokenRepository.findAllByUserIdAndRevokedFalse(user.getId());
        if (!activeTokens.isEmpty()) {
            activeTokens.forEach(token -> token.setRevoked(true));
            refreshTokenRepository.saveAll(activeTokens);
        }
    }

    private void persistRefreshToken(User user, String tokenValue, long expiresInSeconds) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(tokenValue)
                .expiryDate(Instant.now().plusSeconds(expiresInSeconds))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);
    }
}
