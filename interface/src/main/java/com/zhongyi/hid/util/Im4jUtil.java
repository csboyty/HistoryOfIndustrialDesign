package com.zhongyi.hid.util;

import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessExecutor;
import org.im4java.process.ProcessStarter;
import org.im4java.process.ProcessTask;

import com.google.common.collect.Lists;

public class Im4jUtil {

	static final ProcessExecutor im4jExecutor = new ProcessExecutor();

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				im4jExecutor.shutdown();
			}
		});
		if(SystemUtils.IS_OS_WINDOWS){
			ProcessStarter.setGlobalSearchPath("D:\\Program Files\\ImageMagick-6.8.6-Q16");
		}
	}

	public static void resizeImage(Iterable<Pair<String, String>> pairs,Integer width, Integer height) throws Exception {
		try {
			List<ProcessTask> processTasks = Lists.newLinkedList();
			for (Pair<String, String> pair : pairs) {
				IMOperation op = new IMOperation();
				op.resize(width, height);
				op.addImage(pair.getKey());
				op.addImage(pair.getValue());
				ConvertCmd cmd = new ConvertCmd();
				ProcessTask pt = cmd.getProcessTask(op);
				processTasks.add(pt);
				im4jExecutor.execute(pt);
			}
			for(ProcessTask processTask:processTasks){
				processTask.get();
			}
		} catch (Exception ex) {
			throw ex;
		}
	}

}
