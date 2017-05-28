package pathSelection;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by cellargalaxy on 2017/5/19.
 */
public class Site implements Serializable{
	private int id;
	private String name;
	
	private double dimension;
	private double longitude;
	private boolean isP;
	private double countLen;
	private Set<Site> proSites;
	private Set<GoSite> goSites;
	
	private Site busSite;
	private int start;
	private int len;
	
	public Site(String name) {
		this(0,name);
	}
	
	public Site(int id) {
		this(id,null);
	}
	
	public Site(int id, String name) {
		this.id=id;
		this.name = name;
		isP=false;
		countLen=Integer.MAX_VALUE;
		proSites=new HashSet<Site>();
		goSites=new HashSet<GoSite>();
	}
	
	
	public void addProSite(Site site){
		proSites.add(site);
	}
	
	public void addNextSite(GoSite goSite){
		goSites.add(goSite);
	}
	/////////////////////////////////////////////////////////////////
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isP() {
		return isP;
	}
	
	public void setP(boolean p) {
		isP = p;
	}
	
	public double getCountLen() {
		return countLen;
	}
	
	public void setCountLen(double countLen) {
		this.countLen = countLen;
	}
	
	public Set<Site> getProSites() {
		return proSites;
	}
	
	public void setProSites(Set<Site> proSites) {
		this.proSites = proSites;
	}
	
	public Set<GoSite> getGoSites() {
		return goSites;
	}
	
	public void setGoSites(Set<GoSite> goSites) {
		this.goSites = goSites;
	}
	
	public Site getBusSite() {
		return busSite;
	}
	
	public void setBusSite(Site busSite) {
		this.busSite = busSite;
	}
	
	public int getStart() {
		return start;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
	public int getLen() {
		return len;
	}
	
	public void setLen(int len) {
		this.len = len;
	}
	
	public double getDimension() {
		return dimension;
	}
	
	public void setDimension(double dimension) {
		this.dimension = dimension;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return "Site{" +
				"id=" + id +
				", name='" + name + '\'' +
				", start=" + start +
				", len=" + len +
				'}';
	}
	
	public String toAllString() {
		return "Site{" +
				"id=" + id +
				", name='" + name + '\'' +
				", start=" + start +
				", len=" + len +
				", isP=" + isP +
				", countLen=" + countLen +
				", proSites=" + proSites +
				", goSites=" + goSites +
				'}';
	}
}
