package com.zhongyi.hid.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {

	private static final Logger LOG = Logger.getLogger(JsonUtil.class);

	private static final ObjectMapper mapper = new ObjectMapper();
	
	private static final JsonFactory jsonFactory = new JsonFactory();
	

	private JsonUtil() {

	}

	/**
	 * @param <T>
	 *            泛型声明
	 * @param bean
	 *            类的实例
	 * @return JSON字符串
	 */
	public static <T> String toJson(T bean) {
		StringWriter sw = new StringWriter();
		try {
			JsonGenerator gen = jsonFactory.createJsonGenerator(sw);
			mapper.writeValue(gen, bean);
//			mapper.writerWithDefaultPrettyPrinter().writeValue(gen,bean);
			gen.close();
			return sw.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static <T> void toJson(T bean,Writer writer){
		
		try{
			JsonGenerator gen = jsonFactory.createJsonGenerator(writer);
			mapper.writeValue(gen, bean);
			gen.close();
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * @param <T>
	 *            泛型声明
	 * @param json
	 *            JSON字符串
	 * @param clzz
	 *            要转换对象的类型
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> clzz) {

		try {
			return  mapper.readValue(json, clzz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param json
	 *            JSON字符串,请保持key是加了双引号的
	 * @return Map对象,默认为HashMap
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getJsonObj(String json) {
		try {
			return mapper.readValue(json, Map.class);
		}  catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Object> parseArray(String jsonString) {
		List<Object> jsonList = null;

		try {
			jsonList = mapper.readValue(jsonString, List.class);
		} catch (Exception e) {
			LOG.error("Parse json to array failed - " + jsonString);
			throw new RuntimeException(e);
			
		}

		return jsonList;
	}
	
	public static List<Object> parseArray(InputStream in){
		return parseArray(new InputStreamReader(in,Charset.forName("UTF-8")));
	}
	
	
	public static List<Object> parseArray(Reader reader){
		try {
			return mapper.readValue(reader, List.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	public static <T> List<T> parseArray(String jsonString, Class<?> clazz) {
		try {
			return mapper.readValue(jsonString, mapper.getTypeFactory()
					.constructCollectionType(List.class, clazz));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
