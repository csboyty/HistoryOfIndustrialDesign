package com.zhongyi.hid.util;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Title: XmlEscapeOutputStream.java
 * @Package com.zhongyi.hid.util
 * @Description: TODO(添加描述)
 * @author zhongzhenyang at gmail.com
 * @date 2013-9-9 下午7:17:27
 * @version V1.0
 */
public class XmlEscapeOutputStream extends FilterOutputStream{
	

	
	public XmlEscapeOutputStream(OutputStream inner) {
		super(inner);
	}



	@Override
	public void write(int b) throws IOException {
		char c = (char)b;
		if('&' == c){
			for(char _c : "&amp;".toCharArray()){
				out.write(_c);
			}
		}
		else{
			out.write(b);
		}
	}
	
	
	public static void main(String[]args) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream os = new XmlEscapeOutputStream(baos);
		os.write("option=com_content&view=article&id=73&Itemid=好".getBytes());
		os.flush();
		System.out.println(new String(baos.toByteArray()));
	}
	

}

