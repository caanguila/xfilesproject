package in.xfiles.core.helpers.email;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author danon
 */
public class EmailObject {
    private String subject;
    private String text;
    private List<File> attachments;

    public EmailObject() {
        attachments = new ArrayList<File>();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<File> attachments) {
        this.attachments = attachments;
    }
 }
