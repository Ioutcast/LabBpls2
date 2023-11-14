package vasilkov.labbpls2.service.mailUtils.mailTypes;

import lombok.RequiredArgsConstructor;
import nu.xom.ParsingException;
import org.springframework.stereotype.Component;
import vasilkov.labbpls2.entity.Order;
import vasilkov.labbpls2.service.impl.UserServiceImpl;
import vasilkov.labbpls2.service.mailUtils.MailModel;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class StatusChangedMail implements MailGenerator{
    private final UserServiceImpl userServiceImpl;

    @Override
    public MailModel generate(Order order) throws ParsingException, IOException {
        String status = order.getStatus() ? "прошла успешно!" : "не одобрена";
        String mailMessage =
                "Здравствуйте, " + userServiceImpl.getByEmail(order.getUserEmail()).get().getFirstName() + "!\n" +
                        "Ваша заявка прошла модерацию!\n" +
                        "Заявка на " + order.getDescription() + " " + status + "\n"
                        + "Спасибо!";

        return new MailModel(
                order.getUserEmail(),
                "КОМУС - Изменился статус объявления",
                mailMessage

        );
    }

    @Override
    public String getType() {
        return "STATUS";
    }
}
