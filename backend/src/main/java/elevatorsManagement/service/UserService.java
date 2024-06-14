package elevatorsManagement.service;


import elevatorsManagement.dto.UserDetailsDTO;
import elevatorsManagement.exception.CurrentUserNotAuthenticatedException;
import elevatorsManagement.model.User;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User getCurrentUser() throws CurrentUserNotAuthenticatedException;

    List<User> getAllUsers();

    void registerUser(User user);

    User getUserById(UUID id);
}
