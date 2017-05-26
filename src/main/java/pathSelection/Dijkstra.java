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
		
		int[][] busRoutes={
		/*1*/		{0,1,6,4,8},
		/*2*/		{0,1,4,7,8},
		/*3*/		{0,1,3,6,8}
		};
		
		Site[] sites=Graph.createSites(siteNames);
		GoSite[][] goSites=Graph.createGoSites(lens,sites);
		LinkedList<LinkedList<Site>> paths=createPaths(sites,goSites,busRoutes,0,8);
		
//		for (LinkedList<Site> path : paths) {
//			System.out.println(path);
//		}
		
		String[] busNames={"B0","B1","B2"};

		LinkedList<LinkedList<GoBus>> busPaths=dijkstraBuses(paths,busRoutes);
		for (LinkedList<GoBus> busPath : busPaths) {
			for (GoBus goBus : busPath) {
				System.out.println("到"+goBus.getStart().getName()+"乘坐"+busNames[goBus.getBusId()]+"到"+goBus.getEnd().getName()+"下车");
			}
			System.out.println("到达目的地");
		}
	}
	
	public static LinkedList<LinkedList<GoBus>> dijkstraBuses(LinkedList<LinkedList<Site>> paths, int[][] busRoutes) throws IOException, ClassNotFoundException {
		LinkedList<LinkedList<GoBus>> goBusess=new LinkedList<LinkedList<GoBus>>();
		for (LinkedList<Site> path : paths) {
			Site[] busSites=Graph.createBusesGraph(path,busRoutes);
			
			
			
			dijkstra(busSites,0,busSites.length-1);
			
			
			System.out.println("--------------------------");
			System.out.println(Arrays.toString(busSites));
			System.out.println("---------------------------");
			
			LinkedList<LinkedList<Site>> busPaths=createPaths(busSites[busSites.length-1]);
			for (LinkedList<Site> busPath : busPaths) {
				LinkedList<GoBus> goBuses=new LinkedList<GoBus>();
				for (Site site : busPath) {
//					System.out.println("start:"+site.getStart()+":"+(site.getStart()+site.getLen()));
					goBuses.add(new GoBus(
							path.get(site.getStart()),
							site.getId(),
							path.get(site.getStart()+site.getLen()-1)
					));
				}
				System.out.println("::"+goBuses);
				goBusess.add(goBuses);
			}
		}
		return goBusess;
	}
	
//	public static LinkedList<LinkedList<GoBus>> dijkstraBus(LinkedList<Site> path, int[][] busRoutes) throws IOException, ClassNotFoundException {
//		Site[] busSites=Graph.createBusesGraph(path,busRoutes);
//		dijkstra(busSites,0,busSites.length-1);
//		LinkedList<LinkedList<Site>> busPaths=createPaths(path.getLast());
//		LinkedList<LinkedList<GoBus>> goBusess=new LinkedList<LinkedList<GoBus>>();
//		for (LinkedList<Site> busPath : busPaths) {
//			LinkedList<GoBus> goBuses=new LinkedList<GoBus>();
//			for (Site site : busPath) {
//				goBuses.add(new GoBus(
//						path.get(site.getStart()),
//						site.getBusSite().getId(),
//						path.get(site.getStart()+site.getLen()-1)
//				));
//			}
//			goBusess.add(goBuses);
//		}
//		return goBusess;
//	}
	
	/////////////////////////////////////////////////////////////////////
	
	public static LinkedList<LinkedList<Site>> createPaths(Site[] sites,GoSite[][] goSites,int[][] busRoutes,int startPoint,int endPoint) throws IOException, ClassNotFoundException {
		sites=Graph.createSitesGraph(sites,busRoutes,goSites);
		sites=Dijkstra.dijkstra(sites,startPoint,endPoint);
		return createPaths(sites,endPoint);
	}
	
	private static LinkedList<LinkedList<Site>> createPaths(Site[] sites,int endPoint) throws IOException, ClassNotFoundException {
		return createPaths(sites[endPoint]);
	}
	private static LinkedList<LinkedList<Site>> createPaths(Site endSite) throws IOException, ClassNotFoundException {
		LinkedList<LinkedList<Site>> paths=new LinkedList<LinkedList<Site>>();
		LinkedList<Site> endPath=new LinkedList<Site>();
		endPath.add(endSite);
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
	
	private static Site[] dijkstra(Site[] sites,int startPoint,int endPoint) throws IOException, ClassNotFoundException {
		Site[] sitesClone= CloneObject.clone(sites);
		Site site=sitesClone[startPoint];
		site.setCountLen(0);
		site.setP(true);
		
		Site end=sitesClone[endPoint];
		Set<Site> ts= new HashSet<Site>();
		subDijkstra(site,end,ts);
		return sitesClone;
	}
	private static void subDijkstra(Site site,Site end,Set<Site> ts){
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
