package jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;

public class FileServerWithXml {

	public static void main(String[] args) throws Exception {
		Resource resource = Resource
				.newClassPathResource("fileserver.xml");
		XmlConfiguration configuration = new XmlConfiguration(
				resource.getInputStream());
		Server server = (Server) configuration.configure();
		server.start();
		server.join();
	}

}
