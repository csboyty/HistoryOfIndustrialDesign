package com.zhongyi.hid.service;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.chain.impl.ContextBase;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;
import com.zhongyi.hid.dto.HidBundle;
import com.zhongyi.hid.util.FileUtil;

@SuppressWarnings("serial")
public class MakeBundleContext extends ContextBase {
	
	

	public static final String DOC_DIR_NAME = "doc/";
	
	public static final String PUBLIC_DIR_NAME = "doc/public/";

	public static final String POST_CONTENT_FILENAME = DOC_DIR_NAME
			+ "postContent.tmp";

	public static final String POST_CONTENT_REV_FILENAME = DOC_DIR_NAME
			+ "postContent.rev.tmp";
	
	public static final String MAIN_PAD_HTML_FILENAME = DOC_DIR_NAME + "main.pad.html";
	
	public static final String MAIN_PHONE_HTML_FILENAME= DOC_DIR_NAME + "main.phone.html";

	public static final String IMAGES_DIR_NAME = DOC_DIR_NAME + "images/";
	
	public static final String ASSET_DIR_NAME = DOC_DIR_NAME +"asset/";

	
	private final int docId;
	
	private final long createTimeInMills;
	
	private HidBundle.BundleInfo bundleInfo;

	private final File tempBundleDir;
	
	private final File docXmlFile;
	
	private String name;

    private CmsPost cmsPost;
	
//	private File profileFile;
	
//	private File backgroundFile;

	private File bundleFile;
	
	private File publicDir;

	private final Collection<String> images = Sets.newHashSet();
	
	private final String docIdAsMd5;
	
	private List<CmsSlide> slides ;
	
	private Map<String,String> image2MediaIdMap;

	private MakeBundleContext(int docId,long createTimeInMills) {
		this.docId = docId;
		this.createTimeInMills = createTimeInMills;
		this.docIdAsMd5 = DigestUtils.md5Hex(String.valueOf(docId));
		this.tempBundleDir = FileUtil.tempBundleDir(docIdAsMd5);
		this.docXmlFile = new File(tempBundleDir, "doc.xml");
		File docDir = new File(tempBundleDir, DOC_DIR_NAME);
		docDir.mkdir();
		publicDir = new File(tempBundleDir,PUBLIC_DIR_NAME);
		publicDir.mkdir();
		File imagesDir = new File(docDir, IMAGES_DIR_NAME);
		imagesDir.mkdir();
	}

	public int getDocId() {
		return docId;
	}
	
	public long getCreateTimeInMills() {
		return createTimeInMills;
	}

	public File getTempBundleDir() {
		return tempBundleDir;
	}

	public Collection<String> getImages() {
		return images;
	}

	public static MakeBundleContext create(int docId,long createTimeInMills) {
		return new MakeBundleContext(docId,createTimeInMills);
	}

	public File getDocXmlFile() {
		return docXmlFile;
	}
	
//	public File getProfileFile() {
//		return profileFile;
//	}


//	public void setProfileFile(File profileFile) {
//		this.profileFile = profileFile;
//	}

	public File getBundleFile() {
		return bundleFile;
	}

	public void setBundleFile(File bundleFile) {
		this.bundleFile = bundleFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public CmsPost getCmsPost() {
        return cmsPost;
    }

    public void setCmsPost(CmsPost cmsPost) {
        this.cmsPost = cmsPost;
    }
    
    public File getPublicDir() {
		return publicDir;
	}
    

//	public File getBackgroundFile() {
//		return backgroundFile;
//	}
//
//	public void setBackgroundFile(File backgroundFile) {
//		this.backgroundFile = backgroundFile;
//	}


	public void setPublicDir(File publicDir) {
		this.publicDir = publicDir;
	}


	public HidBundle.BundleInfo getBundleInfo() {
		return bundleInfo;
	}

	public void setBundleInfo(HidBundle.BundleInfo bundleInfo) {
		this.bundleInfo = bundleInfo;
	}
	

	public String getDocIdAsMd5() {
		return docIdAsMd5;
	}

	public List<CmsSlide> getSlides() {
		return slides;
	}

	public void setSlides(List<CmsSlide> slides) {
		this.slides = slides;
	}

	public Map<String, String> getImage2MediaIdMap() {
		return image2MediaIdMap;
	}

	public void setImage2MediaIdMap(Map<String, String> image2MediaIdMap) {
		this.image2MediaIdMap = image2MediaIdMap;
	}

	public static class CmsPost {
		private String title;
		private String author;
		private String postDate;
		private String description;
		private String postType;
		private final File contentTmpFile;

		private CmsPost(File contentTmpFile) {
			this.contentTmpFile = contentTmpFile;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getPostDate() {
			return postDate;
		}

		public void setPostDate(String postDate) {
			this.postDate = postDate;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
		

		public String getPostType() {
			return postType;
		}

		public void setPostType(String postType) {
			this.postType = postType;
		}

		public File getContentTmpFile() {
			return contentTmpFile;
		}

		public static CmsPost create(File contentTmpFile) {
			return new CmsPost(contentTmpFile);
		}

	}
	
	public static class CmsSlide{
		private final int docId;
		private final String linkClass;
		private final String linkHref;
		private final String myMediaId;
		private final String src;
		private String imageTitle;
		private String imageMemo;
		
		
		public CmsSlide(int docId,String linkClass,String linkHref,String myMediaId, String src) {
			super();
			this.docId = docId;
			this.linkClass = StringUtils.isEmpty(linkClass) ?"no_attch":linkClass;
			this.linkHref = linkHref;
			this.myMediaId = myMediaId;
			this.src = src;
		}
		
		public int getDocId() {
			return docId;
		}


		public String getImageTitle() {
			return imageTitle;
		}
		public void setImageTitle(String imageTitle) {
			this.imageTitle = StringEscapeUtils.escapeXml(imageTitle);
		}
		public String getImageMemo() {
			return imageMemo;
		}
		public void setImageMemo(String imageMemo) {
			this.imageMemo = StringEscapeUtils.escapeXml(imageMemo);
		}
		public String getMyMediaId() {
			return myMediaId;
		}
		public String getSrc() {
			return src;
		}
		
		public String getLinkClass() {
			return linkClass;
		}
		public String getLinkHref() {
			return linkHref;
		}
		
		
		@Override
		public String toString() {
			return "'"+myMediaId+"'";
		}
		
		
	}
	

}
