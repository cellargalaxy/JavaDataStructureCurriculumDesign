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
	
	public static void main(String[] args) {
		DataSet dataSet=new DataSet();
		File[] dateSetFiles={new File("F:/xi/数据结构实验/t11.csv"),new File("F:/xi/数据结构实验/t31.csv"),new File("F:/xi/数据结构实验/t39.csv"),
				new File("F:/xi/数据结构实验/t54.csv"),new File("F:/xi/数据结构实验/t252.csv"),new File("F:/xi/数据结构实验/t468.csv")};
		dataSet.readDataSets(dateSetFiles,",","utf-8");
		System.out.println(Arrays.toString(dataSet.sites));
		System.out.println("-----------------------------");
		System.out.println(Arrays.toString(dataSet.busNames));
		System.out.println("-----------------------");
		for (int[] busSite : dataSet.busSites) {
			System.out.println(Arrays.toString(busSite));
		}
	}
	
	public void readDataSets(File[] dateSetFiles,String separator,String coding){
		Map<String,LinkedList<Site>> dataSets=new HashMap<String, LinkedList<Site>>();
		int countSite=0;
		for (File dateSetFile : dateSetFiles) {
			String busName=dateSetFile.getName().substring(0,dateSetFile.getName().indexOf('.'));
			LinkedList<Site> sites=readSite(dateSetFile,separator,coding);
			countSite+=sites.size();
			dataSets.put(busName,sites);
		}
		int id=0;
		sites=new Site[countSite];
		for (Map.Entry<String, LinkedList<Site>> entry : dataSets.entrySet()) {
			for (Site site : entry.getValue()) {
				sites[id]=site;
				sites[id].setId(id);
				id++;
			}
		}
		int busId=0;
		busNames=new String[dataSets.size()];
		busSites=new int[dataSets.size()][];
		for (Map.Entry<String, LinkedList<Site>> entry : dataSets.entrySet()) {
			busNames[busId]=entry.getKey();
			busSites[busId]=new int[entry.getValue().size()];
			int i=0;
			for (Site site : entry.getValue()) {
				busSites[busId][i]=site.getId();
				i++;
			}
			busId++;
		}
	}
	
	public LinkedList<Site> readSite(File dataSetFile,String separator,String coding){
		BufferedReader reader=null;
		try {
			LinkedList<Site> sites=new LinkedList<Site>();
			reader=new BufferedReader(new InputStreamReader(new FileInputStream(dataSetFile),coding));
			String string;
			while ((string = reader.readLine()) != null) {
				String[] strings=string.split(separator);
				Site site=new Site(strings[0]);
				site.setDimension(new Double(strings[1]));
				site.setLongitude(new Double(strings[2]));
				sites.add(site);
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
}
