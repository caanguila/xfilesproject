package in.xfiles.core.ejb;


import in.xfiles.core.quartz.jobs.UpdateRequestExpirationJob;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.apache.log4j.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author danon
 */
@Singleton
@LocalBean
@Startup
public class AppStart {
    @PersistenceContext
    private EntityManager entityManager;
    
    private static final Logger log = Logger.getLogger(AppStart.class);
    
    private Scheduler scheduler; 
    
    @PostConstruct
    public void startup()
    {
        try {
            init();
            clearSessions();
            scheduleJobs();
            log.info("startup(): Application Initialized");
        } catch (Exception ex) {
            log.error("(startup(): Failed to initialize application.", ex);
            throw new RuntimeException(ex);
        }
    }
    
    private void init() throws SchedulerException {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
    }
    
    private void clearSessions() {
        entityManager.createQuery("DELETE FROM UserSession s").executeUpdate();
    }
    
    private void scheduleJobs() {
        try {
            JobDetail updateRequestExpirationJob = JobBuilder.newJob(UpdateRequestExpirationJob.class)
                .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInMinutes(5)
                    .repeatForever())
                .build();
            scheduler.scheduleJob(updateRequestExpirationJob, trigger);
        } catch (Exception ex) {
            log.error("scheduleJobs(): failed to schedule UpdateRequestExpirationJob.", ex);
        }
    }
    
    @PreDestroy
    public void shutdown() {
        log.info("shutdown(): Application is about to shutdown...");
        try {
            if(scheduler!= null)
                scheduler.shutdown();
            log.info("shutdown(): rest in piece.");
        } catch (Exception ex) {
            log.error("shutdown(): Something wrong has happened.", ex);
        }
    }
    
//    private void log4jInit() {
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        URL url = classLoader.getResource("/log4j.xml");
//
//        DOMConfigurator.configure(url.getFile());
//        log = Logger.getLogger(Startup.class);
//        if (log != null) {
//            log.debug("Logger initialized with " + url);
//        }
//    }

}
