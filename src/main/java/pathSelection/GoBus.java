package pathSelection;

/**
 * Created by cellargalaxy on 2017/5/23.
 */
public class GoBus {
	private Site start;
	private int busId;
	private Site end;
	
	public GoBus(Site start, int busId, Site end) {
		this.start = start;
		this.busId = busId;
		this.end = end;
	}
	
	public Site getStart() {
		return start;
	}
	
	public void setStart(Site start) {
		this.start = start;
	}
	
	public int getBusId() {
		return busId;
	}
	
	public void setBusId(int busId) {
		this.busId = busId;
	}
	
	public Site getEnd() {
		return end;
	}
	
	public void setEnd(Site end) {
		this.end = end;
	}
	
	@Override
	public String toString() {
		return "GoBus{" +
				"start=" + start.getId() +
				", busId=" + busId +
				", end=" + end.getId() +
				'}';
	}
}
