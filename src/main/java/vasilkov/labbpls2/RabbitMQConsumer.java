package vasilkov.labbpls2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import vasilkov.labbpls2.service.mailUtils.MailModel;

@Component
@EnableRabbit
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumer {

    @RabbitListener(queues = "${rabbit-mq.queue}")
    public void processMyQueue(MailModel message) {
        log.info("Received from myQueue : {} ", message);
    }
}