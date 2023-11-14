package vasilkov.labbpls2.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nu.xom.ParsingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vasilkov.labbpls2.entity.Order;
import vasilkov.labbpls2.repository.OrderRepository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerTasksServiceImpl {
    private final OrderRepository orderRepository;
    private final RabbitMQProducerServiceImpl rabbitMQProducerServiceImpl;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void checkForNewOrdersWithOutStatus() {
        List<Order> orders = orderRepository.findOrderByStatusIsNull();
        if (!orders.isEmpty()) {
            orders.forEach(it -> {
                try {
                    rabbitMQProducerServiceImpl.sendMessage("NEWORDER", it);
                } catch (ParsingException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}