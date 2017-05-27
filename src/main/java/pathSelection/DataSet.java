package pathSelection;

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
	private int[][] busSites;
	private GoSite[][] goSites;
	
	private Map<String,Site> map;
	
	public static void main(String[] args) {
		File[] dateSetFiles={new File("F:/xi/数据结构实验/t11.csv"),new File("F:/xi/数据结构实验/t30.csv"),new File("F:/xi/数据结构实验/t39.csv"),
				new File("F:/xi/数据结构实验/t54.csv"),new File("F:/xi/数据结构实验/t252.csv"),new File("F:/xi/数据结构实验/t468.csv")};
		DataSet dataSet=new DataSet(dateSetFiles,",","utf-8");
		System.out.println("所有的站点：");
		System.out.println(Arrays.toString(dataSet.sites));
		System.out.println("-----------------------------");
		System.out.println("所有的bus：");
		System.out.println(Arrays.toString(dataSet.busNames));
		System.out.println("-----------------------");
		System.out.println("各条bus经过的站：");
		for (int[] busSite : dataSet.busSites) {
			System.out.println(Arrays.toString(busSite));
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
		double dx=(site1.getDimension()-site2.getDimension())*100000;
		double dy=(site1.getLongitude()+site2.getLongitude())*100000;
		return Math.pow( ( (dx*dx)+(dy*dy) ) ,0.5);
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
		busSites=new int[dateSetFiles.length][];
		for (int i = 0; i < dateSetFiles.length; i++) {
			busNames[i]=dateSetFiles[i].getName().substring(0,dateSetFiles[i].getName().lastIndexOf('.'));
			LinkedList<Site> sites=readBus(dateSetFiles[i],separator,coding);
			busSites[i]=new int[sites.size()];
			int j=0;
			for (Site site : sites) {
				busSites[i][j]=site.getId();
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
