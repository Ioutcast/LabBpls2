package vasilkov.labbpls2.service;

import lombok.NonNull;
import nu.xom.ParsingException;
import vasilkov.labbpls2.api.request.JwtRequest;
import vasilkov.labbpls2.api.request.RegisterRequest;
import vasilkov.labbpls2.api.response.JwtResponse;
import vasilkov.labbpls2.entity.User;
import vasilkov.labbpls2.exception.AuthException;
import vasilkov.labbpls2.security.JwtAuthentication;

import java.io.IOException;

public interface AuthService {
    JwtResponse login(@NonNull JwtRequest authRequest) throws ParsingException, IOException;

    JwtResponse getAccessToken(@NonNull String refreshToken) throws ParsingException, IOException;

    JwtResponse refresh(@NonNull String refreshToken) throws ParsingException,IOException;

    JwtAuthentication getAuthInfo();

    void register(RegisterRequest request);

    void logout(@NonNull String refreshToken) throws AuthException;
}
