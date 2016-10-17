package com.zhongyi.hid.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import com.zhongyi.hid.dto.HidBundle;
import com.zhongyi.hid.util.FileUtil;
import com.zhongyi.hid.util.JsonUtil;
import com.zhongyi.hid.util.RedisKeyUtil;
import com.zhongyi.hid.util.XmlUtil;

@Repository
public class CmsRedisRepository {

	@Autowired
	private JedisPool jedisPool;

	@SuppressWarnings("unchecked")
	public void addBundle(HidBundle bundle) throws Exception {

		Jedis jedis = null;
		boolean borrowOrOprSuccess = true;
		String bundleUid = RedisKeyUtil.bundle(bundle.getId());
		Map<String, Object> bundleMap = fromBundle(bundle);
		String bundleJson = JsonUtil.toJson(bundleMap);
		double score =Double.parseDouble(bundle.getTimestamp());
		try {
			jedis = jedisPool.getResource();
			Transaction tx = jedis.multi();
			tx.zadd(RedisKeyUtil.bundles(), score, bundleUid);
			tx.hset(RedisKeyUtil.bundleMap(), bundleUid, bundleJson);
			tx.exec();
		} catch (JedisConnectionException e) {
			borrowOrOprSuccess = false;
			if (jedis != null)
				jedisPool.returnBrokenResource(jedis);
			throw e;
		} finally {
			if (borrowOrOprSuccess)
				jedisPool.returnResource(jedis);
		}

	}
	
	private Map<String,Object> fromBundle(HidBundle bundle){
		SystemContextListener.getSystemProperty().getBundleRootURL();
		Map<String,Object> map = Maps.newLinkedHashMap();
		map.put("id", DigestUtils.md5Hex(bundle.getId()));
		map.put("name", bundle.getName());
		map.put("size", bundle.getSize());
		map.put("url", SystemContextListener.getSystemProperty().getBundleRootURL()+"/"+bundle.getUrl());
		map.put("timestamp", bundle.getTimestamp());
		map.put("md5", bundle.getMd5());
		map.put("op", bundle.getOp());
		map.put("info", bundle.getInfo());
		return map;
	}
	
	

	public void removeBundle(String bundleId,long removeTimeInMills) {
		Jedis jedis = null;
		boolean borrowOrOprSuccess = true;
		String bundleUid = RedisKeyUtil.bundle(bundleId);
		
		double score = 	new Long(removeTimeInMills).doubleValue();
		
		try {
			jedis = jedisPool.getResource();
			String bundleJson=jedis.hget(RedisKeyUtil.bundleMap(), bundleUid);
			if(StringUtils.isEmpty(bundleJson))
				return ;
			
			Map<String,Object> bundleMap = JsonUtil.getJsonObj(bundleJson);
			String name = (String) bundleMap.get("name");
			Map<String,String> updatedMap = new HashMap<String,String>();
			updatedMap.put("id",(String) bundleMap.get("id"));
			updatedMap.put("name", name);
			updatedMap.put("op", "d");
			updatedMap.put("timestamp", String.valueOf(removeTimeInMills));
			String updateJson = JsonUtil.toJson(updatedMap);
			Transaction tx = jedis.multi();
			tx.zadd(RedisKeyUtil.bundles(), score, bundleUid);
			tx.hset(RedisKeyUtil.bundleMap(), bundleUid, updateJson);
			tx.exec();
		} catch (JedisConnectionException e) {
			borrowOrOprSuccess = false;
			if (jedis != null)
				jedisPool.returnBrokenResource(jedis);
			throw e;
		} finally {
			if (borrowOrOprSuccess)
				jedisPool.returnResource(jedis);
		}
	}

