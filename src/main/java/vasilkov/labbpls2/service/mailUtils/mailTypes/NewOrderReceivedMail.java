package vasilkov.labbpls2.service.mailUtils.mailTypes;

import lombok.RequiredArgsConstructor;
import nu.xom.ParsingException;
import org.springframework.stereotype.Component;
import vasilkov.labbpls2.entity.Order;
import vasilkov.labbpls2.exception.ResourceIsNotValidException;
import vasilkov.labbpls2.service.XMLService;

import javax.jms.TextMessage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component @RequiredArgsConstructor
public class NewOrderReceivedMail implements MailGenerator{

    @Override
    public String generate(Order order) throws ParsingException, IOException {

        Optional<List<String>> adminEmail = XMLService.getAllAdminsEmail();
        if (adminEmail.isEmpty())
            throw new ResourceIsNotValidException("Админов не сущетствует!)");

        String mailMessage = "{\"to\": \"" + adminEmail.get().get(0)
                + "\",\"subject\": \"КОМУС\", \"text\": \"Здравствуйте! Вам пришло новое объявление на модерацию! Id этого объявления:"
                + order.getId()+"\"}";
//        return new MailModel(
//                adminEmail.get().get(0),
//                "КОМУС - Новое объявление",
//                mailMessage
//        );

        return  mailMessage;
    }

    @Override
    public String getType() {
        return "NEWORDER";
    }
}
