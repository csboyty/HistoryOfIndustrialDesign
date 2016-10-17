package jetty;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringWithServer {
	public static void main(String[] args) throws Exception {
		new ClassPathXmlApplicationContext("jetty-spring.xml");
	}

}
