import java.io.File;
import java.io.FileFilter;
import java.util.Collections;

import org.apache.commons.codec.digest.DigestUtils;

import com.zhongyi.hid.util.ZipUtil;


public class RenameUtils {
	
	public static void main(String[]args) throws Exception{
		File root = new File("D:/hid/bundles");
		File[] bundles =root.listFiles(new FileFilter(){

			@Override
			public boolean accept(File file) {
				return file.isDirectory() ? true :false;
			}
			
		});
		int i=0;
		for(File bundle:bundles){
			File[] parts= bundle.listFiles();
			for(File part:parts){
				String filename = part.getName().toLowerCase();
				if(filename.endsWith(".xml")){
					part.delete();
				}else if(filename.endsWith(".png")|| filename.endsWith(".jpg") || filename.endsWith(".jpeg")){
					File bgFile = new File(part.getParentFile(),"bg"+filename.substring(filename.indexOf(".")));
					part.renameTo(bgFile);
				}
			}
			bundle.renameTo(new File(bundle.getParentFile(),DigestUtils.md5Hex(String.valueOf(i))));
			i++;
		}
		
		 bundles =root.listFiles(new FileFilter(){

				@Override
				public boolean accept(File file) {
					return file.isDirectory() ? true :false;
				}
				
			});
		 
		for(File bundle:bundles){
			String zipName = bundle.getName()+".zip";
			File zipFile = new File(root,zipName);
			ZipUtil.zip(bundle, zipFile,Collections.<FileFilter>emptyList());
		}
	}

}
