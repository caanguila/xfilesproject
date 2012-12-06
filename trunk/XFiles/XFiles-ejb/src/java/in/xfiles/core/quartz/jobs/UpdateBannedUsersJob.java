/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.xfiles.core.quartz.jobs;

import in.xfiles.core.entity.User;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.util.List;
import java.util.Date;
import java.util.logging.Level;
/**
 *
 * @author 7
 */
public class UpdateBannedUsersJob extends BaseJob {
private final Logger log = Logger.getLogger(UpdateBannedUsersJob.class);

private final static long PUNISH_MIN_TIME = 5;
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            Date actualDate = new Date(System.currentTimeMillis()-PUNISH_MIN_TIME*1000*60);
            log.debug("Hi! I'm trying to help banned users!");
             EntityManager em = getEntityManager();
             //Should see all users with suspend date!=null and decide on their guilt
             beginTransaction();
             em.createQuery("update User u set u.dateSuspended =:nullDate where u.dateSuspended < :dateUnlock ")
                     .setParameter("nullDate", (Date)null)
                     .setParameter("dateUnlock", actualDate).executeUpdate();
                     
//             if(!suspended.isEmpty()){
//                 for(User u:suspended){
//                     
//                     Date sDate = u.getDateSuspended();
//                     long diff = ( actualDate.getTime()-sDate.getTime() );
//                     log.debug("diff: "+diff+" act: "+actualDate+"  susp: "+sDate);
//                     if(diff > PUNISH_MIN_TIME*1000*60){
//                         u.setDateSuspended((Date)null);
//                         em.merge(u);
//                     }
//                 }
//                 
//             }
            em.flush();
            commitTransaction();
        } catch (Exception ex) {
            try {
                rollbackTransaction();
            } catch (Exception ex1) {
                java.util.logging.Logger.getLogger(UpdateBannedUsersJob.class.getName()).log(Level.SEVERE, null, ex1);
            }
            java.util.logging.Logger.getLogger(UpdateBannedUsersJob.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            finish();
        }
    }
    
    @Override
    public boolean isTransacted() {
        return true;
    }
}
