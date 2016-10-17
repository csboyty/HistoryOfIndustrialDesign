package com.zhongyi.hid.service;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.chain.impl.ChainBase;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zhongyi.hid.service.commands.BgResizeCommand;
import com.zhongyi.hid.service.commands.CreateBundleInfoCommand;
import com.zhongyi.hid.service.commands.CreateDocumentEntryCommand;
import com.zhongyi.hid.service.commands.CreateHidBundleCommand;
import com.zhongyi.hid.service.commands.CreatePadMainHtmlCommand;
import com.zhongyi.hid.service.commands.LoadImageCommand;
import com.zhongyi.hid.service.commands.LoadPostUseJsoupCommand;
import com.zhongyi.hid.service.commands.MakeBundleCommand;
import com.zhongyi.hid.service.commands.ProfileResizeCommand;
import com.zhongyi.hid.util.AppLogType;

@Service
public class BundleManageService {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private ExecutorService bundleExecutor = null;

	@Autowired
	private CmsDbRepository cmsDbRepository;
	
	@Autowired
	private CmsRedisRepository cmsRedisRepository;
	
	@Autowired
	private ThymeleafHtmlEngine thymeleafHtmlEngine;

	private BundleDocChain bundleDocChain;
	
	private DefaultCompleteHandler completeHandler;

	@PostConstruct
	public void init() {

		ThreadFactory _threadFactory = new ThreadFactoryBuilder()
				.setNameFormat("bundle-pool-%d").setDaemon(true)
				.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread t, Throwable e) {
						logger.error("bundle thread occurs exception", e);
					}
				}).build();
		int coreNum = Runtime.getRuntime().availableProcessors();
		bundleExecutor = new ThreadPoolExecutor(coreNum, coreNum * 2, 60,
				TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
				_threadFactory);
		bundleDocChain = new BundleDocChain();
		completeHandler = new DefaultCompleteHandler(cmsDbRepository);

	}

	@PreDestroy
	public void destroy() throws InterruptedException {
		bundleExecutor.shutdown();
		while (!bundleExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
			logger.info("Awaiting completion of threads.");
		}
	}

	private class BundleDocChain extends ChainBase {

		private BundleDocChain() {
			super();
			addCommand(new LoadPostUseJsoupCommand(cmsDbRepository));
			addCommand(new LoadImageCommand());
			addCommand(new CreateBundleInfoCommand(cmsDbRepository));
			addCommand(new ProfileResizeCommand());
			addCommand(new BgResizeCommand());
			addCommand(new CreateDocumentEntryCommand(cmsDbRepository));
			addCommand(new CreatePadMainHtmlCommand(thymeleafHtmlEngine));
//			addCommand(new CreatePhoneMainHtmlCommand(thymeleafHtmlEngine));
			addCommand(new MakeBundleCommand());
			addCommand(new CreateHidBundleCommand(cmsRedisRepository));
		}

		private void bundleDoc(int docId,long createTimeInMills) throws InterruptedException,
				ExecutionException {
			 bundleExecutor.submit(new BundleDocTask(docId,createTimeInMills));
		}

	}

	private class BundleDocTask implements Callable<Boolean> {

		private final int docId;
		private final long createTimeInMills;

		private BundleDocTask(int docId,long createInMills) {
			super();
			this.docId = docId;
			this.createTimeInMills = createInMills;
		}

		@Override
		public Boolean call() throws Exception {
			MakeBundleContext bundleContext = MakeBundleContext.create(docId,createTimeInMills);
			boolean result = false;
			Exception ex = null;
			try{
				result= bundleDocChain.execute(bundleContext);
			}catch(Exception e){
				if(logger.isErrorEnabled()){
					String message = String.format("bundle fail,[docId=%d,createTimeInMills=%d]", docId,createTimeInMills);
					logger.error(message,e);
					cmsDbRepository.insertLog(AppLogType.TYPE_BUNDLE, "error", message, new Date(createTimeInMills));
				}
				result =false;
				ex = e;
			}
			if(result && ex ==null){
				completeHandler.onSuccess(docId);
			}else{
				completeHandler.onException(docId, ex);
			}
			return result;
		}

	}

	public void makeBundles(int[] docIdArray,long createTimeInMills) throws InterruptedException,
			ExecutionException {
		for (int i = 0; i < docIdArray.length; i++) {
			bundleDocChain.bundleDoc(docIdArray[i],createTimeInMills);
		}
	}
	
	public void removeBundles(String[] docIdArray,long removeTimeInMills) throws Exception {
		File bundleDir = new File(SystemContextListener.getSystemProperty().getBundleRoot());
		for(int i=0; i< docIdArray.length; i++){
			String docId =docIdArray[0];
			FileUtils.deleteQuietly(new File(bundleDir,docId+".zip"));
			cmsRedisRepository.removeBundle(docId, removeTimeInMills);
		}
	}

}
