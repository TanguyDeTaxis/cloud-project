package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;

import entity.Account;
import entity.Approval;

public class MyListener implements ServletContextListener {
	public void contextInitialized(ServletContextEvent event) {
		ObjectifyService.init();
                ObjectifyService.register(Account.class);
                ObjectifyService.register(Approval.class);
		// etc...
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}
}
