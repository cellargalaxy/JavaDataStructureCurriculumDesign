package pathSelection;

import java.io.Serializable;

/**
 * Created by cellargalaxy on 2017/5/19.
 */
public class GoSite implements Serializable{
	private int len;
	private Site site;
	
	public GoSite(int len, Site site) {
		this.len = len;
		this.site = site;
	}
	
	public int getLen() {
		return len;
	}
	
	public void setLen(int len) {
		this.len = len;
	}
	
	public Site getSite() {
		return site;
	}
	
	public void setSite(Site site) {
		this.site = site;
	}
	
	@Override
	public String toString() {
		return "GoSite{" +
				"len=" + len +
				", site=" + site +
				'}';
	}
}
