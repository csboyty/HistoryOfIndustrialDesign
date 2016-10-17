package guava;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.RateLimiter;

public class RateLimiterTest {

	public static void main(String[] args) {

		final RateLimiter rateLimiter = RateLimiter.create(0.1);
		final ExecutorService threadPoolExcutor = Executors
				.newFixedThreadPool(2);

		for (Runnable task : Lists.newArrayList(new MyTask(), new MyTask())) {
			System.out.println(((ThreadPoolExecutor)threadPoolExcutor).getActiveCount());
			rateLimiter.acquire(); // may wait
			System.out.println("here");
			threadPoolExcutor.execute(task);
		}
		System.out.println(((ThreadPoolExecutor)threadPoolExcutor).getCompletedTaskCount());
		System.out.println(((ThreadPoolExecutor)threadPoolExcutor).getTaskCount());

	}

	private static class MyTask implements Runnable {
		public void run() {
			System.out.println(Thread.currentThread().getId() + ":"
					+ new Date());
		}
	}

}
