package com.zhongyi.hid.util;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.nio.CharBuffer;

public class WordPressImageCaptionEscapeReader extends Reader {

	protected PushbackReader pushbackReader = null;
	protected String tokenValue = null;
	protected int tokenValueIndex = 0;

	public WordPressImageCaptionEscapeReader(Reader source) {
		this.pushbackReader = new PushbackReader(source, 2);
	}

	public int read(CharBuffer target) throws IOException {
		throw new RuntimeException("Operation Not Supported");
	}

	public int read() throws IOException {
		if (this.tokenValue != null) {
			if (this.tokenValueIndex < this.tokenValue.length()) {
				return this.tokenValue.charAt(this.tokenValueIndex++);
			}
			if (this.tokenValueIndex == this.tokenValue.length()) {
				this.tokenValue = null;
				this.tokenValueIndex = 0;
			}
		}
		int data = this.pushbackReader.read();
		if (data != '[')
			return data;

		data = this.pushbackReader.read();
		if (!(data == 'c' || data == '/')) {
			this.pushbackReader.unread(data);
			return '[';
		}else{
			StringBuilder tokenNameBuffer = new StringBuilder();
			char sential = (char)data;
			if(sential == 'c'){
				tokenNameBuffer.append("<fig");
			}else{
				tokenNameBuffer.append("<");
			}
			tokenNameBuffer.append((char)data);
			if(sential == '/'){
				tokenNameBuffer.append("fig");
			}
			
			
			data = this.pushbackReader.read();
			while (data != ']') {
				tokenNameBuffer.append((char) data);
				data = this.pushbackReader.read();
			}
			tokenNameBuffer.append(">");

			this.tokenValue = tokenNameBuffer.toString();
			return this.tokenValue.charAt(this.tokenValueIndex++);
		}
		
		

	}

	public int read(char cbuf[]) throws IOException {
		return read(cbuf, 0, cbuf.length);
	}

	public int read(char cbuf[], int off, int len) throws IOException {
		int charsRead = 0;
		for (int i = 0; i < len; i++) {
			int nextChar = read();
			if (nextChar == -1) {
				if (charsRead == 0) {
					charsRead = -1;
				}
				break;
			}
			charsRead = i + 1;
			cbuf[off + i] = (char) nextChar;
		}
		return charsRead;
	}

	public void close() throws IOException {
		this.pushbackReader.close();
	}

	public long skip(long n) throws IOException {
		throw new RuntimeException("Operation Not Supported");
	}

	public boolean ready() throws IOException {
		return this.pushbackReader.ready();
	}

	public boolean markSupported() {
		return false;
	}

	public void mark(int readAheadLimit) throws IOException {
		throw new RuntimeException("Operation Not Supported");
	}

	public void reset() throws IOException {
		throw new RuntimeException("Operation Not Supported");
	}

//	public static void main(String[]args) throws IOException{
//		Reader r = new WordPressImageCaptionEscapeReader(new InputStreamReader(new FileInputStream("D:/test_caption.txt"),"UTF-8"));
//		System.out.println(IOUtils.toString(r));
//	}
}
