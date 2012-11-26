/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.entity.Groups;
import in.xfiles.core.entity.User;
import in.xfiles.core.helpers.ShamirSchema;
import java.util.Collection;
import java.util.HashMap;
import javax.ejb.Stateless;

/**
 *
 * @author 7
 */
@Stateless
public class SequreManager implements SequreManagerLocal{

    @Override
    public HashMap<Integer, String> splitShare(String secret, int n, Groups group) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HashMap<Integer, String> splitShare(String secret, int n, int k) {
        return ShamirSchema.splitShare(secret, n, k);
    }

    @Override
    public String combineSecret(HashMap<Integer, String> parts) {
        return ShamirSchema.combineSecret(parts);
    }

    @Override
    public String combineSecret(Collection<User> users) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
