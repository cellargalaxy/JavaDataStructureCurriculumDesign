package huffman;

import java.io.*;

/**
 * Created by cellargalaxy on 2017/5/15.
 */
public class HuffmanEncodingOutputStream extends BufferedOutputStream {
	private HuffmanCoding huffmanCoding;
	private StringBuilder stringBuilder;
	
	/**
	 * @param out           压缩文件的输出流
	 * @param huffmanCoding 哈弗曼编码对象
	 * @throws IOException
	 */
	public HuffmanEncodingOutputStream(OutputStream out, HuffmanCoding huffmanCoding) throws IOException {
		super(out);
		this.huffmanCoding = huffmanCoding;
		stringBuilder = new StringBuilder();
		byte[] bytes = huffmanCoding.getCodingHead().toString().getBytes();
		byte[] bs = new byte[1024 * 4];
		for (int i = 0; i < bytes.length && i < bs.length; i++) {
			bs[i] = bytes[i];
		}
		super.write(bs, 0, bs.length);
	}
	
	@Override
	public synchronized void write(byte[] bs, int off, int len) throws IOException {
		for (int i = off; i < len; i++) {
			stringBuilder.append(huffmanCoding.byteToCoding(bs[i]));
		}
		len = stringBuilder.length() / 8;
		if (off + len > bs.length) {
			len = bs.length - off;
		}
		for (int i = off; i < off + len; i++) {
			bs[i] = (byte) Integer.parseInt(stringBuilder.substring(0, 8), 2);
			stringBuilder.delete(0, 8);
		}
		super.write(bs, off, len);
	}
	
	@Override
	public void close() throws IOException {
		while (stringBuilder.length() > 8) {
			byte[] bs = new byte[1024];
			int len = stringBuilder.length() / 8;
			if (len > bs.length) {
				len = bs.length;
			}
			for (int i = 0; i < len; i++) {
				bs[i] = (byte) Integer.parseInt(stringBuilder.substring(0, 8), 2);
				stringBuilder.delete(0, 8);
			}
			super.write(bs, 0, len);
		}
		
		byte over = 0;
		while (stringBuilder.length() < 8) {
			stringBuilder.append("0");
			over++;
		}
		byte b = (byte) Integer.parseInt(stringBuilder.substring(0, 8), 2);
		stringBuilder.delete(0, 8);
		super.write(b);
		super.write(over);
		super.close();
	}
}
