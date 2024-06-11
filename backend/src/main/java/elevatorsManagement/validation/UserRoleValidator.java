package elevatorsManagement.validation;

import elevatorsManagement.model.User;
import elevatorsManagement.model.ERole;

public interface UserRoleValidator {

    boolean userHasRole(User user, ERole role);

    boolean checkUserRole(ERole role);

    boolean isAdmin(User user);
}
