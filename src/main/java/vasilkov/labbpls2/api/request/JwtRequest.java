package vasilkov.labbpls2.api.request;


import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor @AllArgsConstructor
public class JwtRequest {

    private String email;
    private String password;

}