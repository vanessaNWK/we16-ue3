package at.ac.tuwien.big.we16.ue3.servlet;

import at.ac.tuwien.big.we16.ue3.service.ServiceFactory;
import org.apache.log4j.PropertyConfigurator;
import org.h2.tools.Server;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class ShutdownListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String log4jConfigFile = "/home/vanessa/Dokumente/TU_Wien/4. Semester/Webengineering/UE3-Angabe/assignment/src/main/resources/log4j2.xml";
        PropertyConfigurator.configure(log4jConfigFile);
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer("GZ6tiy1XyB9W0P4xEJudQ","gaJDlW0vf7en46JwHAOkZsTHvtAiZ3QUd2mD1x26J9w");
        twitter.setOAuthAccessToken(new AccessToken("1366513208-MutXEbBMAVOwrbFmZtj1r4Ih2vcoHGHE2207002", "RMPWOePlus3xtURWRVnv1TgrjTyK7Zk33evp4KKyA"));

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServiceFactory.getNotifierService().stop();
        ServiceFactory.getComputerUserService().stopAll();
    }
}
