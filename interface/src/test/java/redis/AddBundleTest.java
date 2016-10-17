package redis;

import java.util.Map;

import junit.framework.TestCase;

import net.sf.cglib.beans.BeanMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import com.zhongyi.hid.dto.BundleList.HidBundle;
import com.zhongyi.hid.util.RedisKeyUtil;

public class AddBundleTest extends TestCase {

	private JedisPool pool ;
	private Jedis jedis;

	public void setUp() {
		pool = new JedisPool(new JedisPoolConfig(),
					"192.168.2.148");
		jedis = pool.getResource();
	}

	public void testAddBundle() throws Exception{
		for(int i=0;i<100;i++){
			HidBundle bundle = new HidBundle();
			bundle.setId(String.valueOf(i));
			bundle.setName("name-"+i);
			bundle.setSize(RandomStringUtils.randomNumeric(8));
			bundle.setTimestamp(String.valueOf(System.currentTimeMillis()));
			bundle.setMd5(DigestUtils.md5Hex(String.valueOf(System.nanoTime())));
			bundle.setUrl("http://localhost:8080/hid/bundle/"+i+".zip");
			
			String bundleUid = RedisKeyUtil.bundle(bundle.getId());
			Map<String, String> bundleMap = BeanMap.create(bundle);
			jedis.watch(bundleUid);
			Transaction tx = jedis.multi();
			tx.zadd(RedisKeyUtil.bundles(),
					Double.parseDouble(bundle.getTimestamp()), bundleUid);
			tx.hmset(bundleUid, bundleMap);
			tx.exec();
			Thread.sleep(1000L);
		}
	}

	public void tearDown() {
		pool.returnResource(jedis);

	}

}
