package pathSelection;


import util.CloneObject;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by cellargalaxy on 2017/5/19.
 */
public class Graph {
	
	public static Site[] createSites(String[] siteNames){
		Site[] sites=new Site[siteNames.length];
		for (int i = 0; i < siteNames.length; i++) {
			sites[i]=new Site(i,siteNames[i]);
		}
		return sites;
	}
	
	public static GoSite[][] createGoSites(double[][] lens,Site[] sites){
		GoSite[][] goSites=new GoSite[lens.length][];
		for (int i = 0; i < lens.length; i++) {
			goSites[i]=new GoSite[lens[i].length];
			for (int j = 0; j < lens[i].length; j++) {
				if (lens[i][j]>0) {
					goSites[i][j]=new GoSite(sites[i],lens[i][j],sites[j]);
				}
			}
		}
		return goSites;
	}
	
	
	
	public static Site[] createSitesGraph(Site[] sites,int[][] busPaths,GoSite[][] goSites){
		for (int[] busPath : busPaths) {
			for (int i = 0; i < busPath.length-1; i++) {
				sites[busPath[i]].addNextSite(goSites[ busPath[i] ][ busPath[i+1] ]);
			}
		}
		return sites;
	}
	
	public static Site[] createBusesGraph(LinkedList<Site> path, int[][] busPaths, String[] busNames) throws IOException, ClassNotFoundException {
		Site[] busSites=new Site[path.size()-1];
		LinkedList<Site> bs=addBusesSites(path,busPaths,busNames);
		for (Site site : bs) {
			if (busSites[site.getStart()]==null||busSites[site.getStart()].getLen()<site.getLen()) {
				busSites[site.getStart()]=site;
			}
		}
		for (int i = 0; i < busSites.length; i++) {
			if (busSites[i]==null) {
				continue;
			}
			for (int j = i+1; j < busSites.length; j++) {
				if (busSites[j]!=null) {
					linkSite(busSites[i],busSites[j]);
				}
			}
		}
		return busSites;
	}
	private static void linkSite(Site site1,Site site2){
		site1.addNextSite(new GoSite(site1,1,site2));
		site2.addNextSite(new GoSite(site2,1,site1));
	}
	public static LinkedList<Site> addBusesSites(LinkedList<Site> path, int[][] busPaths, String[] busNames) throws IOException, ClassNotFoundException {
		LinkedList<Site> busSites=new LinkedList<Site>();
		for (int i = 0; i < busPaths.length; i++) {
			addBusSite(path,busPaths[i],i,busNames[i],busSites);
		}
		return busSites;
	}
	private static void addBusSite(LinkedList<Site> path,int[] busPath,int busId,String busName,LinkedList<Site> busSites) throws IOException, ClassNotFoundException {
		LinkedList<Site> newPath= CloneObject.clone(path);
		Site site1;
		Site site2=null;
		int point=0;
		int i=0;
		for (Site site : newPath) {
			if (site2==null) {
				site2=site;
				continue;
			}
			site1=site2;
			site2=site;
			int j= findBusPath(site1,site2,point,busPath);
			if (point<j) {
				point=j;
				Site busSite=site1.getBusSite();
				if (busSite==null) {
					busSite=new Site(busId,busName);
					busSites.add(busSite);
					site1.setBusSite(busSite);
					site2.setBusSite(busSite);
					busSite.setLen(2);
					busSite.setStart(i);
				}else {
					site2.setBusSite(busSite);
					busSite.setLen(busSite.getLen()+1);
				}
			}
			i++;
		}
	}
	private static int findBusPath(Site site1, Site site2, int point, int[] busPath){
		for (; point < busPath.length-1; point++) {
			if (site1.getId()==busPath[point]&&site2.getId()==busPath[point+1]) {
				return point+1;
			}
		}
		return -1;
	}
	
	
	
	public static void printGraph(Site[] sites){
		for (Site site : sites) {
			System.out.println(site.toAllString());
		}
	}
}
