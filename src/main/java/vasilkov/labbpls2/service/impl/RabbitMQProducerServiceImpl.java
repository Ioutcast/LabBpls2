package vasilkov.labbpls2.service.impl;

import lombok.extern.slf4j.Slf4j;
import nu.xom.ParsingException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vasilkov.labbpls2.config.RabbitMqProperties;
import vasilkov.labbpls2.entity.Order;
import vasilkov.labbpls2.service.RabbitMQProducerService;
import vasilkov.labbpls2.service.mailUtils.mailTypes.MailGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;


@Service @Slf4j
public class RabbitMQProducerServiceImpl implements RabbitMQProducerService {
    private Map<String, MailGenerator> map;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqProperties rabbitMqProperties;
    @Autowired
    public RabbitMQProducerServiceImpl(
            RabbitTemplate rabbitTemplate,
            List<MailGenerator> mailGenerator,
            RabbitMqProperties rabbitMqProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.map = mailGenerator
                .stream()
                .collect(toMap(MailGenerator::getType, Function.identity()));
        this.rabbitMqProperties = rabbitMqProperties;
    }
    @Override
    public void sendMessage(String typeOfMail, Order order) throws ParsingException, IOException {
        MailGenerator mailGenerator = map.get(typeOfMail);
        if (mailGenerator == null) {
            throw new UnsupportedOperationException(typeOfMail + " can not generate message");
        }
        rabbitTemplate.convertAndSend(
                rabbitMqProperties.getExchange(),
                rabbitMqProperties.getRoutingKey(),
                mailGenerator.generate(order)
        );

    }












}