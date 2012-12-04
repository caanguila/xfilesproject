package in.xfiles.core.quartz.jobs;

import in.xfiles.core.entity.DownloadRequest;
import in.xfiles.core.helpers.StringUtils;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Quartz Scheduler job for updating statuses of download requests.
 * All requests with READY status should expire after 2 hours 
 * with removing corresponding file.
 *
 * @author danon
 */
public class UpdateRequestExpirationJob extends BaseJob {

    private final Logger log = Logger.getLogger(UpdateRequestExpirationJob.class);
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        log.debug("execute(): job started");
        Date date = new Date(System.currentTimeMillis() - 60*60*1000*2); // 2 hours before now

        EntityManager em = getEntityManager();
        try {
            beginTransaction();
            final List<String> filesToDelete = (List<String>)em.createQuery("select r.outputFile from DownloadRequest r"
                    + " where r.status = :readyStatus and r.dateProvided < :dateProvided")
                    .setParameter("readyStatus", DownloadRequest.READY_STATUS)
                    .setParameter("dateProvided", date)
                    .getResultList();
            if(filesToDelete.isEmpty()) {
                if(log.isTraceEnabled()) {
                    log.trace("execute(): Nothing to update/remove.");
                    rollbackTransaction();
                    return;
                }
            }
            int r = em.createQuery("update DownloadRequest r set r.status = :newStatus"
                    + " where r.status = :readyStatus and r.dateProvided < :dateProvided")
                    .setParameter("newStatus", DownloadRequest.EXPIRED_STATUS)
                    .setParameter("readyStatus", DownloadRequest.READY_STATUS)
                    .setParameter("dateProvided", date)
                    .executeUpdate();
            commitTransaction();
            em.flush();
            if(log.isTraceEnabled()) {
                log.trace("execute(): "+r+" request have been marked EXPIRED");
            }
            r = 0;
            for(String file : filesToDelete) {
                if(!StringUtils.isEmpty(file)) {
                    try {
                        FileUtils.forceDelete(new File(file));
                        r++;
                    } catch (IOException ex) {
                        if(log.isDebugEnabled()) {
                            log.debug("execute(): failed to delete file "+file+".", ex);
                        }
                    }
                }
            }
            if(log.isTraceEnabled()) {
                log.trace("execute(): "+r+" files have been removed.");
            }
            filesToDelete.clear();
        } catch (Exception ex) {
            log.error("execute(): job execution failed.", ex);
            try {
                rollbackTransaction();
            } catch (Exception e) {
                
            }
        } finally {
            finish();
        }
    }
    
    @Override
    public boolean isTransacted() {
        return true;
    }
    
    protected void finish() {
        EntityManagerFactory emf = getEntityManager().getEntityManagerFactory();
        getEntityManager().close();
        emf.close();
    }
    
}
