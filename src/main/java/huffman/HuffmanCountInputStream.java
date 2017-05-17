package huffman;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by cellargalaxy on 2017/5/15.
 */
public class HuffmanCountInputStream extends BufferedInputStream{
	private long[] counts;
	
	
	public HuffmanCountInputStream(InputStream in) throws IOException {
		super(in);
		counts=new long[Byte.MAX_VALUE-Byte.MIN_VALUE+1];
		count();
	}
	
	private void count() throws IOException {
		byte[] bytes=new byte[1024];
		try {
			while (read(bytes, 0, bytes.length) != -1) ;
		}finally {
			try { close(); }catch (Exception e){ e.printStackTrace(); }
		}
	}
	
	@Override
	public synchronized int read(byte[] bs, int off, int len) throws IOException {
		int result=super.read(bs, off, len);
		for (int i = off; i < result; i++) {
			counts[bs[i]-Byte.MIN_VALUE]+=1;
		}
		return result;
	}
	
	public long[] getCounts() {
		return counts;
	}
	
	public void setCounts(long[] counts) {
		this.counts = counts;
	}
}
