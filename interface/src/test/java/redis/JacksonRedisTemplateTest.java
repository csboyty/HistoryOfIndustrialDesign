package redis;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.RedisMap;

public class JacksonRedisTemplateTest {

	private final RedisTemplate<String,String> redisTemplate;

	 private final HashMapper<FooRedisObject,String,String> fooMapper =  new DecoratingStringHashMapper<FooRedisObject>(
				new JacksonHashMapper<FooRedisObject>(FooRedisObject.class));

	public JacksonRedisTemplateTest(RedisTemplate<String,String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void addFoo(FooRedisObject foo) {
		Map<String,String> fooMap=fooMapper.toHash(foo);
		RedisMap<String,String> redisMap=new DefaultRedisMap<String, String>(foo.uid, redisTemplate);
		redisMap.putAll(fooMap);
	}
	
	public void getFoo(String uid){
		Map fooMap=redisTemplate.boundHashOps(uid).entries();
		FooRedisObject fooObject=fooMapper.fromHash(fooMap);
		System.out.println(fooObject);
		
	}
	
	public void testMulti(){
		redisTemplate.multi();
		redisTemplate.boundValueOps("somevkey").increment(1);
		redisTemplate.boundZSetOps("somezkey").add("zvalue", System.currentTimeMillis());
		redisTemplate.exec();
	}
	
	public void testMulti2(){
		Object result=redisTemplate.execute(new SessionCallback(){

			@Override
			public Object execute(RedisOperations operations)
					throws DataAccessException {
				
				operations.watch("somevkey");
				operations.boundValueOps("somevkey").increment(1);
				operations.boundZSetOps("somezkey").add("zvalue", System.currentTimeMillis());
				return operations.exec();
			}
		});
		System.out.println(result);
	}
	
	
	public static void main(String[]args){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext-redis.xml");
		RedisTemplate redisTemplate = (RedisTemplate) applicationContext.getBean("redisTemplate");
		JacksonRedisTemplateTest test  = new JacksonRedisTemplateTest(redisTemplate);
//		test.addFoo(new FooRedisObject("foo1",String.valueOf(System.currentTimeMillis()),"bar1"));
//		test.getFoo("foo1");
		test.testMulti2();
	}

	public static class FooRedisObject {
		private String uid;
		private String timestamp;
		private String value;

		public FooRedisObject(String uid, String timestamp, String value) {
			super();
			this.uid = uid;
			this.timestamp = timestamp;
			this.value = value;
		}

		public FooRedisObject() {
			super();
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		
		public static String OBJECT_KEY(){
			return "foo_redis";
		}

		@Override
		public String toString() {
			return "FooRedisObject [uid=" + uid + ", timestamp=" + timestamp
					+ ", value=" + value + "]";
		}
		
		

	}

}
