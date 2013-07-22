package i2.application.extranet.filtres;

import i2.application.extranet.business.authentification.IExTypeDroitManager;

import javax.servlet.ServletContextEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;



public class ContextLoaderListener extends
		org.springframework.web.context.ContextLoaderListener {

	@SuppressWarnings("unused")
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);

		ApplicationContext contextLoader = (ApplicationContext)ContextLoader.getCurrentWebApplicationContext();
		
		IExTypeDroitManager exTypeDroitManager = (IExTypeDroitManager)contextLoader.getBean("exTypeDroitManager");
		exTypeDroitManager.readAllExTypeDroit();	
	}

}
