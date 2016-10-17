import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.zhongyi.hid.util.JsonUtil;



public class JsonTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String s = FileUtils.readFileToString(new File("D:/json.txt"), "UTF-8");
		System.out.println(JsonUtil.getJsonObj(s));
	}

}
