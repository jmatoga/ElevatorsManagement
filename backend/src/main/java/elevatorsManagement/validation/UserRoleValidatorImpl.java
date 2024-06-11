package elevatorsManagement.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import elevatorsManagement.model.ERole;
import elevatorsManagement.model.User;


@Component
@RequiredArgsConstructor
public class UserRoleValidatorImpl implements UserRoleValidator{

    @Override
    public boolean userHasRole(User user, ERole role) {
        return user.getRoles().contains(role);
    }

    @Override
    public boolean checkUserRole(ERole role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                       .anyMatch(r -> r.getAuthority().equals(role.name()));
    }

    @Override
    public boolean isAdmin(User user) {
        return userHasRole(user,ERole.ROLE_ADMIN);
    }
}
