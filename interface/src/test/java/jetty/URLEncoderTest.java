package jetty;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class URLEncoderTest 
{
    public static void main( String[] args ) throws UnsupportedEncodingException
    {
        System.out.println( "Hello World!" );
        System.out.println(Arrays.toString("好".getBytes("GBK")));
        System.out.println(URLEncoder.encode("好+=abc","GBK"));
        System.out.println(URLDecoder.decode("%BA%C3%2B%3Dabc", "GBK"));
    }
}
