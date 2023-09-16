package vasilkov.labbpls2.service.mailUtils.mailTypes;

import nu.xom.ParsingException;
import vasilkov.labbpls2.entity.Order;

import java.io.IOException;

public interface  MailGenerator {
    String generate(Order order) throws ParsingException, IOException;
    String getType();
}
