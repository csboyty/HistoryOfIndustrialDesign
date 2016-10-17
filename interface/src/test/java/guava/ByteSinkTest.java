package guava;

import java.io.File;
import java.io.IOException;

import com.google.common.io.ByteSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;

public class ByteSinkTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File file =new File("D:/python_le.txt");
		ByteSink sink=Files.asByteSink(file, FileWriteMode.APPEND);
		sink.write(Bytes.concat(Shorts.toByteArray((short)1),Shorts.toByteArray((short)2),Longs.toByteArray(3l)));
		
//		sink.asCharSink(Charset.forName("UTF-8")).write("Hello");
//		HashCode hashCode=Files.hash(file, Hashing.md5());
//		System.out.println(hashCode.toString());
	}

}
