package mailing;

import org.joda.time.DateTime;

import javax.mail.*;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GmailClient {

    private Store store;

    public GmailClient(Properties gmailProperties) {
        String user = gmailProperties.getProperty("gmail.user");
        String password = gmailProperties.getProperty("gmail.password");

        Properties properties = System.getProperties();
        properties.setProperty("mail.store.protocol", "imaps");
        properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imap.socketFactory.fallback", "false");

        Session session = Session.getDefaultInstance(properties, null);

        try {
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", user, password);
        } catch (MessagingException e) {
            Logger.getGlobal().log(Level.WARNING, "cannot connect to gmail service");
        }
    }

    public List<MailWrapper> getLastHoursMails(int hours) throws GmailClientException {
        return getLastHoursMails(hours, "[Gmail]/Todos");
    }

    public List<MailWrapper> getLastHoursMails(int hours, String inboxName) throws GmailClientException {
        Folder inbox = null;

        try {
            inbox = store.getFolder(inboxName);
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.search(lastHoursSearchTerm(hours));

            return Arrays.stream(messages)
                    .map(MailWrapper::createInstance)
                    .collect(Collectors.toList());
        } catch (MessagingException e) {
            throw new GmailClientException(e);
        } finally {
            closeFolder(inbox);
        }
    }

    public List<MailWrapper> getMailsSince(DateTime fromDate, String inboxName) throws GmailClientException {
        Folder inbox = null;

        try {
            inbox = store.getFolder(inboxName);
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.search(sinceSearchTerm(fromDate));

            return Arrays.stream(messages)
                    .map(MailWrapper::createInstance)
                    .collect(Collectors.toList());
        } catch (MessagingException e) {
            throw new GmailClientException(e);
        } finally {
            closeFolder(inbox);
        }
    }

    private SearchTerm sinceSearchTerm(DateTime fromDate) {
        DateTime rightNow = new DateTime();

        SearchTerm olderThan = new ReceivedDateTerm(ComparisonTerm.LE, rightNow.toDate());
        SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GE, fromDate.toDate());

        return new AndTerm(newerThan, olderThan);
    }

    private SearchTerm lastHoursSearchTerm(int hours) {
        DateTime rightNow = new DateTime();
        DateTime past = rightNow.minusHours(hours);

        SearchTerm olderThan = new ReceivedDateTerm(ComparisonTerm.LE, rightNow.toDate());
        SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GE, past.toDate());

        return new AndTerm(newerThan, olderThan);
    }

    private void closeFolder(Folder inbox) {
        try {
            if (inbox != null)
                inbox.close(false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static class GmailClientException extends Throwable {
        private final String message;

        public GmailClientException(Exception e) {
            message = e.getMessage();
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
}
