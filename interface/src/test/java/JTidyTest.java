import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;
import org.w3c.tidy.Tidy;

/**
 * @Title: JTidyTest.java
 * @Package 
 * @Description: TODO(添加描述)
 * @author zhongzhenyang at gmail.com
 * @date 2013-9-11 上午9:00:48
 * @version V1.0
 */
public class JTidyTest {

	/**
	 * @throws IOException 
	 * @Title: main
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param args    参数类型
	 * @return void    返回类型
	 * @throws
	 */

	public static void main(String[] args) throws IOException {
		Tidy tidy = new Tidy(); 
		tidy.setEncloseText(true);
		tidy.setEncloseBlockText(true);
		tidy.setQuoteAmpersand(true);
		tidy.setQuoteNbsp(true);
		tidy.setForceOutput(true);
		tidy.setInputEncoding("UTF-8");
		tidy.setOutputEncoding("UTF-8");
		tidy.getConfiguration().printConfigOptions(new PrintWriter(System.out), true);
		
		InputStream in = new FileInputStream("original.html");
		OutputStream out = new FileOutputStream("out.html");
		tidy.parse(in, out);
		IOUtils.closeQuietly(in);
		IOUtils.closeQuietly(out);
		

	}

}

