package pathSelection;

import huffman.Link;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by cellargalaxy on 2017/5/19.
 */
public class Site implements Serializable{
	private String name;
	private boolean isP;
	private int countLen;
	private LinkedList<Site> proSites;
	private LinkedList<GoSite> goSites;
	
	public Site(String name) {
		this.name = name;
		isP=false;
		countLen=Integer.MAX_VALUE;
		proSites=new LinkedList<Site>();
		goSites=new LinkedList<GoSite>();
	}
	
	public void addProSite(Site site){
		proSites.add(site);
	}
	
	public void addNextSite(int len,Site site){
		goSites.add(new GoSite(len,site));
	}
	/////////////////////////////////////////////////////////////////
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
	
	public int getCountLen() {
		return countLen;
	}
	
	public void setCountLen(int countLen) {
		this.countLen = countLen;
	}
	
	public LinkedList<Site> getProSites() {
		return proSites;
	}
	
	public void setProSites(LinkedList<Site> proSites) {
		this.proSites = proSites;
	}
	
	public LinkedList<GoSite> getGoSites() {
		return goSites;
	}
	
	public void setGoSites(LinkedList<GoSite> goSites) {
		this.goSites = goSites;
	}
	
	@Override
	public String toString() {
		return "Site{" +
				"name='" + name + '\'' +
				", isP=" + isP +
				", countLen=" + countLen +
				'}';
	}
	
	public String toStringAll() {
		return "Site{" +
				"name='" + name + '\'' +
				", isP=" + isP +
				", countLen=" + countLen +
				", \r\n	proSites=" + proSites +
				", \r\n	goSites=" + goSites +
				'}';
	}
}
