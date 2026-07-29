package su35.setec.authservice.service;

import java.util.List;
import java.util.UUID;
import su35.setec.authservice.dto.request.UpdateUserRequest;
import su35.setec.authservice.dto.response.UserResponse;

public interface UserService {

    List<UserResponse> findAll();

    UserResponse findById(UUID id);

    UserResponse update(UUID id, UpdateUserRequest request);

    void delete(UUID id);
}
