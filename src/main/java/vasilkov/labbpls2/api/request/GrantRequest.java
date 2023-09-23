package vasilkov.labbpls2.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data @RequiredArgsConstructor @AllArgsConstructor
public class GrantRequest {

    @NotNull
    Long id;

    @NotNull
    Boolean finalStatus;

    String message;

}