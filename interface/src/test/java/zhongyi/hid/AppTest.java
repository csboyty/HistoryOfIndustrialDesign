package zhongyi.hid;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Properties;

import net.sf.cglib.beans.BeanMap;

import org.apache.commons.io.FileUtils;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.google.common.io.Closeables;
import com.zhongyi.hid.dto.BundleList.HidBundle;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws IOException 
     */
    public void testBeanMap(){
    	HidBundle b =new HidBundle("1","文章1", "1232322", "http://a/b/c/1.zip",
				"123343300", "4203u432oyoadff");
    	System.out.println(BeanMap.create(b));
    }
    
    public void testApp() throws IOException
    {
    	String s= "s";
    	s.replaceAll("pat", "rep");
    	File a1File=new File("etc","b/1");
    	FileUtils.writeByteArrayToFile(a1File, new byte[]{0x1,'A',127,4,5});
    	
        assertTrue( true );
        Properties p = new Properties();
        p.load(AppTest.class.getClassLoader().getResourceAsStream("system.properties"));
        System.out.println(p);
        
        BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(AppTest.class.getResourceAsStream("app.properties")));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Reading standard css", e);
		} finally {
			Closeables.closeQuietly(in);
		}
        
    }
}
