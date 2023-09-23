package vasilkov.labbpls2.handler;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import vasilkov.labbpls2.api.request.GrantRequest;
import vasilkov.labbpls2.service.OrderService;

@Named("changeStatus")
@RequiredArgsConstructor
@Slf4j
public class ChangeStatus implements JavaDelegate {
    private final OrderService orderService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long id = Long.valueOf(delegateExecution.getVariable("orderId").toString());
        boolean status = Boolean.parseBoolean(delegateExecution.getVariable("finalStatusId").toString());
        orderService.grantOrderWithEmail(new GrantRequest(id,status,""));
    }
}
