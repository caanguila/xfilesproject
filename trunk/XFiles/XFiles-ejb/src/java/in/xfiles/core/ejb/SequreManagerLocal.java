/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.Groups;
import in.xfiles.core.entity.Messages;
import in.xfiles.core.entity.User;
import java.util.HashMap;
import java.util.Collection;
import javax.ejb.Local;

/**
 *
 * @author 7
 */
@Local
public interface SequreManagerLocal {
    
  HashMap<Integer, String> splitShare(String secret, int n, Groups group);
  HashMap<Integer, String> splitShare(String secret, int n, int k);
  String combineSecret(HashMap<Integer, String> parts);
  String combineSecret(Collection<User> users);
  void acceptRequestByUser(Messages message);
  void validateUserInput(String options);
}
