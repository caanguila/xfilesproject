/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.Messages;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Stateless;

/**
 *
 * @author 7
 */
@Stateless
public class MessageManager implements MessageManagerLocal{
    
    public Collection<Messages> getUserMessages(BigInteger userId){
        return null;
    }
    
    public Collection<Messages> getMessagesByDate(Date creation, Date recieve){
        return null;
    }
    
   
    
}
