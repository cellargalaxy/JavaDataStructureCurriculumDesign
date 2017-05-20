package pathSelection;

import util.CloneObject;

import java.io.IOException;
import java.util.*;

/**
 * Created by cellargalaxy on 2017/5/19.
 */
public class Dijkstra {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String[] siteNames={"a1","b2","c3","d4","f5","g6","h7","i8","j9"};
		int[][] lens={
		/*1*/		{-1,1,2,-1,-1,-1,-1,8,-1},
		/*2*/		{1,-1,-1,4,3,-1,2,-1,-1},
		/*3*/		{2,-1,-1,-1,3,5,-1,-1,-1},
		/*4*/		{-1,4,-1,-1,-1,5,-1,-1,-1},
		/*5*/		{-1,3,3,-1,-1,-1,2,1,3},
		/*6*/		{-1,-1,5,-1,-1,-1,-1,2,-1},
		/*7*/		{-1,2,-1,2,2,-1,-1,-1,4},
		/*8*/		{8,-1,-1,-1,1,2,-1,-1,4},
		/*9*/		{-1,-1,-1,-1,3,-1,4,4,-1}
		};
		
		Site[] sites=Graph.createGraph(siteNames,lens);
		Graph.printGraph(sites);
		System.out.println("---------------------------------------");
		
		sites=Dijkstra.dijkstra(sites,0,8);
		Graph.printGraph(sites);
		System.out.println("---------------------------------------");
		
		LinkedList<LinkedList<Site>> paths=createPaths(sites,0,8);
		for (LinkedList<Site> path : paths) {
			System.out.println(path);
		}
		System.out.println("--------------------------------------");
	}
	
	public static LinkedList<LinkedList<Site>> createPaths(Site[] sites,int startPoint,int endPoint) throws IOException, ClassNotFoundException {
		LinkedList<LinkedList<Site>> paths=new LinkedList<LinkedList<Site>>();
		LinkedList<Site> endPath=new LinkedList<Site>();
		endPath.add(sites[endPoint]);
		paths.add(endPath);
		createSubPaths(paths);
		for (LinkedList<Site> path : paths) {
			path.addFirst(sites[startPoint]);
		}
		return paths;
	}
	
	private static void createSubPaths(LinkedList<LinkedList<Site>> paths) throws IOException, ClassNotFoundException {
		Iterator<LinkedList<Site>> iterator=paths.iterator();
		LinkedList<LinkedList<Site>> newPaths=new LinkedList<LinkedList<Site>>();
		
		while (iterator.hasNext()) {
			LinkedList<Site> path=iterator.next();
			Site site=path.getFirst();
			LinkedList<Site> proSites=site.getProSites();
			if (proSites.size()==1) {
				path.addFirst(proSites.getFirst());
			}else if(proSites.size()>1) {
				iterator.remove();
				for (Site proSite : proSites) {
					LinkedList<Site> newPath=CloneObject.clone(path);
					newPath.addFirst(proSite);
					newPaths.add(newPath);
				}
			}
		}
		
		for (LinkedList<Site> newPath : newPaths) {
			paths.add(newPath);
		}
	}
	
	public static Site[] dijkstra(Site[] sites,int startPoint,int endPoint) throws IOException, ClassNotFoundException {
		Site[] sitesClone= CloneObject.clone(sites);
		Site site=sitesClone[startPoint];
		Site end=sitesClone[endPoint];
		site.setCountLen(0);
		site.setP(true);
		
		Set<Site> ts= new HashSet<Site>();
		subDijkstra(site,end,ts);
		return sitesClone;
	}
	public static void subDijkstra(Site site,Site end, Set<Site> ts){
		LinkedList<GoSite> goSites=site.getGoSites();
		int len;
		for (GoSite goSite : goSites) {
			len=site.getCountLen()+goSite.getLen();
			if (len<=goSite.getSite().getCountLen()) {
				goSite.getSite().addProSite(site);
				goSite.getSite().setCountLen(len);
				if (!goSite.getSite().isP()) {
					ts.add(goSite.getSite());
				}
			}
		}
		
		int minLen=Integer.MAX_VALUE;
		for (Site t : ts) {
			if (t.getCountLen()<minLen) {
				minLen=t.getCountLen();
			}
		}
		LinkedList<Site> minSites=new LinkedList<Site>();
		Iterator<Site> iterator=ts.iterator();
		while (iterator.hasNext()) {
			Site minSite=iterator.next();
			if (minSite.getCountLen()==minLen) {
				iterator.remove();
				minSites.add(minSite);
				minSite.setP(true);
			}
		}
		
		if (minSites.contains(end)) {
			return;
		}
		
		for (Site minSite : minSites) {
			subDijkstra(minSite,end,ts);
		}
	}
}
