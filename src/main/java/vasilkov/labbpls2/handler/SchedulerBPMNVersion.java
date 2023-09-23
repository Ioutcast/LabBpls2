package vasilkov.labbpls2.handler;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nu.xom.ParsingException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.transaction.annotation.Transactional;
import vasilkov.labbpls2.entity.Order;
import vasilkov.labbpls2.repository.OrderRepository;
import vasilkov.labbpls2.service.RabbitMQProducerService;

import java.io.IOException;
import java.util.List;
@Named("schedulerBPMNVersion")
@RequiredArgsConstructor
@Slf4j
public class SchedulerBPMNVersion implements JavaDelegate {
    private final OrderRepository orderRepository;
    private final RabbitMQProducerService rabbitMQProducerService;
    @Override
    @Transactional
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<Order> orders = orderRepository.findOrderByStatusIsNull();
        if (!orders.isEmpty()) {
            orders.forEach(
                    it -> {
                        try {
                            it.setStatus(false);
                            rabbitMQProducerService.sendMessage("NEWORDER", it);
                            orderRepository.save(it);
                        } catch (ParsingException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        } else
            System.out.println("Scheduled works, but there is no new orders");
    }
}
