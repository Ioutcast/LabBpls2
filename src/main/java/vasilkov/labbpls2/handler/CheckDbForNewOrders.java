package vasilkov.labbpls2.handler;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import vasilkov.labbpls2.entity.Order;
import vasilkov.labbpls2.repository.OrderRepository;

import java.util.List;

@Named("checkDbForNewOrders")
@RequiredArgsConstructor
@Slf4j
public class CheckDbForNewOrders implements JavaDelegate {
    private final OrderRepository orderRepository;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<Order> orders = orderRepository.findOrderByStatusIsNull();
        if (orders.isEmpty()){
            delegateExecution.setVariable("newOrders", false);
        } else {
            log.info("новый заказ!");
            delegateExecution.setVariable("newOrders", true);
        }
    }
}
