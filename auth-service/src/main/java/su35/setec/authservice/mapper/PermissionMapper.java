package su35.setec.authservice.mapper;

import org.springframework.stereotype.Component;
import su35.setec.authservice.entity.Permission;

@Component
public class PermissionMapper {

    public String toName(Permission permission) {
        return permission.getName();
    }
}
