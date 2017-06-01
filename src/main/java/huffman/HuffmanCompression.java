package huffman;

import java.io.*;

/**
 * Created by cellargalaxy on 2017/5/17.
 */
public class HuffmanCompression {
	
	public static void main(String[] args) throws IOException {
		File file1 = new File("g:/迅雷下载/blogData_train.csv");
		long t1 = System.currentTimeMillis();
		File file2 = compression(file1, new File("f:/"), null);
		long t2 = System.currentTimeMillis();
		File file3 = decompression(file2, new File("e:/"), "blogData_train.csv");
		long t3 = System.currentTimeMillis();
		System.out.println("压缩时间：" + (t2 - t1)/1000);
		System.out.println("解压时间：" + (t3 - t2)/1000);
	}
	
	public static File compression(File file, File saveFolder, String fileName) {
		if (!file.exists() || file.isDirectory()) {
			return null;
		}
		if (saveFolder != null && (saveFolder.isFile() || (!saveFolder.exists() && !saveFolder.mkdirs()))) {
			return null;
		}
		if (saveFolder == null) {
			saveFolder = file.getParentFile();
		}
		if (fileName == null) {
			fileName = file.getName().substring(0, file.getName().lastIndexOf('.')) + ".ha";
		}
		
		File newFile = new File(saveFolder.getAbsolutePath() + "/" + fileName);
		System.out.println("newFile:" + newFile.getAbsolutePath());
		
		
		BufferedInputStream inputStream = null;
		HuffmanEncodingOutputStream encodingOutputStream = null;
		try {
			
			HuffmanCountInputStream countInputStream = new HuffmanCountInputStream(new FileInputStream(file));
			
			
			long[] counts = countInputStream.getCounts();
			byte[] bs=new byte[1024];
			while (countInputStream.read(bs) != -1);
			HuffmanCoding huffmanCoding = new HuffmanCoding(counts, file.getName());
			
			inputStream = new BufferedInputStream(new FileInputStream(file));
			encodingOutputStream = new HuffmanEncodingOutputStream(new FileOutputStream(newFile), huffmanCoding);
			int len;
			byte[] bytes = new byte[1024 * 10];
			while ((len = inputStream.read(bytes, 0, bytes.length)) != -1) {
				encodingOutputStream.write(bytes, 0, len);
			}
			
			
			return newFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (inputStream!=null) {
					inputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (encodingOutputStream!=null) {
					encodingOutputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static File decompression(File file, File saveFolder, String fileName) {
		if (!file.exists() || file.isDirectory()) {
			return null;
		}
		if (saveFolder != null && (saveFolder.isFile() || (!saveFolder.exists() && !saveFolder.mkdirs()))) {
			return null;
		}
		if (saveFolder == null) {
			saveFolder = file.getParentFile();
		}
		
		int len;
		byte[] bytes = new byte[1024];
		HuffmanDecodingInputStream decodingInputStream = null;
		BufferedOutputStream outputStream = null;
		try {
			decodingInputStream = new HuffmanDecodingInputStream(new FileInputStream(file));
			File newFile = null;
			if (fileName == null) {
				newFile = new File(saveFolder.getAbsolutePath() + "/" + decodingInputStream.getFileName());
			} else {
				newFile = new File(saveFolder.getAbsolutePath() + "/" + fileName);
			}
			
			System.out.println("newFile:" + newFile.getAbsolutePath());
			
			outputStream = new BufferedOutputStream(new FileOutputStream(newFile));
			while ((len = decodingInputStream.read(bytes, 0, bytes.length)) != -1) {
				outputStream.write(bytes, 0, len);
			}
			return newFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (decodingInputStream!=null) {
					decodingInputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (outputStream!=null) {
					outputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
}
