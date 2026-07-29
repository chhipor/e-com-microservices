package su35.setec.authservice.service;

import su35.setec.authservice.dto.request.LoginRequest;
import su35.setec.authservice.dto.request.RefreshTokenRequest;
import su35.setec.authservice.dto.request.RegisterRequest;
import su35.setec.authservice.dto.response.AuthResponse;
import su35.setec.authservice.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);
}
