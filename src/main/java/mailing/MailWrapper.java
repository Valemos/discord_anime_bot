package mailing;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MailWrapper {
    private final String message;

    public static MailWrapper createInstance(Message message) {
        try {
            if (message.isMimeType("text/plain")) {
                return new MailWrapper((String) message.getContent());
            }
        } catch (MessagingException | IOException e) {
            Logger.getGlobal().log(Level.WARNING, "cannot read message correctly");
        }

        return new MailWrapper("");
    }

    public MailWrapper(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
