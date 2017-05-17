package huffman;

import java.io.*;
import java.util.Arrays;

/**
 * Created by cellargalaxy on 2017/5/15.
 */
public class HuffmanEncodingOutputStream extends BufferedOutputStream{
	private HuffmanCoding huffmanCoding;
	private StringBuilder stringBuilder;
	
	
	
	public HuffmanEncodingOutputStream(OutputStream out, HuffmanCoding huffmanCoding) throws IOException {
		super(out);
		this.huffmanCoding = huffmanCoding;
		stringBuilder=new StringBuilder();
		byte[] bytes=huffmanCoding.getCodingHead().toString().getBytes();
		super.write(bytes,0,bytes.length);
	}
	
	@Override
	public synchronized void write(byte[] bs, int off, int len) throws IOException {
		for (int i = off; i < len; i++) {
			stringBuilder.append(huffmanCoding.byteToCoding(bs[i]));
		}
//		System.out.println("生成编码：");
//		System.out.println(stringBuilder);
		off=0;
		len=stringBuilder.length()/8;
		if (len>=bs.length) {
			len=bs.length;
		}
		for (int i = off; i < len; i++) {
			bs[i]=(byte)Integer.parseInt(stringBuilder.substring(0,8), 2);
			stringBuilder.delete(0,8);
		}
//		System.out.println("剩余编码：");
//		System.out.println(stringBuilder);
		super.write(bs, off, len);
	}
	
	@Override
	public void close() throws IOException {
		while (stringBuilder.length() != 8) {
			stringBuilder.append("0");
		}
//		System.out.println("最终编码：");
//		System.out.println(stringBuilder);
		byte b=(byte)Integer.parseInt(stringBuilder.substring(0,8), 2);
		stringBuilder.delete(0,8);
		super.write(b);
		super.close();
	}
}
