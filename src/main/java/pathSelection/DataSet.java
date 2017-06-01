package pathSelection;

import jdk.nashorn.internal.ir.IfNode;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by cellargalaxy on 2017/5/26.
 */
public class DataSet {
	private Site[] sites;
	private String[] busNames;
	private int[][] busRoutes;
	private GoSite[][] goSites;
	
	private Map<String,Site> map;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		File[] dateSetFiles={new File("11路.csv"),new File("30路.csv"),new File("39路.csv"),
				new File("54路.csv"),new File("252路.csv"),new File("468路.csv")};
		DataSet dataSet=new DataSet(dateSetFiles,",","utf-8");
		
		dataSet.sites=Graph.createSitesGraph(dataSet.sites,dataSet.busRoutes,dataSet);
		String startSiteName="迎龙路";
		String endSiteName="广州塔西";
		Site startSite=null;
		Site endSite=null;
		for (Site site : dataSet.sites) {
			if (startSiteName.equals(site.getName())) {
				startSite=site;
			}
			if (endSiteName.equals(site.getName())) {
				endSite=site;
			}
		}
		dataSet.sites=Dijkstra.dijkstra(dataSet.sites,startSite,endSite);
		LinkedList<LinkedList<Site>> paths=Dijkstra.createPaths(endSite);
		
		LinkedList<LinkedList<GoBus>> busPaths=Dijkstra.dijkstraBuses(paths,dataSet.busRoutes);
		for (LinkedList<GoBus> busPath : busPaths) {
			for (GoBus goBus : busPath) {
				System.out.println("到"+goBus.getStart().getName()+"乘坐"+dataSet.busNames[goBus.getBusId()]+"到"+goBus.getEnd().getName()+"下车");
			}
			System.out.println("到达目的地");
		}
	}
	
	public DataSet(File[] dateSetFiles, String separator, String coding) {
		map=new HashMap<String, Site>();
		readDataSets(dateSetFiles,separator,coding);
	}
	
	public GoSite getGoSite(int i,int j){
		if (goSites[i][j]==null) {
			goSites[i][j]=new GoSite(sites[i],calculateDistance(sites[i],sites[j]),sites[j]);
		}
		return goSites[i][j];
	}
	private static double calculateDistance(Site site1,Site site2){
		double dx=(site1.getDimension()-site2.getDimension())*100;
		double dy=(site1.getLongitude()+site2.getLongitude())*100;
		return (dx*dx)+(dy*dy);
	}
	
	private void readDataSets(File[] dateSetFiles, String separator, String coding){
		for (File dateSetFile : dateSetFiles) {
			readSite(dateSetFile,separator,coding);
		}
		sites=new Site[map.size()];
		int id=0;
		for (Map.Entry<String, Site> entry : map.entrySet()) {
			sites[id]=entry.getValue();
			sites[id].setId(id);
			id++;
		}
		busNames=new String[dateSetFiles.length];
		busRoutes =new int[dateSetFiles.length][];
		for (int i = 0; i < dateSetFiles.length; i++) {
			busNames[i]=dateSetFiles[i].getName().substring(0,dateSetFiles[i].getName().lastIndexOf('.'));
			LinkedList<Site> sites=readBus(dateSetFiles[i],separator,coding);
			busRoutes[i]=new int[sites.size()];
			int j=0;
			for (Site site : sites) {
				busRoutes[i][j]=site.getId();
				j++;
			}
		}
		goSites=new GoSite[sites.length][sites.length];
	}
	
	private LinkedList<Site> readBus(File dataSetFile,String separator,String coding){
		BufferedReader reader=null;
		try {
			reader=new BufferedReader(new InputStreamReader(new FileInputStream(dataSetFile),coding));
			LinkedList<Site> sites=new LinkedList<Site>();
			String string;
			while ((string = reader.readLine()) != null) {
				String[] strings=string.split(separator);
				sites.add(map.get(strings[0].trim()));
			}
			return sites;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}finally {
			if (reader!=null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void readSite(File dataSetFile,String separator,String coding){
		BufferedReader reader=null;
		try {
			reader=new BufferedReader(new InputStreamReader(new FileInputStream(dataSetFile),coding));
			String string;
			while ((string = reader.readLine()) != null) {
				String[] strings=string.split(separator);
				Site site=new Site(strings[0].trim());
				site.setDimension(new Double(strings[1]));
				site.setLongitude(new Double(strings[2]));
				map.put(site.getName(),site);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if (reader!=null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
