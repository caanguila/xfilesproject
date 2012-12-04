package in.xfiles.core.quartz.jobs;

import in.xfiles.core.helpers.EJBHelper;
import javax.persistence.EntityManager;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author danon
 */
public abstract class BaseJob implements Job {
    
    private EntityManager entityManager;
    private UserTransaction userTransaction;
    
    public BaseJob() {
        init();
    }
    
    private void init() {
        try {
            entityManager = EJBHelper.getInstance().getEntityManager();
            if(isTransacted()) {
                EJBHelper.getInstance().resolve("java:comp/UserTransaction", UserTransaction.class);
            }
        } catch (Exception ex) {
            System.err.println("BaseJob.init(): failed");
            ex.printStackTrace(System.err);
        }
    }
    
    @Override
    public abstract void execute(JobExecutionContext jec) throws JobExecutionException;
    
    protected boolean isTransacted() {
        return false;
    }
    
    protected UserTransaction getUserTransaction() {
        return userTransaction;
    }
    
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    protected void beginTransaction() throws Exception {
        UserTransaction utx = getUserTransaction();    
        if(utx!= null)
                getUserTransaction().begin();
    }
    
    protected void commitTransaction() throws Exception {
        UserTransaction utx = getUserTransaction();    
        if(utx!= null)
            utx.commit();
    }
    
    protected void rollbackTransaction() throws Exception {
        UserTransaction utx = getUserTransaction();    
        if(utx!= null)
            utx.rollback();
    }
    
}
