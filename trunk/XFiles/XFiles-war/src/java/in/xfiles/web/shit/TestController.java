package in.xfiles.web.shit;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author danon
 */
@ManagedBean
@ViewScoped
public class TestController implements Serializable {

    private int rendered = 1;
    
    /**
     * Creates a new instance of TestController
     */
    public TestController() {
        System.err.println("TestController.<init>: !!!");
    }

    public int getRendered() {
        return rendered;
    }

    public void setRendered1(ActionEvent evt) {
        System.err.println("TestController.serTendered1: !!!");
        this.rendered = 1;
    }
    
    public void setRendered2(ActionEvent evt) {
        System.err.println("TestController.setRendered2: !!!");
        this.rendered = 2;
    }
}
