package com.zhongyi.hid.service.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.StringUtils;

import com.zhongyi.hid.dto.HidBundle;
import com.zhongyi.hid.service.CmsDbRepository;
import com.zhongyi.hid.service.MakeBundleContext;
import com.zhongyi.hid.util.JsonUtil;

/**
 * 
 * @author zzy
 * 
 */
public class CreateBundleInfoCommand implements Command {

	private final CmsDbRepository cmsDbRepository;


	public CreateBundleInfoCommand(CmsDbRepository cmsDbRepository) {
		super();
        this.cmsDbRepository = cmsDbRepository;
	}
	
	@Override
	public boolean execute(Context context) throws Exception {
		MakeBundleContext makeBundleContext = (MakeBundleContext) context;
		HidBundle.BundleInfo bundleInfo = generateBundleInfo(makeBundleContext);
		makeBundleContext.setBundleInfo(bundleInfo);
		return Command.CONTINUE_PROCESSING;
	}

	

    private HidBundle.BundleInfo generateBundleInfo(MakeBundleContext context){
        HidBundle.BundleInfo bundleInfo = new HidBundle.BundleInfo();
        List<CreateDocumentEntryCommand.CmsGenre> genreList = cmsDbRepository.queryForGenre(context.getDocId());
        if(!genreList.isEmpty()){
        	ArrayList<String> genres = new ArrayList<String>();
        	for(CreateDocumentEntryCommand.CmsGenre _cmsGenre:genreList){
        		genres.add(_cmsGenre.getName());
        	}
        	bundleInfo.setGenre(genres);
        }
        List<String> artists = cmsDbRepository.queryForArtists(context.getDocId());
        if(! artists.isEmpty()){
        	 bundleInfo.setArtists(new ArrayList<String>(artists));
        }
        List<String> organizations = cmsDbRepository.queryForOrganizations(context.getDocId());
        if(! organizations.isEmpty()){
        	bundleInfo.setOrganizations(new ArrayList<String>(organizations));
        }
        String year = cmsDbRepository.queryForStartYear(context.getDocId());
        bundleInfo.setYear(year);
        bundleInfo.setSummary(context.getCmsPost().getDescription());
        Pair<String,String> cityInfo = cmsDbRepository.queryForCity(context.getDocId());
        if(cityInfo != null){
	        bundleInfo.setCity(cityInfo.getKey());
	        bundleInfo.setCoordinate(cityInfo.getValue());
        }
        bundleInfo.setPostDate(context.getCmsPost().getPostDate());
        String profileJson = cmsDbRepository.queryForProfile(context.getDocId());
        if(!StringUtils.isEmpty(profileJson)){
			Map<String,Object> profileMap = JsonUtil.getJsonObj(profileJson);
			String profileFilepath =(String) profileMap.get("filepath");
			bundleInfo.setProfile(profileFilepath);
        }
        
		String backgroundJson = cmsDbRepository.queryForBackground(context.getDocId());
		if(!StringUtils.isEmpty(backgroundJson)){
			Map<String,Object> backgroundMap = JsonUtil.getJsonObj(backgroundJson);
			String backgroundFilepath =(String) backgroundMap.get("filepath");
			bundleInfo.setBackground(backgroundFilepath);
		}
		
        return bundleInfo;
    }


}
