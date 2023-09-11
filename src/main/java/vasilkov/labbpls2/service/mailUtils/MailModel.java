package vasilkov.labbpls2.service.mailUtils;
import lombok.Data;

import java.io.Serializable;

@Data
public class MailModel implements Serializable {

    private String to;
    private String subject;
    private String text;

    public MailModel(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }
}