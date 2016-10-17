package com.zhongyi.hid.service;

import java.io.File;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.zhongyi.hid.util.FileUtil;

@Component
public class StaleFileSweep {

	private static final long one_day_in_mills = 24 * 60 * 60 * 1000L;
	ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	
	@PostConstruct
	public  void init() {
		SweepTask task = new SweepTask(FileUtil.appTempDir());
		scheduledExecutorService.scheduleWithFixedDelay(task, 0, 10, TimeUnit.MINUTES);
	}
	
	@PreDestroy
	public void destroy(){
		scheduledExecutorService.shutdownNow();
	}

	private static class SweepTask implements Runnable {
		private final File dir;

		public SweepTask(File dir) {
			this.dir = dir;
		}

		public void run() {
			long cutoff = new Date(System.currentTimeMillis()
					- one_day_in_mills).getTime();
			
			File[] files = dir.listFiles();
			for(File file:files){
				if(file.lastModified() < cutoff){
					FileUtils.deleteQuietly(file);
				}
			}
			
		}
	}

	

}