	@SuppressWarnings("deprecation")
	public Reader retriveBundles(long fromTimeInMills,long toTimeInMills,String outputType) throws Exception {
		Jedis jedis = null;
		boolean borrowOrOprSuccess = true;
		String bundlesKey =RedisKeyUtil.bundles();
		try {
			jedis = jedisPool.getResource();
			Set<String> bundleUidSet = jedis.zrangeByScore(bundlesKey, "("+fromTimeInMills, String.valueOf(toTimeInMills));
			if (bundleUidSet == null || bundleUidSet.size() == 0) {
				return null;
			} else {
				Writer writer = null;
				File tempFile = null;
				try{
					if(bundleUidSet.size() <= 70){
						writer = new StringWriter();
					}else{
						tempFile = new File(FileUtil.appTempDir(),String.valueOf(System.currentTimeMillis()));
						writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile),"UTF-8"));
					}
					if("json".equals(outputType))
						writer.append("[");
					else
						writer.append("<file_list>\n");
					List<String> bundleJsonList = jedis.hmget(RedisKeyUtil.bundleMap(), bundleUidSet.toArray(new String[bundleUidSet.size()]));
					Iterator<String> bundleJsonIt = bundleJsonList.iterator();
					String bundleJson = bundleJsonIt.next();
					
					if("json".equals(outputType)){
						writer.write(bundleJson);
					}else{
						Map<String, Object> bundleMap = (Map<String,Object>)JsonUtil.getJsonObj(bundleJson);
						HidBundle bundle = toBundle(bundleMap);
						XmlUtil.toXml(bundle, writer);
					}
					
					for (;bundleJsonIt.hasNext();) {
						bundleJson = bundleJsonIt.next();
						if("json".equals(outputType)){
							writer.append(",");
							writer.write(bundleJson);
						}else{
							Map<String, Object> bundleMap = (Map<String,Object>)JsonUtil.getJsonObj(bundleJson);
							HidBundle bundle = toBundle(bundleMap);
							XmlUtil.toXml(bundle, writer);
						}
					}
					if(("json".equals(outputType)))
						writer.append("]");
					else
						writer.append("</file_list>");
					
					
				}finally{
					Closeables.closeQuietly(writer);
				}
				if(tempFile == null){
					return new StringReader(((StringWriter) writer).toString());
				}else{
					return new InputStreamReader(new FileInputStream(tempFile),"utf-8");
				}
			}
			
		} catch (JedisConnectionException e) {
			borrowOrOprSuccess = false;
			if (jedis != null)
				jedisPool.returnBrokenResource(jedis);
			throw e;
		} finally {
			if (borrowOrOprSuccess)
				jedisPool.returnResource(jedis);
		}
	}
	
	@SuppressWarnings("unchecked")
	private HidBundle toBundle(Map<String,Object> bundleMap){
		String op = (String)bundleMap.get("op");
		HidBundle bundle = new HidBundle();
		bundle.setId((String)bundleMap.get("id"));
		bundle.setName((String)bundleMap.get("name"));
		bundle.setOp(op);
		if("u".equals(op)){
			bundle.setSize((String)bundleMap.get("size"));
			bundle.setUrl((String)bundleMap.get("url"));
			bundle.setMd5((String)bundleMap.get("md5"));
			bundle.setInfo(toBundleInfo((Map<String,Object>)bundleMap.get("info")));
		}
		bundle.setTimestamp((String)bundleMap.get("timestamp"));
		return bundle;
	}
	
	
	@SuppressWarnings("unchecked")
	private HidBundle.BundleInfo toBundleInfo(Map<String,Object> bundleInfoMap){
		HidBundle.BundleInfo bundleInfo = new HidBundle.BundleInfo();
		
		bundleInfo.setGenre(bundleInfoMap.get("genre") !=null ?(Lists.newArrayList((Iterable<String>)bundleInfoMap.get("genre"))):null);
		bundleInfo.setArtists(bundleInfoMap.get("artists")!=null ?Lists.newArrayList((Iterable<String>)bundleInfoMap.get("artists")):null);
		bundleInfo.setOrganizations(bundleInfoMap.get("organizations") !=null ? Lists.newArrayList((Iterable<String>)bundleInfoMap.get("organizations")):null);
		bundleInfo.setYear((String)bundleInfoMap.get("year"));
		bundleInfo.setCity((String)bundleInfoMap.get("city"));
		bundleInfo.setCoordinate((String)bundleInfoMap.get("coordinate"));
		bundleInfo.setSummary((String)bundleInfoMap.get("summary"));
		bundleInfo.setPostDate((String)bundleInfoMap.get("postDate"));
		bundleInfo.setProfile((String)bundleInfoMap.get("profile"));
		bundleInfo.setBackground((String)bundleInfoMap.get("background"));
		return bundleInfo;
	}
}
