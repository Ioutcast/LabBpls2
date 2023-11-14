package vasilkov.labbpls2.service.impl;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nu.xom.ParsingException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vasilkov.labbpls2.api.request.JwtRequest;
import vasilkov.labbpls2.api.response.JwtResponse;
import vasilkov.labbpls2.api.request.RegisterRequest;
import vasilkov.labbpls2.entity.Role;
import vasilkov.labbpls2.entity.User;
import vasilkov.labbpls2.exception.AuthException;
import vasilkov.labbpls2.security.JwtAuthentication;
import vasilkov.labbpls2.security.JwtProvider;
import vasilkov.labbpls2.service.AuthService;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final Map<String, String> refreshStorage = new ConcurrentHashMap<>();

    private final JwtProvider jwtProvider;

    private static final String USER_NOT_FOUND_MESSAGE = "Пользователь не найден";
    private static final String INVALID_PASSWORD_MESSAGE = "Неправильный пароль";
    private static final String INVALID_JWT_TOKEN_MESSAGE = "Невалидный JWT токен";
    private static final String REGISTRATION_ERROR_MESSAGE = "Зарегистрироваться не получилось!";

    @Override
    public JwtResponse login(@NonNull JwtRequest authRequest) throws ParsingException, IOException {
        User user = getUserByEmail(authRequest.getEmail());
        validateCredentials(user, authRequest.getPassword());

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        storeRefreshToken(user.getEmail(), refreshToken);

        return new JwtResponse(accessToken, refreshToken);
    }
    private User getUserByEmail(String email) throws ParsingException, IOException {
        return userService.getByEmail(email).orElseThrow(() -> new AuthException(USER_NOT_FOUND_MESSAGE));
    }

    private void validateCredentials(User user, String password) {
        if (!user.getPassword().equals(password)) {
            throw new AuthException(INVALID_PASSWORD_MESSAGE);
        }
    }

    private void storeRefreshToken(String email, String refreshToken) {
        refreshStorage.put(email, refreshToken);
    }
    @Override
    public JwtResponse getAccessToken(@NonNull String refreshToken) throws ParsingException, IOException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByEmail(email)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }
    @Override
    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException, IOException, ParsingException {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new AuthException(INVALID_JWT_TOKEN_MESSAGE);
        }

        Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        String email = claims.getSubject();
        String savedRefreshToken = refreshStorage.get(email);

        if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
            throw new AuthException(INVALID_JWT_TOKEN_MESSAGE);
        }

        User user = getUserByEmail(email);
        String accessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);

        refreshStorage.put(user.getEmail(), newRefreshToken);

        return new JwtResponse(accessToken, newRefreshToken);
    }

    @Override
    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
    @Override
    public void register(RegisterRequest request) {
        try {
            User user = createUserFromRequest(request);
            XMLServiceImpl.addToXml(user);
        } catch (Exception ex) {
            throw new AuthException(REGISTRATION_ERROR_MESSAGE);
        }
    }
    private User createUserFromRequest(RegisterRequest request) {
        return User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(Collections.singleton(Role.USER))
                .build();
    }
    @Override
    public void logout(@NonNull String refreshToken) throws AuthException {

        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new AuthException(INVALID_JWT_TOKEN_MESSAGE);
        }

        Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        String email = claims.getSubject();

        if (!refreshStorage.containsKey(email)) {
            throw new AuthException(INVALID_JWT_TOKEN_MESSAGE);
        }

        refreshStorage.remove(email);
        SecurityContextHolder.getContext().setAuthentication(null);

    }
}