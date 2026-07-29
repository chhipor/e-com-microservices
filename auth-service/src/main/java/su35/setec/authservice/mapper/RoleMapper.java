package su35.setec.authservice.mapper;

import org.springframework.stereotype.Component;
import su35.setec.authservice.entity.Role;

@Component
public class RoleMapper {

    public String toName(Role role) {
        return role.getName();
    }
}
