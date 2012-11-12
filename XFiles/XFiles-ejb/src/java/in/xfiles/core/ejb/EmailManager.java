package in.xfiles.core.ejb;

import in.xfiles.core.helpers.StringUtils;
import java.util.Date;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;

/**
 * Provides basic routines for sending and receiving messages.
 * 
 * @author danon
 */
@Stateless
public class EmailManager implements EmailManagerLocal {
    
    @Resource(name = "mail/XFiles-NoReply")
    private Session noReplySession;

    private final Logger log = Logger.getLogger(EmailManager.class);
    
    /**
     * Sends plain-text email to specified recipients.
     * @param subject Subject of the message
     * @param text Body of the message
     * @param recepients target e-mails
     */
    @Asynchronous
    public void sendSimpleEmail(String subject, String text, String ... recepients) {
        try {
            Message m = new MimeMessage(noReplySession);
            Address[] addrs = InternetAddress.parse(StringUtils.concat(recepients, ";"), false);
            m.setRecipients(Message.RecipientType.TO, addrs);
            m.setSubject(subject);
            m.setFrom(InternetAddress.parse("noreply@xfiles.in", false)[0]);
            m.setText(text);
            m.setSentDate(new Date());
            Transport.send(m);
        } catch (Exception ex) {
            log.error("sendSimpleEmail(): failed to send message.", ex);
        }
    }

}
