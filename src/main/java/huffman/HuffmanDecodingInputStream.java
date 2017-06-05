package huffman;

import java.io.*;

/**
 * Created by cellargalaxy on 2017/5/15.
 */
public class HuffmanDecodingInputStream extends BufferedInputStream {
	private HuffmanCoding huffmanCoding;
	private StringBuilder stringBuilder;
	private byte over;
	
	
	public HuffmanDecodingInputStream(InputStream in) throws IOException {
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
	}
	
	@Override
	public synchronized int read(byte[] bs, int off, int len) throws IOException {
		if (stringBuilder.length() > 32) {
			int result = 0;
			while (stringBuilder.length() > 16 && result < len) {
				bs[off + result] = huffmanCoding.codingToByte(this);
				result++;
			}
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
			result = 0;
			while (stringBuilder.length() > 16 && result < len) {
				bs[off + result] = huffmanCoding.codingToByte(this);
				result++;
			}
			return result;
		} else {
			if (stringBuilder.length() == 0) {
				return -1;
			} else {
				stringBuilder.delete(stringBuilder.length() - over - 8, stringBuilder.length());
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
		return huffmanCoding.getFileName();
	}
	
	public HuffmanCoding getHuffmanCoding() {
		return huffmanCoding;
	}
	
	public void setHuffmanCoding(HuffmanCoding huffmanCoding) {
		this.huffmanCoding = huffmanCoding;
	}
	
	public StringBuilder getStringBuilder() {
		return stringBuilder;
	}
	
	public void setStringBuilder(StringBuilder stringBuilder) {
		this.stringBuilder = stringBuilder;
	}
	
	public byte getOver() {
		return over;
	}
	
	public void setOver(byte over) {
		this.over = over;
	}
}
