package elevatorsManagement.service;

import elevatorsManagement.model.User;
import elevatorsManagement.security.payload.request.LoginRequest;
import elevatorsManagement.security.payload.response.JwtResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    JwtResponse authenticateUser(LoginRequest loginRequest);

    HttpHeaders createHeaders(JwtResponse jwtResponse);

    ResponseEntity<JwtResponse> getJwtResponseResponseEntity(LoginRequest loginRequest);

    ResponseEntity<JwtResponse> getJwtResponseFromUser(User user);

}
