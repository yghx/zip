package decompressfile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry.Entry;

class Demo {
	static final int BUFFER = 2048;

	public static void main(String argv[]) {
		try {
			//BufferedOutputStream dest = null;
			//FileInputStream fis = new FileInputStream(argv[0]);
			//ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipFile zipFile = new ZipFile(new File("1.zip"),Charset.forName("GBK"));
			
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				File file = new File(entry.getName());
				// 如果是文件夹,就创建文件夹
				if (entry.isDirectory()) {
					file.mkdir();
					continue;
				}
				//创建文件
				file.createNewFile();
				//如果是文件,使用流写入
				System.out.println("Extracting: " + entry);
				int i;
				byte buff[] = new byte[BUFFER];
				// write the files to the disk
				FileOutputStream fos = new FileOutputStream(entry.getName());
				InputStream fis = zipFile.getInputStream(entry);
				while((i = fis.read(buff)) != -1) {
					fos.write(buff,0,i);
				}
				fos.flush();
				fis.close();
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}