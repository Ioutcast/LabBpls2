package vasilkov.labbpls2.handler;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import vasilkov.labbpls2.entity.Order;
import vasilkov.labbpls2.repository.OrderRepository;
import vasilkov.labbpls2.service.RabbitMQProducerService;

@Named("userInfoForStatusChanged")
@RequiredArgsConstructor
@Slf4j
public class UserInfoForStatusChanged implements JavaDelegate {
    private final RabbitMQProducerService rabbitMQProducerService;
    private final OrderRepository orderRepository;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        long id = Long.parseLong(delegateExecution.getVariable("orderId").toString());
        Order order =  orderRepository.findById(Math.toIntExact(id)).orElseThrow((()->new IllegalArgumentException("")));
        rabbitMQProducerService.sendMessage("STATUS",order);
    }
}
