package compressfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DemoCompress {
	public static void main(String[] args) throws IOException {
		String srcDirPath = "."+File.separator+"a";
		String outPath = srcDirPath+"1.zip";
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(new File(outPath)), Charset.forName("gbk"));
		toZip(srcDirPath, zipOut, true);
		//这三个方法都不执行,很有可能出现压缩文件有问题,说不定是必然出问题
		zipOut.flush();
		zipOut.closeEntry();
		zipOut.close();
	}
	
	public static void toZip(String srcDirPath, ZipOutputStream zipOut, boolean KeepStructure) {
		File srcFile = new File(srcDirPath);
		try {
			long startTime = System.currentTimeMillis();
			compressFile(srcFile, zipOut, KeepStructure);
			long endTime = System.currentTimeMillis();
			long totalTime = endTime-startTime;
			System.err.println(totalTime/1000.0 + "s");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设计上看,这个方法是不希望暴露出来的,所以私有的必要性体现了
	 * 
	 * @param srcFile
	 * @param zipOut
	 * @param keepStructure
	 * @throws IOException 
	 */
	private static void compressFile(File srcFile, ZipOutputStream zipOut, boolean keepStructure) throws IOException {
		if (srcFile.isFile()) {
			//向文件中添加一个zip实体,然后通过流来将数据copy到该zip实体中
			zipOut.putNextEntry(new ZipEntry(srcFile.getName()));
			InputStream is = new FileInputStream(srcFile);
			byte buff []  = new byte[1024];
			int i = 0;
			while((i = is.read(buff))!= -1) {
				zipOut.write(buff,0,i);
			}
			zipOut.flush();
			zipOut.closeEntry();//关闭实体
			is.close();
		}else {
			File[] listFiles = srcFile.listFiles();
			if((listFiles == null || listFiles.length == 0) && keepStructure) {
				zipOut.putNextEntry(new ZipEntry(srcFile.getName()+File.separator));
				zipOut.closeEntry();//关闭该实体
				return;
			}
			for (File file : listFiles) {
				compressFile(file, zipOut, keepStructure);
			}
		}
	}
	/**
	 * 将一批文件打包文件打包,就是遍历调用toZip方法
	 */
}
