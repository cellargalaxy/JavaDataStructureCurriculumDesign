package huffman;

import java.io.*;

/**
 * Created by cellargalaxy on 2017/5/17.
 */
public class HuffmanCompression {
	
	public static void main(String[] args) throws IOException {
		File file1 = new File("g:/罗小黑战记01.mp4");
		long t1 = System.currentTimeMillis();
		File file2 = compression(file1, new File("f:/"), null);
		long t2 = System.currentTimeMillis();
		File file3 = decompression(file2, new File("e:/"), "罗小黑战记.mp4");
		long t3 = System.currentTimeMillis();
//		System.out.println("压缩时间：" + (t2 - t1));
		System.out.println("解压时间：" + (t3 - t2));
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
			
			long t1=System.currentTimeMillis();
			HuffmanCountInputStream countInputStream = new HuffmanCountInputStream(new FileInputStream(file));
			long t2=System.currentTimeMillis();
			System.out.println("统计时间："+(t2-t1));
			
			long t3=System.currentTimeMillis();
			
			long[] counts = countInputStream.getCounts();
			HuffmanCoding huffmanCoding = new HuffmanCoding(counts, file.getName());
			
			inputStream = new BufferedInputStream(new FileInputStream(file));
			encodingOutputStream = new HuffmanEncodingOutputStream(new FileOutputStream(newFile), huffmanCoding);
			int len;
			byte[] bytes = new byte[1024];
			while ((len = inputStream.read(bytes, 0, bytes.length)) != -1) {
				encodingOutputStream.write(bytes, 0, len);
			}
			
			long t4=System.currentTimeMillis();
			System.out.println("压缩时间："+(t4-t3));
			
			return newFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				encodingOutputStream.close();
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
			File newFile=null;
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
				decodingInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
}
