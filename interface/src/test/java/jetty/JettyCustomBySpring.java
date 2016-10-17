package jetty;

import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;


public class JettyCustomBySpring 
{
    public static void main( String[] args ) throws Exception
    {
    	 Resource config = Resource.newResource("etc/jetty-spring.xml");
    	 XmlConfiguration.main(config.getFile().getAbsolutePath());
    }
}
