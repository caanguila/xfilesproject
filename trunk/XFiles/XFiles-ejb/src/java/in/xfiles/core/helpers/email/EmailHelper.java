package in.xfiles.core.helpers.email;

import java.text.MessageFormat;

/**
 *
 * @author 7
 */
public class EmailHelper {
    
    public static final String PASSWORD_RECOVERY_TEMPLATE = 
            "<html><body><p>Hello, {0}. </p>\n"
            + "<p>Password from your <a href=\"http://xfiles.in/XFiles-war/faces/login.xhtml?from=recovery-email\">xfiles.in</a> "
            + "account has been reset. \n"
            + "New password: <strong>{1}</strong></p>\n\n"
            + "<p>--<br/>\nThank you for choosing our services. Stay with us! <a href=\"http://xfiles.in/XFiles-war/\">XFiles.IN</a></p>\n<br/>\n<br/>\n"
            + "<p>--<br/>\nPlease, do not reply to this messages. You can contact our administrator via admin@xfiles.in</p></body></html>";
    
    public static EmailObject createEmailFromTemplate(String subject, String template, Object... params) {
        final EmailObject email = new EmailObject();
        email.setSubject(subject);
        email.setText(MessageFormat.format(template, params));
        return email;
    }
    
    public static EmailObject createPasswordRecoveryEmail(String userName, String newPassword) {
        return createEmailFromTemplate("XFiles.IN - Password recovery", PASSWORD_RECOVERY_TEMPLATE, userName, newPassword);
    }
}
