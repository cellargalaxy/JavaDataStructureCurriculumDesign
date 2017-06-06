package huffman;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by cellargalaxy on 2017/5/15.
 */
public class HuffmanCountInputStream extends BufferedInputStream {
	private long[] counts;
	
	
	public HuffmanCountInputStream(InputStream in) throws IOException {
		super(in);
		counts = new long[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];
	}
	
	/**
	 * 重写了read方法，在读取的每一个比特数组时，
	 * 都会统计数组里各个比特值出现的频数，并保存到一个long数组里
	 *
	 * @param bs
	 * @param off
	 * @param len
	 * @return
	 * @throws IOException
	 */
	@Override
	public synchronized int read(byte[] bs, int off, int len) throws IOException {
		int result = super.read(bs, off, len);
		for (int i = off; i < result; i++) {
			counts[bs[i] - Byte.MIN_VALUE] += 1;
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
