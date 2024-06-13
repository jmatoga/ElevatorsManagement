package elevatorsManagement.mapper;

import elevatorsManagement.dto.UserDTO;
import elevatorsManagement.model.User;
import elevatorsManagement.security.payload.request.SignUpRequest;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import elevatorsManagement.service.UserService;

@Mapper(imports = String.class, componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected UserService userService;

    @Mapping(target="email", source = "email")
    @Mapping(target="password", source = "password")
    public abstract User mapToUser(SignUpRequest signupRequest);

    @InheritInverseConfiguration(name = "mapToUser")
    @Named("mapToUserDTO")
    public abstract UserDTO mapToUserDTO(User user);

}
