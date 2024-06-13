package elevatorsManagement.controller;

import elevatorsManagement.configuration.PropertiesConfig;
import elevatorsManagement.exception.CurrentUserNotAuthenticatedException;
//import elevatorsManagement.mapper.UserMapper;
import elevatorsManagement.model.User;
import elevatorsManagement.security.payload.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import elevatorsManagement.security.payload.request.*;
import elevatorsManagement.security.payload.response.JwtResponse;
import elevatorsManagement.service.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Log4j2
public class AuthController {

    private final LoginService loginService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final PropertiesConfig propertiesConfig;
    //private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        return loginService.getJwtResponseResponseEntity(loginRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logoutUser(@CookieValue(name = "refreshToken",required = false) String refreshToken) {
        if(refreshToken!=null)
            refreshTokenService.deleteByToken(refreshToken);
        else {
            try {
                refreshTokenService.deleteByUserId(userService.getCurrentUser().getId());
            } catch (CurrentUserNotAuthenticatedException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, String.valueOf(createAccessTokenCookie("", 0L)));
        responseHeaders.add(HttpHeaders.SET_COOKIE, String.valueOf(createRefreshTokenCookie("", 0L)));

        return ResponseEntity.ok().headers(responseHeaders).body(new MessageResponse("Log out successful!"));
    }

    public HttpCookie createAccessTokenCookie(String token, Long duration) {
        return ResponseCookie.from(propertiesConfig.getAccessTokenCookieName(), token)
                       .maxAge(duration)
                       .httpOnly(true)
                       .sameSite("Strict")
                       .path("/")
                       .build();
    }

    public HttpCookie createRefreshTokenCookie(String token, Long duration) {
        return ResponseCookie.from(propertiesConfig.getRefreshTokenCookieName(), token)
                       .maxAge(duration)
                       .httpOnly(true)
                       .sameSite("Strict")
                       .path("/")
                       .build();
    }

//    @PostMapping("/register")
//    ResponseEntity<MessageResponse> registerUser(@RequestBody SignUpRequest signUpRequest) {
//        User user = userMapper.mapToUser(signUpRequest);
//        userService.registerUser(user);
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//    }
}
