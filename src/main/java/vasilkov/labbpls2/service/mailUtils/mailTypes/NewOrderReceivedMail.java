package vasilkov.labbpls2.service.mailUtils.mailTypes;

import lombok.RequiredArgsConstructor;
import nu.xom.ParsingException;
import org.springframework.stereotype.Component;
import vasilkov.labbpls2.entity.Order;
import vasilkov.labbpls2.exception.ResourceIsNotValidException;
import vasilkov.labbpls2.service.impl.XMLServiceImpl;
import vasilkov.labbpls2.service.mailUtils.MailModel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component @RequiredArgsConstructor
public class NewOrderReceivedMail implements MailGenerator{

    @Override
    public MailModel generate(Order order) throws ParsingException, IOException {
        //todo
        Optional<List<String>> adminEmail = XMLServiceImpl.getAllAdminsEmail();
        if (adminEmail.isEmpty())
            throw new ResourceIsNotValidException("Админов не сущетствует!)");
        String mailMessage = "Здравствуйте!\n" +  "Вам пришло новое объявление на модерацию!\n"
                + "Id этого объявления: " + order.getId() ;
        return new MailModel(
                adminEmail.get().get(0),
                "КОМУС - Новое объявление",
                mailMessage

        );
    }

    @Override
    public String getType() {
        return "NEWORDER";
    }
}
