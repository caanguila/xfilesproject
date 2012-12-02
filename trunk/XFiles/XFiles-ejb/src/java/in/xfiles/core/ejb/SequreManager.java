/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.crypto.FileEncryptor;
import in.xfiles.core.entity.*;
import in.xfiles.core.helpers.CommonTools;
import in.xfiles.core.helpers.ShamirSchema;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
/**
 *
 * @author 7
 */
@Stateless
public class SequreManager implements SequreManagerLocal{

    private Logger log = Logger.getLogger(SequreManager.class);
    @PersistenceContext
    private EntityManager em;
    @EJB
    private LogManagerLocal lm;
    @EJB
    private FileManagerLocal fml;
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

    @Override
    public void acceptRequestByUser(Messages message) {
        Long groupId = message.getGroupId();
        User recipient = message.getRecipientId();
        User sender = message.getSenderId();
        Groups gr = em.find(Groups.class, groupId);
        if(gr == null){
            log.warn("Incorrect group id: "+groupId);
            return;
        }
        Files file = gr.getFilesCollection().iterator().next();
        List<DownloadRequest> resList = em.createQuery("select u from DownloadRequest u where u.user =:userId and u.file =:fileId")
                .setParameter("userId", sender)
                .setParameter("fileId", file).getResultList();
        if(resList.isEmpty()) {
            log.warn("There is no suitable requests for message: "+message);
            return;
        }
        
        DownloadRequest req = resList.get(0);
        if(validatePropForUserId(req, recipient)){
            log.debug("modify request: "+req.getId());
            String param = "user_id="+recipient.getUserId();
            String prop = req.getProperties();
            if("".equals(prop)){
                req.setProperties(param);
            }else{
                prop+=":"+param;
                req.setProperties(prop);
            }
            em.merge(req);
        }else{
            log.debug("User: "+recipient.getUserId()+" has already accepted request ");
        }
        
        log.debug("Can I download group file:"+validateDownloadGroupFile(req));
        if(validateDownloadGroupFile(req)){
            fml.completeGroupFile(req);
            //Delete all messages connecting with group file
            List<Messages> messages = em.createQuery("select u from Messages u where u.groupId=:group and u.senderId=:sender and u.typeId =:type")
                   .setParameter("group", gr.getGroupId())
                   .setParameter("sender", message.getSenderId())
                   .setParameter("type", message.getTypeId()).getResultList();
                for(Messages m: messages ){
                    
                    em.remove(m);
                    }
            }
           
    }
    
    
    
    private boolean validatePropForUserId(DownloadRequest req, User u){
        String prop = req.getProperties();
        String[] parts1 = prop.split(":");
        for(String p1: parts1){
            
            String[] parts2 = p1.split("=");
            for(int i=0; i<parts2.length; i++){
                if(parts2[i].equals("user_id")){
                    if(i+1<parts2.length){
                        String value = parts2[i+1];
                        Long val = new Long(value);
                        log.debug("Parametr: "+parts2[i]+"  value: "+val);
                        if(val.equals(u.getUserId())) return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    
    
    private boolean validateDownloadGroupFile(DownloadRequest req){
        int count = 0;
        String prop = req.getProperties();
        ArrayList result = CommonTools.parceElements(prop);
        
        for(int i=0; i<result.size(); i+=2){
            log.debug(result.get(i)+" value: "+result.get(i+1));
            if(result.get(i).equals("user_id")) count++;
        }
        
        String attr = req.getFile().getEnFileId().getAttributes();
        if(attr == null){
            log.warn("Something Strange with attributes");
            return false;
        }
        Integer k = new Integer(attr);//Bad Practice
        log.debug("People accepted requestId="+req.getId()+"  "+count+"  need: "+k);
        if(k<=count) return true; 
        return false;
    }


    
}
