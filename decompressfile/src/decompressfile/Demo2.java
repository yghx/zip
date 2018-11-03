package decompressfile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Demo2 {
	static final int BUFFER = 2048;

	public static void main(String argv[]) {
		try {
			BufferedOutputStream dest = null;
			FileInputStream fis = new FileInputStream("1.zip");
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis),Charset.forName("GBK"));
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				if(entry.isDirectory()) {
					new File(entry.getName()).mkdir();
					continue;
				}
				System.out.println("Extracting: " + entry);
				int count;
				byte data[] = new byte[BUFFER];
				// write the files to the disk
				FileOutputStream fos = new FileOutputStream(entry.getName());
				dest = new BufferedOutputStream(fos, BUFFER);
				//zis读取的都是当前实体数据
				while ((count = zis.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
			}
			zis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
