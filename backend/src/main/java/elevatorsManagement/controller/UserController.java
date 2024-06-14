package elevatorsManagement.controller;

import elevatorsManagement.dto.UserDTO;
import elevatorsManagement.exception.CurrentUserNotAuthenticatedException;
import elevatorsManagement.mapper.UserMapper;
import elevatorsManagement.model.User;
import elevatorsManagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/users")
    ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/current-user")
    @PreAuthorize("hasRole('ROLE_USER')" + "or hasRole('ROLE_MODERATOR')" + "or hasRole('ROLE_ADMIN')" )
    ResponseEntity<UserDTO> getCurrentUser() throws CurrentUserNotAuthenticatedException {
        User currentUser = userService.getCurrentUser();
        UserDTO dto = userMapper.mapToUserDTO(userService.getUserById(currentUser.getId()));
        return ResponseEntity.ok(dto);
    }

}