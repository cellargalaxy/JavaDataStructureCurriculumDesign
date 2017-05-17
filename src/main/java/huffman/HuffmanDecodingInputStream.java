package huffman;

import java.io.*;

/**
 * Created by cellargalaxy on 2017/5/15.
 */
public class HuffmanDecodingInputStream extends BufferedInputStream {
	private HuffmanCoding huffmanCoding;
	private StringBuilder stringBuilder;
	private byte over;
	
	
	protected HuffmanDecodingInputStream(InputStream in) throws IOException {
		super(in);
		stringBuilder = new StringBuilder();
		int result = 0;
		byte[] bytes = new byte[1024 * 4];
		while (result < 1024 * 4) {
			int i = super.read(bytes, result, bytes.length);
			if (i != -1) {
				result += i;
			} else {
				throw new IOException("哈夫曼压缩文件已损坏");
			}
		}
		String codingHead = new String(bytes);
		huffmanCoding = new HuffmanCoding(codingHead);

//		System.out.println("解压字典：");
//		huffmanCoding.printCoding();
//		System.out.println("解压树：");
//		huffmanCoding.printTree();
	}
	
	@Override
	public synchronized int read(byte[] bs, int off, int len) throws IOException {
		if (stringBuilder.length() > 32) {

//			System.out.println("再次编码：");
//			System.out.println(stringBuilder);
			
			int result = 0;
			while (stringBuilder.length() > 16 && result < len) {
				bs[off + result] = huffmanCoding.codingToByte(this);
				result++;
			}

//			System.out.println("再次剩余编码：");
//			System.out.println(stringBuilder);
			
			return result;
		}
		
		int result = super.read(bs, off, len);
		if (result != -1) over = bs[off + result - 1];
		if (result != -1) {
			for (int i = off; i < result; i++) {
				String s = Integer.toBinaryString(bs[i]);
				if (s.length() > 8) {
					s = s.substring(s.length() - 8);
				} else if (s.length() < 8) {
					while (s.length() < 8) {
						s = "0" + s;
					}
				}
				stringBuilder.append(s);
			}

//			System.out.println("编码：");
//			System.out.println(stringBuilder);
			
			result = 0;
			while (stringBuilder.length() > 16 && result < len) {
				bs[off + result] = huffmanCoding.codingToByte(this);
				result++;
			}

//			System.out.println("剩余编码：");
//			System.out.println(stringBuilder);
			
			return result;
		} else {
			if (stringBuilder.length() == 0) {
				return -1;
			} else {
				stringBuilder.delete(stringBuilder.length() - over - 8, stringBuilder.length());

//				System.out.println("删除剩余编码，超码：");
//				System.out.println(stringBuilder+"："+over);
				
				result = 0;
				while (stringBuilder.length() > 0 && result < len) {
					bs[off + result] = huffmanCoding.codingToByte(this);
					result++;
				}
				return result;
			}
		}
	}
	
	public String pollBit() {
		String s = stringBuilder.substring(0, 1);
		stringBuilder.delete(0, 1);
		return s;
	}
	
	public String getFileName() {
		return huffmanCoding.getFileNmae();
	}
}
