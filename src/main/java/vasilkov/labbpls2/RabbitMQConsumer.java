//package vasilkov.labbpls2;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import vasilkov.labbpls2.service.mailUtils.MailModel;
//import org.springframework.jms.annotation.JmsListener;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class RabbitMQConsumer {
//
////    @JmsListener(destination = "${rabbit-mq.queue}")
////    public void processMyQueue(MailModel message) {
////        log.info("Received from myQueue : {} ", message);
////    }
//}