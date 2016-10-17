package jetty;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebAppContextWithWarServer {

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);

//		Constraint constraint = new Constraint();
//		constraint.setName(Constraint.__BASIC_AUTH);;
//		constraint.setRoles(new String[]{"user","admin","moderator"});
//		constraint.setAuthenticate(true);
		 
//		ConstraintMapping cm = new ConstraintMapping();
//		cm.setConstraint(constraint);
//		cm.setPathSpec("/*");
		
		
		
		WebAppContext context = new WebAppContext();
		context.setContextPath("/myapp");
//		context.setResourceBase("webapps/examples");
		context.setWar("webapps/test.war");
		context.getSecurityHandler().setLoginService(new HashLoginService("MyRealm","etc/realm.properties"));
		
		HandlerCollection handlers= new HandlerCollection();
		handlers.setHandlers(new Handler[]{context, new DefaultHandler()});
		
		
		server.setHandler(context);
		server.start();
		server.join();
	}
}
