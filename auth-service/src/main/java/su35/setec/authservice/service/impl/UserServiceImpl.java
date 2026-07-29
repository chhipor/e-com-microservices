package su35.setec.authservice.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su35.setec.authservice.dto.request.UpdateUserRequest;
import su35.setec.authservice.dto.response.UserResponse;
import su35.setec.authservice.entity.Role;
import su35.setec.authservice.entity.User;
import su35.setec.authservice.exception.BadRequestException;
import su35.setec.authservice.exception.NotFoundException;
import su35.setec.authservice.mapper.UserMapper;
import su35.setec.authservice.repository.RefreshTokenRepository;
import su35.setec.authservice.repository.RoleRepository;
import su35.setec.authservice.repository.UserRepository;
import su35.setec.authservice.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(UUID id) {
        return userMapper.toResponse(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id)));
    }

    @Override
    @Transactional
    public UserResponse update(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        if (!user.getEmail().equalsIgnoreCase(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setEnabled(request.isEnabled());
        user.setAccountNonLocked(request.isAccountNonLocked());
        user.setRoles(resolveRoles(request.getRoleNames()));

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found: " + id);
        }
        refreshTokenRepository.findAllByUserIdAndRevokedFalse(id).forEach(token -> token.setRevoked(true));
        refreshTokenRepository.flush();
        userRepository.deleteById(id);
    }

    private Set<Role> resolveRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            throw new BadRequestException("At least one role is required");
        }

        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new NotFoundException("Role not found: " + roleName));
            roles.add(role);
        }
        return roles;
    }
}
