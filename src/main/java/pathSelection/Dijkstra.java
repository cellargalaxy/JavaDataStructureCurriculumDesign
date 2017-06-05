package pathSelection;

import util.CloneObject;

import java.io.IOException;
import java.util.*;

/**
 * Created by cellargalaxy on 2017/5/19.
 */
public class Dijkstra {
	
	public static LinkedList<LinkedList<GoBus>> dijkstraBuses(LinkedList<LinkedList<Site>> paths, int[][] busRoutes) throws IOException, ClassNotFoundException {
		LinkedList<LinkedList<GoBus>> goBusess = new LinkedList<LinkedList<GoBus>>();
		for (LinkedList<Site> path : paths) {
			Site[] busSites = Graph.createBusesGraph(path, busRoutes);
			
			if (busSites.length == 0) {
				return goBusess;
			}
			busSites = dijkstra(busSites, 0, busSites.length - 1);
			LinkedList<LinkedList<Site>> busPaths = createPaths(busSites[busSites.length - 1]);
			
			for (LinkedList<Site> busPath : busPaths) {
				LinkedList<GoBus> goBuses = new LinkedList<GoBus>();
				int end = -1;
				for (Site site : busPath) {
					if (end == -1) {
						end = site.getStart();
					}
					goBuses.add(new GoBus(
							path.get(end),
							site.getId(),
							path.get(site.getStart() + site.getLen() - 1)
					));
					end = site.getStart() + site.getLen() - 1;
				}
				goBusess.add(goBuses);
			}
		}
		return goBusess;
	}
	
	/////////////////////////////////////////////////////////////////////
	
	public static LinkedList<LinkedList<Site>> createPaths(Site[] sites, int endPoint) throws IOException, ClassNotFoundException {
		return createPaths(sites[endPoint]);
	}
	
	public static LinkedList<LinkedList<Site>> createPaths(Site endSite) throws IOException, ClassNotFoundException {
		LinkedList<LinkedList<Site>> paths = new LinkedList<LinkedList<Site>>();
		LinkedList<Site> endPath = new LinkedList<Site>();
		endPath.add(endSite);
		paths.add(endPath);
		return createSubPaths(paths);
	}
	
	private static LinkedList<LinkedList<Site>> createSubPaths(LinkedList<LinkedList<Site>> paths) throws IOException, ClassNotFoundException {
		boolean goon;
		do {
			goon = false;
			Iterator<LinkedList<Site>> iterator = paths.iterator();
			LinkedList<LinkedList<Site>> newPaths = new LinkedList<LinkedList<Site>>();
			
			while (iterator.hasNext()) {
				LinkedList<Site> path = iterator.next();
				Site site = path.getFirst();
				Set<Site> proSites = site.getProSites();
				if (proSites.size() == 1) {
					goon = true;
					Iterator<Site> iterator2 = proSites.iterator();
					path.addFirst(iterator2.next());
				} else if (proSites.size() > 1) {
					goon = true;
					iterator.remove();
					for (Site proSite : proSites) {
						LinkedList<Site> newPath = CloneObject.clone(path);
						newPath.addFirst(proSite);
						newPaths.add(newPath);
					}
				}
			}
			
			for (LinkedList<Site> newPath : newPaths) {
				paths.add(newPath);
			}
		} while (goon);
		return paths;
	}
	
	
	public static Site[] dijkstra(Site[] sites, int startPoint, int endPoint) throws IOException, ClassNotFoundException {
		Site startSite = sites[startPoint];
		Site end = sites[endPoint];
		return dijkstra(sites, startSite, end);
	}
	
	public static Site[] dijkstra(Site[] sites, Site startSite, Site endSite) throws IOException, ClassNotFoundException {
		startSite.setCountLen(0);
		startSite.setP(true);
		
		Set<Site> ts = new HashSet<Site>();
		subDijkstra(startSite, endSite, ts);
		return sites;
	}
	
	private static void subDijkstra(Site site, Site end, Set<Site> ts) {
		Set<GoSite> goSites = site.getGoSites();
		double len;
		for (GoSite goSite : goSites) {
			len = site.getCountLen() + goSite.getLen();
			if (len <= goSite.getEnd().getCountLen()) {
				goSite.getEnd().addProSite(site);
				goSite.getEnd().setCountLen(len);
				if (!goSite.getEnd().isP()) {
					ts.add(goSite.getEnd());
				}
			}
		}
		
		double minLen = Double.MAX_VALUE;
		for (Site t : ts) {
			if (t.getCountLen() < minLen) {
				minLen = t.getCountLen();
			}
		}
		LinkedList<Site> minSites = new LinkedList<Site>();
		Iterator<Site> iterator = ts.iterator();
		while (iterator.hasNext()) {
			Site minSite = iterator.next();
			if (minSite.getCountLen() == minLen) {
				iterator.remove();
				minSites.add(minSite);
				minSite.setP(true);
			}
		}
		
		if (minSites.contains(end)) {
			return;
		}
		
		for (Site minSite : minSites) {
			subDijkstra(minSite, end, ts);
		}
	}
}
