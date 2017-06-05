package web;

/**
 * Created by cellargalaxy on 2017/5/31.
 */
public class FileBean {
	private String fileName;
	private long originalSize;
	private long laterSize;
	private float ratio;
	
	public FileBean(String fileName, long originalSize, long laterSize) {
		this.fileName = fileName;
		this.originalSize = originalSize;
		this.laterSize = laterSize;
		ratio = laterSize * 1.0F / originalSize;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public long getOriginalSize() {
		return originalSize;
	}
	
	public void setOriginalSize(long originalSize) {
		this.originalSize = originalSize;
	}
	
	public long getLaterSize() {
		return laterSize;
	}
	
	public void setLaterSize(long laterSize) {
		this.laterSize = laterSize;
	}
	
	public float getRatio() {
		return ratio;
	}
	
	public void setRatio(float ratio) {
		this.ratio = ratio;
	}
}
