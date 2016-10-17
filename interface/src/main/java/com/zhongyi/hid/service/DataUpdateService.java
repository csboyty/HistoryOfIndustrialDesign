package com.zhongyi.hid.service;

import java.io.Reader;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhongyi.hid.util.AppLogType;

@Service
public class DataUpdateService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Autowired
	private CmsRedisRepository bundleRedisRepository;
	
	@Autowired
	private CmsDbRepository cmsDbRepository;
	
	
	
	public Reader retriveDataUpdate(String udid,long fromTimeInMills,long toTimeInMills,String outputType) throws IllegalStateException{
		try{
			return  bundleRedisRepository.retriveBundles(fromTimeInMills,toTimeInMills,outputType);
		}catch(Exception ex){
			if(logger.isErrorEnabled()){
				String message = String.format("retrive data update failed,[udid=%s,fromTimeInMills=%d,toTimeInMills=%d]",udid,fromTimeInMills,toTimeInMills);
//				logger.error(message,ex);
				cmsDbRepository.insertLog(AppLogType.TYPE_DATA_UPDATE, "error", message, new Date());
			}
			throw new IllegalStateException(ex);
		}
	}

}
