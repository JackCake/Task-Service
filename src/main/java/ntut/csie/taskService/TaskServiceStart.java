package ntut.csie.taskService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import ntut.csie.taskService.gateways.database.SqlDatabaseHelper;
import ntut.csie.taskService.useCase.DomainEventListener;

@SuppressWarnings("serial")
public class TaskServiceStart extends HttpServlet implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("Task Service Start!");
		SqlDatabaseHelper sqlDatabaseHelper = new SqlDatabaseHelper();
		sqlDatabaseHelper.initialize();
		ApplicationContext context = ApplicationContext.getInstance();
		DomainEventListener.getInstance().init(context.newTaskRepository(), context.newEventStore());
	}
}