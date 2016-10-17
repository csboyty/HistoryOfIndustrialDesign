package com.zhongyi.hid.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtil {

	public static void zip(File srcFile, File dest, Collection<? extends FileFilter> filter)
			throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(dest));
		zip(out, srcFile, null, filter);
		out.close();
	}

	private static void zip(ZipOutputStream out, File srcFile, String base,
			Collection<? extends FileFilter> filter) throws Exception {
		if (!srcFile.exists()) {
			throw new IllegalStateException("srcFile is not exists");
		}
		if (srcFile.isDirectory()) {
			File[] files = srcFile.listFiles();
			base = base == null ? srcFile.getName() + "/" : base + "/";
			if (isAccept(srcFile, filter)) {
				out.putNextEntry(new ZipEntry(base));
			}
			for (int i = 0; i < files.length; i++) {
				zip(out, files[i], base + files[i].getName(), filter);
			}
		} else {
			if (isAccept(srcFile, filter)) {
				base = base.length() == 0 ? srcFile.getName() : base;
				ZipEntry zipEntry = new ZipEntry(base);

				out.putNextEntry(zipEntry);
				FileInputStream in = new FileInputStream(srcFile);
				int length = 0;
				byte[] b = new byte[1024];
				while ((length = in.read(b, 0, 1024)) != -1) {
					out.write(b, 0, length);
				}
				in.close();
			}
		}
	}

	private static boolean isAccept(File srcFile, Collection<? extends FileFilter> list) {
		boolean accept = true;
		if (list != null && list.size() > 0) {
			for (FileFilter filter : list) {
				accept = filter.accept(srcFile);
				if (!accept)
					break;
			}
		}
		return accept;
	}

	public static void unZip(String srcFile, String dest, boolean deleteFile)
			throws Exception {
		File file = new File(srcFile);
		if (!file.exists()) {
			throw new Exception("解压文件不存在!");
		}
		ZipFile zipFile = new ZipFile(file);
		Enumeration<ZipEntry> e = zipFile.getEntries();
		while (e.hasMoreElements()) {
			ZipEntry zipEntry = e.nextElement();
			if (zipEntry.isDirectory()) {
				String name = zipEntry.getName();
				name = name.substring(0, name.length() - 1);
				File f = new File(dest + name);
				f.mkdirs();
			} else {
				File f = new File(dest + zipEntry.getName());
				f.getParentFile().mkdirs();
				f.createNewFile();
				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(f);
				int length = 0;
				byte[] b = new byte[1024];
				while ((length = is.read(b, 0, 1024)) != -1) {
					fos.write(b, 0, length);
				}
				is.close();
				fos.close();
			}
		}

		if (zipFile != null) {
			zipFile.close();
		}

		if (deleteFile) {
			file.deleteOnExit();
		}
	}

	public static void main(String[] args) throws Exception {
		long begin = System.currentTimeMillis();
		List<FileFilter> filter = new ArrayList<FileFilter>();
		ZipUtil.zip(new File("D:/1"), new File("D:/1.zip"), filter);
		long end = System.currentTimeMillis();
		System.out.println(end - begin);
	}

}