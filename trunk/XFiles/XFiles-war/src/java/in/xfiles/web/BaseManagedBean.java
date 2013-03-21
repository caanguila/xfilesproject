package in.xfiles.web;

import in.xfiles.web.utils.JSFHelper;
import java.io.Serializable;

/**
 * Base class for all JSF managed beans.
 * 
 * @author danon
 */
public abstract class BaseManagedBean implements Serializable {
    
    /** 
     * Creates new instance of JSFHelper.
     * @return new instance of JSFHelper initialized by current instance of Faces Context.
     */
    protected JSFHelper getJSFHelper() {
        return new JSFHelper();
    }
    
}
