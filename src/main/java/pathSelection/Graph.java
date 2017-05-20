package pathSelection;


/**
 * Created by cellargalaxy on 2017/5/19.
 */
public class Graph {
	
	
	public static Site[] createGraph(String[] siteNames,int[][] lens){
		Site[] sites=new Site[siteNames.length];
		for (int i = 0; i < siteNames.length; i++) {
			sites[i]=new Site(siteNames[i]);
		}
		for (int i = 0; i < lens.length; i++) {
			for (int j = 0; j < lens[i].length; j++) {
				if (lens[i][j]>=0) {
					sites[i].addNextSite(lens[i][j],sites[j]);
				}
			}
		}
		return sites;
	}
	
	public static void printGraph(Site[] sites){
		for (Site site : sites) {
			System.out.println(site.toStringAll());
		}
	}
}
