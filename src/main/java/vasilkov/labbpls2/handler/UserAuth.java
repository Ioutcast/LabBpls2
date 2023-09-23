package vasilkov.labbpls2.handler;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import vasilkov.labbpls2.api.request.JwtRequest;
import vasilkov.labbpls2.api.response.JwtResponse;
import vasilkov.labbpls2.service.AuthService;

@Named("userAuth")
@RequiredArgsConstructor @Slf4j
public class UserAuth implements JavaDelegate {
    private final AuthService authService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("registerUser");
        delegateExecution.removeVariable("isUser");
        JwtResponse response = authService.login(new JwtRequest(delegateExecution.getVariable("email2").toString(),
                delegateExecution.getVariable("passwordId2").toString())
        );
        delegateExecution.setVariable("isBucket","no");
        delegateExecution.setVariable("accessToken",response.getAccessToken());
        delegateExecution.setVariable("refreshToken",response.getRefreshToken());
    }
}
