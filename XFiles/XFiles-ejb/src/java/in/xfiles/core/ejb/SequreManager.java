/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.ejb;

import in.xfiles.core.crypto.FileEncryptor;
import in.xfiles.core.entity.*;
import in.xfiles.core.helpers.CommonConstants;
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

    private static final int MINUTES_ACT = 2;
    private static final int MAX_TRYES = 3;
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
    public boolean approveRequestByUser(Messages message) {
        Long groupId = message.getGroupId();
        User recipient = message.getRecipientId();
     //   User sender = message.getSenderId();
        Groups gr = em.find(Groups.class, groupId);
        if(gr == null){
            log.warn("Incorrect group id: "+groupId);
            return false;
        }
      //  Files file = gr.getFilesCollection().iterator().next();
        
        ArrayList<String> params = CommonTools.parceElements(message.getOptions());
        if(params.isEmpty()){
            log.warn("Notification message has no params like: request_id=some_string");
            return false;
        }
        String requestId="";
        for(int i=0; i<params.size(); i+=2){
          if(params.get(i).equals("request_id")){
             requestId = params.get(i+1);             
          }  
        }
//        List<DownloadRequest> resList = em.createQuery("select u from DownloadRequest u where u.user =:userId and u.file =:fileId")
//                .setParameter("userId", sender)
//                .setParameter("fileId", file).getResultList();
//        if(resList.isEmpty()) {
//            log.warn("There is no suitable requests for message: "+message);
//            return;
//        }
        
  //      DownloadRequest req = resList.get(0);//I think there should be the last user request
        DownloadRequest req = em.find(DownloadRequest.class, new Long(requestId));
        if(req == null){
            log.warn("Can't find download request!");
            return false;
        }
        boolean result = false;
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
            result = true;
        }else{
            
            log.debug("User: "+recipient.getUserId()+" has already accepted request ");
        }
        
        log.debug("Can I download group file:"+validateDownloadGroupFile(req));
        if(validateDownloadGroupFile(req)){
            fml.completeGroupFile(req);
            //Delete all messages connecting with group file
            List<Messages> messages = em.createQuery("select u from Messages u where u.groupId=:group and u.senderId=:sender and u.typeId =:type and u.options =:messOptions")
                   .setParameter("group", gr.getGroupId())
                   .setParameter("sender", message.getSenderId())
                   .setParameter("type", message.getTypeId())
                   .setParameter("messOptions", message.getOptions()).getResultList();
                for(Messages m: messages ){
                    
                    em.remove(m);
                    }
            }
           
        return result;
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

    @Override
    public void validateUserInput(String options) {
        ArrayList<String> result = CommonTools.parceElements(options);
        if(result.isEmpty()){
            log.warn("Validation of login user failed");
            
        }else{
            String login="";
            String password = "";
            for(int i=0; i< result.size(); i++){
                if(result.get(i).equals("login")) login = result.get(i+1);
                if(result.get(i).equals("password")) password = result.get(i+1);
            }
            log.info("User try to login: "+login+"  "+password);
            if(!login.equals("") && !password.equals("")){
                Date actDate = new Date(System.currentTimeMillis() - MINUTES_ACT*60*1000);
                
                String regexp = "%login="+login+":%";
                List<Log> list = em.createQuery("select u from Log u where u.options like :regexp and u.dateCreation > :actualDate order by u.dateCreation")
                        .setParameter("regexp", regexp)
                        .setParameter("actualDate", actDate).getResultList();
                
                int counter = 0;
                boolean ban = false;
               
                for(int i=0; i<list.size(); i++){
                    Log l = list.get(i);
                    //log.info("log record: "+l.getLogId()+"  date: "+l.getDateCreation()+"  options: "+l.getOptions());
                    if(l.getTypeActionId().getActionTypeId().equals(CommonConstants.SUCCESS_LOGIN)) counter = 0;
                    if(l.getTypeActionId().getActionTypeId().equals(CommonConstants.USER_LOGIN)) counter++;
                    if(counter >= MAX_TRYES ){
                        //permision denyed
                        ban = true;
                       // log.info("User has benn suspended: "+login+"  dateSuspended: "+new Date());
                    }
                    
                }
                
                if(ban){
                    List<User> user = em.createQuery("select u from User u where u.email=:login")
                            .setParameter("login", login).getResultList();
                    if(user.isEmpty()){
                        log.error("Can't suspend user with login: "+login);
                        return;
                    }else{
                        User u = user.get(0);
                        if(u.getDateSuspended()==null){
                            u.setDateSuspended(new Date());
                            em.merge(u);
                        }
                        //Should be logged for this user;
                        log.info("User "+u+" is suspended at "+new Date());
                    }
                }
                
                
            }
        }
        
    }


    
}
