package redis;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public class UserRepository implements DomainRepository<User> {

	@Autowired
	private RedisTemplate<String, User> redisTemplate;
	
	@PostConstruct
	public void init(){
	}

	public RedisTemplate<String, User> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, User> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void put(User user) {
		redisTemplate.opsForHash()
				.put(user.getObjectKey(), user.getKey(), user);
	}

	@Override
	public void delete(User key) {
		redisTemplate.opsForHash().delete(key.getObjectKey(), key.getKey());
	}

	@Override
	public User get(User key) {
		return (User) redisTemplate.opsForHash().get(key.getObjectKey(),
				key.getKey());
	}

	@Override
	public List<User> getObjects() {
		List<User> users = new ArrayList<User>();
		for (Object user : redisTemplate.opsForHash().values(User.OBJECT_KEY)) {
			users.add((User) user);
		}
		return users;
	}

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext-redis.xml");
		UserRepository userRepository = (UserRepository) applicationContext
				.getBean("userRepository");
		User user1 = new User("1", "user 1");
		User user2 = new User("2", "user 2");
		userRepository.put(user1);
		System.out.println(" Step 1 output : " + userRepository.getObjects());
		userRepository.put(user2);
		System.out.println(" Step 2 output : " + userRepository.getObjects());
		userRepository.delete(user1);
		System.out.println(" Step 3 output : " + userRepository.getObjects());
	}
}
