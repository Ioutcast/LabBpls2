package vasilkov.labbpls2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vasilkov.labbpls2.api.request.JwtRequest;
import vasilkov.labbpls2.api.request.RefreshJwtRequest;
import vasilkov.labbpls2.api.request.RegisterRequest;
import vasilkov.labbpls2.api.response.JwtResponse;
import vasilkov.labbpls2.api.response.MessageResponse;
import vasilkov.labbpls2.exception.AuthException;
import vasilkov.labbpls2.service.AuthService;
import vasilkov.labbpls2.service.EmailService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final EmailService emailService;

    @PostMapping("/singin")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) throws Exception {
        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws Exception {
        try {
            final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
            return ResponseEntity.ok(token);
        } catch (NullPointerException ex) {
            throw new AuthException(ex.getMessage());
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws Exception {
        try {
            final JwtResponse token = authService.refresh(request.getRefreshToken());
            return ResponseEntity.ok(token);
        } catch (NullPointerException ex) {
            throw new AuthException(ex.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody RefreshJwtRequest request) throws Exception {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }

}