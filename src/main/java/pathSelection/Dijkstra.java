package pathSelection;

import util.CloneObject;

import java.io.IOException;
import java.util.*;

/**
 * Created by cellargalaxy on 2017/5/19.
 */
public class Dijkstra {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String[] siteNames={"s0","s1","s2","s3","s4","s5","s6","s7","s8"};
		
		double[][] lens={
				/*   0 1 2 3 4 5 6 7 8    */
		/*0*/		{0,1,2,0,0,0,0,8,0},
		/*1*/		{1,0,0,4,3,0,2,0,0},
		/*2*/		{2,0,0,0,3,5,0,0,0},
		/*3*/		{0,4,0,0,0,0,2,0,0},
		/*4*/		{0,3,3,0,0,0,2,1,3},
		/*5*/		{0,0,5,0,0,0,0,2,0},
		/*6*/		{0,2,0,2,2,0,0,0,4},
		/*7*/		{8,0,0,0,1,2,0,0,4},
		/*8*/		{0,0,0,0,3,0,4,4,0},
		};
		
		int[][] busPaths={
		/*1*/		{0,1,6,4,8},
		/*2*/		{0,1,4,7,8},
		/*3*/		{0,1,3,6,8}
		};
		
		LinkedList<LinkedList<Site>> paths=createPaths(siteNames,lens,busPaths);
		
		String[] busNames={"B0","B1","B2"};
		
		LinkedList<LinkedList<BusPath>> bps=dijkstraBuses(paths,busPaths,busNames);
		for (LinkedList<BusPath> bp : bps) {
			for (BusPath busPath : bp) {
				if (busPath.getBusName()!=null) {
					System.out.println("到"+busPath.getStart().getName()+"乘坐"+busPath.getBusName()+"到"+busPath.getEnd().getName()+"下车");
				}else {
					System.out.println("从"+busPath.getStart().getName()+"步行到"+busPath.getEnd().getName());
				}
			}
			System.out.println("到达目的地");
		}
	}
	
	public static LinkedList<LinkedList<BusPath>> dijkstraBuses(LinkedList<LinkedList<Site>> paths, int[][] busPaths, String[] busNames) throws IOException, ClassNotFoundException {
		LinkedList<LinkedList<BusPath>> bps=new LinkedList<LinkedList<BusPath>>();
		for (LinkedList<Site> path : paths) {
			bps.add(dijkstraBus(path,busPaths,busNames));
		}
		return bps;
	}
	
	public static LinkedList<BusPath> dijkstraBus(LinkedList<Site> path, int[][] busPaths, String[] busNames) throws IOException, ClassNotFoundException {
		Site[] busSites=Graph.createBusesGraph(path,busPaths,busNames);
		int count=0;
		for (Site busSite : busSites) {
			if (busSite!=null) {
				count++;
			}
		}
		Site[] newBusSite=new Site[count];
		count=0;
		for (Site busSite : busSites) {
			if (busSite!=null) {
				newBusSite[count]=busSite;
				count++;
			}
		}
		newBusSite=dijkstra(newBusSite,0,newBusSite.length-1);
		LinkedList<LinkedList<Site>> answer=createPaths(newBusSite,0,newBusSite.length-1);
		LinkedList<BusPath> bp=new LinkedList<BusPath>();
		for (LinkedList<Site> an : answer) {
			Site s=null;
			for (Site site : an) {
				s=site;
				bp.add(new BusPath(path.get(site.getStart()),site.getName(),path.get(site.getStart()+site.getLen()-1)));
			}
			if (!path.get(s.getStart()+s.getLen()-1).equals(path.getLast())) {
				bp.add(new BusPath(path.get(s.getStart()+s.getLen()-1),null,path.getLast()));
			}
		}
		return bp;
	}
	
	public static LinkedList<LinkedList<Site>> createPaths(String[] siteNames,double[][] lens,int[][] busPaths) throws IOException, ClassNotFoundException {
		Site[] sites=Graph.createSites(siteNames);
		GoSite[][] goSites=Graph.createGoSites(lens,sites);
		sites=Graph.createSitesGraph(sites,busPaths,goSites);
		sites=Dijkstra.dijkstra(sites,0,8);
		return createPaths(sites,0,8);
	}
	
	
	public static LinkedList<LinkedList<Site>> createPaths(Site[] sites,int startPoint,int endPoint) throws IOException, ClassNotFoundException {
		LinkedList<LinkedList<Site>> paths=new LinkedList<LinkedList<Site>>();
		LinkedList<Site> endPath=new LinkedList<Site>();
		endPath.add(sites[endPoint]);
		paths.add(endPath);
		createSubPaths(paths);
		return paths;
	}
	
	private static void createSubPaths(LinkedList<LinkedList<Site>> paths) throws IOException, ClassNotFoundException {
		boolean goon;
		do {
			goon=false;
			Iterator<LinkedList<Site>> iterator=paths.iterator();
			LinkedList<LinkedList<Site>> newPaths=new LinkedList<LinkedList<Site>>();
			
			while (iterator.hasNext()) {
				LinkedList<Site> path=iterator.next();
				Site site=path.getFirst();
				Set<Site> proSites=site.getProSites();
				if (proSites.size()==1) {
					goon=true;
					Iterator<Site> iterator2=proSites.iterator();
					path.addFirst(iterator2.next());
				}else if(proSites.size()>1) {
					goon=true;
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
		}while (goon);
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
	public static void subDijkstra(Site site,Site end,Set<Site> ts){
		Set<GoSite> goSites=site.getGoSites();
		double len;
		for (GoSite goSite : goSites) {
			len=site.getCountLen()+goSite.getLen();
			if (len<=goSite.getEnd().getCountLen()) {
				goSite.getEnd().addProSite(site);
				goSite.getEnd().setCountLen(len);
				if (!goSite.getEnd().isP()) {
					ts.add(goSite.getEnd());
				}
			}
		}
		
		double minLen=Double.MAX_VALUE;
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
