package pathSelection;

/**
 * Created by cellargalaxy on 2017/5/23.
 */
public class BusPath {
	private Site start;
	private String busName;
	private Site end;
	
	public BusPath(Site start, String busName, Site end) {
		this.start = start;
		this.busName = busName;
		this.end = end;
	}
	
	public Site getStart() {
		return start;
	}
	
	public void setStart(Site start) {
		this.start = start;
	}
	
	public String getBusName() {
		return busName;
	}
	
	public void setBusName(String busName) {
		this.busName = busName;
	}
	
	public Site getEnd() {
		return end;
	}
	
	public void setEnd(Site end) {
		this.end = end;
	}
}
