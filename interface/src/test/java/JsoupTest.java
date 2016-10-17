import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @Title: JsoupTest.java
 * @Package 
 * @Description: TODO(添加描述)
 * @author zhongzhenyang at gmail.com
 * @date 2013-9-11 上午10:10:20
 * @version V1.0
 */
public class JsoupTest {
	
	public static void main(String []args) throws IOException{
		String html = FileUtils.readFileToString(new File("original.html"));
//		String html = "<p>&nbsp;abc</p>";
		Document doc = Jsoup.parseBodyFragment(html);
		Element body = doc.body();
//		Elements figcaptions = body.getElementsByTag("figcaption");
//		for(Element figcaption:figcaptions){
//		System.out.println(figcaption.text());
//		}
		for(Element div:body.getElementsByTag("div")){
			System.out.println(div.attr("class"));
		}
		FileUtils.write(new File("out.html"),body.html());
		
		
		
	}

}

