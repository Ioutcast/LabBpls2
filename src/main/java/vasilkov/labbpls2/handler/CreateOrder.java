package vasilkov.labbpls2.handler;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import vasilkov.labbpls2.api.request.OrderRequest;
import vasilkov.labbpls2.service.OrderService;


@Named("createOrder")
@RequiredArgsConstructor
@Slf4j
public class CreateOrder implements JavaDelegate {
    private final OrderService orderService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        orderService.save(new OrderRequest(
                delegateExecution.getVariable("descriptionId").toString(),
                delegateExecution.getVariable("colorId").toString(),
                delegateExecution.getVariable("materialId").toString(),
                Integer.valueOf(delegateExecution.getVariable("number_of_pieces_in_a_packageId").toString()),
                delegateExecution.getVariable("country_of_originId").toString(),
                delegateExecution.getVariable("brandNameId").toString(),
                delegateExecution.getVariable("modelNameId").toString(),
                Integer.valueOf(delegateExecution.getVariable("guarantee_periodId").toString())
        ));
    }

}