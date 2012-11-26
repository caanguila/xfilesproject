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
public class TestBean1 implements Serializable {

    /**
     * Creates a new instance of TestBean1
     */
    public TestBean1() {
        System.err.println("TestBean1.<init>: !!!");
    }
    
    public void testAction(ActionEvent evt) {
        System.err.println("TestBean1.testAction: !!!");
    }
}
