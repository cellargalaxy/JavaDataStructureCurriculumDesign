package huffman;

import java.io.*;

/**
 * Created by cellargalaxy on 2017/5/15.
 */
public class HuffmanDecodingInputStream extends BufferedInputStream{
	private HuffmanCoding huffmanCoding;
	private StringBuilder stringBuilder;
	private byte over;
	
	public static void main(String[] args) throws IOException {
		HuffmanCountInputStream countInputStream=new HuffmanCountInputStream(new FileInputStream("g:/t.mp4"));
		long[] counts=countInputStream.getCounts();
		HuffmanCoding huffmanCoding=new HuffmanCoding(counts);


		huffmanCoding.printCoding();

		BufferedInputStream inputStream=new BufferedInputStream(new FileInputStream("g:/t.mp4"));
		HuffmanEncodingOutputStream encodingOutputStream=new HuffmanEncodingOutputStream(new FileOutputStream("g:/t.ha"),huffmanCoding);
		int len;
		byte[] bytes=new byte[1024];
		while ((len = inputStream.read(bytes,0,bytes.length)) != -1) {
			encodingOutputStream.write(bytes,0,len);
		}
		inputStream.close();
		encodingOutputStream.close();
		
		System.out.println("-----------------------------------");
		HuffmanDecodingInputStream decodingInputStream=new HuffmanDecodingInputStream(new FileInputStream("g:/t.ha"));
		BufferedOutputStream outputStream=new BufferedOutputStream(new FileOutputStream("g:/t2.mp4"));
		while ((len = decodingInputStream.read(bytes,0,bytes.length)) != -1){
			outputStream.write(bytes,0,len);
		}
		outputStream.close();
		decodingInputStream.close();
	}
	
	public HuffmanDecodingInputStream(InputStream in) {
		super(in);
		stringBuilder=new StringBuilder();
		over=2;
	}
	
	@Override
	public synchronized int read(byte[] bs, int off, int len) throws IOException {
		if (huffmanCoding==null) {
			int result=0;
			byte[] bytes=new byte[3072];
			while (result<3072){
				int i=super.read(bytes,result,bytes.length);
				if (i!=-1) {
					result+=i;
				}else {
					throw new IOException("哈夫曼压缩文件已损坏");
				}
			}
			String codingHead=new String(bytes).trim();
			String[] strings=codingHead.split(":");
			String[] codings=new String[Byte.MAX_VALUE-Byte.MIN_VALUE+1];
			for (int i = 0; i < strings.length; i++) {
				if (strings[i].length()>0) {
					codings[i]=strings[i].trim();
				}
			}
			huffmanCoding=new HuffmanCoding(codings);
			huffmanCoding.printCoding();
		}
		
		if (stringBuilder.length()>32) {
//			System.out.println("再次编码：");
//			System.out.println(stringBuilder);
			int result=0;
			StringBuilder s=new StringBuilder("");
			while (stringBuilder.length() > 8 && off+result<bs.length) {
				s.append(stringBuilder.substring(0,1));
				stringBuilder.delete(0,1);
				Byte b=huffmanCoding.codingToByte(s.toString());
				if (b!=null) {
					bs[off+result]=b;
					result++;
					s.delete(0,s.length());
				}
			}
			stringBuilder.insert(0,s);
//			System.out.println("再次剩余编码：");
//			System.out.println(stringBuilder);
			return result;
		}
		
		int result=super.read(bs, off, len);
		if (result!=-1) {
			for (int i = off; i < result; i++) {
				String s=Integer.toBinaryString(bs[i]);
				if (s.length()>8) {
					s=s.substring(s.length()-8);
				}else if (s.length()<8){
					while (s.length() < 8) {
						s="0"+s;
					}
				}
				stringBuilder.append(s);
			}
//			System.out.println("编码：");
//			System.out.println(stringBuilder);
			result=0;
			StringBuilder s=new StringBuilder("");
			while (stringBuilder.length() > 8 && off+result<bs.length) {
				s.append(stringBuilder.substring(0,1));
				stringBuilder.delete(0,1);
				Byte b=huffmanCoding.codingToByte(s.toString());
				if (b!=null) {
					bs[off+result]=b;
					result++;
					s.delete(0,s.length());
				}
			}
			stringBuilder.insert(0,s);
//			System.out.println("剩余编码：");
//			System.out.println(stringBuilder);
			return result;
		}else {
			if (stringBuilder.length()==0) {
				return -1;
			}else {
				result=0;
				stringBuilder.delete(stringBuilder.length()-over,stringBuilder.length());
//				System.out.println("删除剩余编码：");
//				System.out.println(stringBuilder);
				StringBuilder s=new StringBuilder("");
				while (stringBuilder.length() > 0) {
					s.append(stringBuilder.substring(0,1));
					stringBuilder.delete(0,1);
					Byte b=huffmanCoding.codingToByte(s.toString());
					if (b!=null) {
						bs[off+result]=b;
						result++;
						s.delete(0,s.length());
					}
				}
				return result;
			}
		}
	}
	
}
