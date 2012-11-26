/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.ActionTypes;
import in.xfiles.core.entity.Log;
import in.xfiles.core.entity.User;
import javax.ejb.Local;
import java.util.Collection;
/**
 *
 * @author 7
 */
@Local
public interface LogManagerLocal {
    Log addRecord(Long userId, Long typeActionId, String message,String options, String sessionId);
    Log addRecord(User user, ActionTypes type, String message,String options, String sessionId);
    Log addRecord(User user, String typeName, String message,String options, String sessionId);
    Log addRecord(Long userId, String typeName, String message,String options, String sessionId);
    Collection<ActionTypes> getAllActionTypes();
    Collection<ActionTypes> getActionTypesByName(String name);
    Collection<Log> getRecordsByUser(User user);
}
