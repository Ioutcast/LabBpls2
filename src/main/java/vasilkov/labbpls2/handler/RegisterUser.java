package vasilkov.labbpls2.handler;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import vasilkov.labbpls2.api.request.RegisterRequest;
import vasilkov.labbpls2.service.AuthService;

@Named("registerUser")
@RequiredArgsConstructor @Slf4j
public class RegisterUser implements JavaDelegate {
    private final AuthService authService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("registerUser");
        delegateExecution.removeVariable("isUser");
        authService.register(
                new RegisterRequest(
                        delegateExecution.getVariable("email2").toString(),
                        delegateExecution.getVariable("passwordId2").toString(),
                        delegateExecution.getVariable("firstnameId2").toString(),
                        delegateExecution.getVariable("lastnameId2").toString()
                )
        );
    }
}
