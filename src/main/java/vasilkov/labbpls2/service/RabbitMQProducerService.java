package vasilkov.labbpls2.service;

import nu.xom.ParsingException;
import vasilkov.labbpls2.entity.Order;

import java.io.IOException;

public interface RabbitMQProducerService {
    void sendMessage(String typeOfMail, Order order) throws ParsingException, IOException;
}
