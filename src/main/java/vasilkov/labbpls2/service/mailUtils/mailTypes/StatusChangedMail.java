package vasilkov.labbpls2.service.mailUtils.mailTypes;

import lombok.RequiredArgsConstructor;
import nu.xom.ParsingException;
import org.springframework.stereotype.Component;
import vasilkov.labbpls2.entity.Order;
import vasilkov.labbpls2.service.UserService;
import vasilkov.labbpls2.service.mailUtils.MailModel;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class StatusChangedMail implements MailGenerator{
    private final UserService userService;

    @Override
    public String generate(Order order) throws ParsingException, IOException {
        String status = order.getStatus() ? "прошла успешно!" : "не одобрена";
        String mailMessage =
                "{\"to\": \"" + order.getUserEmail()
                        + "\",\"subject\": \"КОМУС\", \"text\": \"Ваша заявка прошла модерацию!"
                        + status+"\"}";

//        return new MailModel(
//                order.getUserEmail(),
//                "КОМУС - Изменился статус объявления",
//                mailMessage
//
//        );
        return  mailMessage;
    }

    @Override
    public String getType() {
        return "STATUS";
    }
}
