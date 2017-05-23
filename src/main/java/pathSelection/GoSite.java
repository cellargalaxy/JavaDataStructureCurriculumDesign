package pathSelection;

import java.io.Serializable;

/**
 * Created by cellargalaxy on 2017/5/19.
 */
public class GoSite implements Serializable{
	private Site first;
	private double len;
	private Site end;
	
	public GoSite(Site first, double len, Site end) {
		this.first = first;
		this.len = len;
		this.end = end;
	}
	
	public Site getFirst() {
		return first;
	}
	
	public void setFirst(Site first) {
		this.first = first;
	}
	
	public double getLen() {
		return len;
	}
	
	public void setLen(double len) {
		this.len = len;
	}
	
	public Site getEnd() {
		return end;
	}
	
	public void setEnd(Site end) {
		this.end = end;
	}
	
	@Override
	public String toString() {
		return "GoSite{" +
//				"first=" + first +
				", len=" + len +
				", end=" + end +
				'}';
	}
}
