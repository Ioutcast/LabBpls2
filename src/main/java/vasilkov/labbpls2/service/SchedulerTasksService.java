package vasilkov.labbpls2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nu.xom.ParsingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vasilkov.labbpls2.entity.Order;
import vasilkov.labbpls2.entity.User;
import vasilkov.labbpls2.exception.ResourceNotFoundException;
import vasilkov.labbpls2.repository.OrderRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor @Slf4j
public class SchedulerTasksService {
    private final OrderRepository orderRepository;
    private final RabbitMQProducerService rabbitMQProducerService;

    @Scheduled(fixedDelay = 1,timeUnit = TimeUnit.MINUTES)
    public void checkForNewOrdersWithOutStatus() {
        List<Order> orders = orderRepository.findOrderByStatusIsNull();
        if (!orders.isEmpty()) {
            orders.forEach(
                    it -> {
                        try {
                            rabbitMQProducerService.sendMessage("NEWORDER", it);
                        } catch (ParsingException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        } else
            System.out.println("Scheduled works, but there is no new orders");
    }

}
