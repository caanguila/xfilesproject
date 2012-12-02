/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.Messages;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author 7
 */
@Local
public interface MessageManagerLocal {
     Collection<Messages> getUserInputMessages(Long userId);
    
     Collection<Messages> getMessagesByDate(Date creation, Date recieve);
     
     void sendMessage(Long recipientId, Long senderId, String message, Long typeId, Long groupId);
     
     void sendGruopMessage(Long groupId,String message, Long typeId, Long senderId);
}
