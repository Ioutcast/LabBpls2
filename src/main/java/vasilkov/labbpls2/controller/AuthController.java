package vasilkov.labbpls2.controller;

import lombok.RequiredArgsConstructor;
import nu.xom.ParsingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vasilkov.labbpls2.api.request.JwtRequest;
import vasilkov.labbpls2.api.request.RefreshJwtRequest;
import vasilkov.labbpls2.api.request.RegisterRequest;
import vasilkov.labbpls2.api.response.JwtResponse;
import vasilkov.labbpls2.api.response.MessageResponse;
import vasilkov.labbpls2.exception.AuthException;
import vasilkov.labbpls2.service.AuthService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Обрабатывает вход пользователя в систему.
     *
     * @param authRequest Запрос аутентификации, содержащий имя пользователя и пароль.
     * @return ResponseEntity с JwtResponse в случае успешного входа.
     * @throws Exception В случае проблем с процессом аутентификации.
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) throws Exception {
        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    /**
     * Обрабатывает регистрацию пользователя.
     *
     * @param request Запрос на регистрацию, содержащий данные пользователя.
     * @return ResponseEntity с MessageResponse, указывающим на успешную регистрацию.
     */
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(new MessageResponse("Пользователь успешно зарегистрирован!"));
    }

    /**
     * Генерирует новый токен доступа с использованием предоставленного токена обновления.
     *
     * @param request Запрос, содержащий токен обновления.
     * @return ResponseEntity с JwtResponse в случае успешного обновления токена.
     * @throws AuthException В случае проблем с процессом обновления токена.
     */
    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        try {
            final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
            return ResponseEntity.ok(token);
        } catch (NullPointerException | ParsingException | IOException ex) {
            throw new AuthException(ex.getMessage());
        }
    }

    /**
     * Генерирует новый токен обновления с использованием предоставленного токена обновления.
     *
     * @param request Запрос, содержащий токен обновления.
     * @return ResponseEntity с JwtResponse в случае успешного обновления токена.
     * @throws AuthException В случае проблем с процессом обновления токена.
     * @throws ParsingException В случае проблем с процессом обновления токена.
     * @throws IOException В случае проблем с процессом обновления токена.
     */
    @PostMapping("/refreshtoken")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException, ParsingException, IOException {
        try {
            final JwtResponse token = authService.refresh(request.getRefreshToken());
            return ResponseEntity.ok(token);
        } catch (NullPointerException ex) {
            throw new AuthException(ex.getMessage());
        }
    }

    /**
     * Выход пользователя из системы путем аннулирования токена обновления.
     *
     * @param request Запрос, содержащий токен обновления.
     * @return ResponseEntity с MessageResponse, указывающим на успешный выход.
     * @throws AuthException В случае проблем с процессом выхода из системы.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody RefreshJwtRequest request) throws AuthException {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(new MessageResponse("Выход из системы успешен!"));
    }
}
