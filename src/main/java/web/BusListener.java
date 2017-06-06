package web;

import pathSelection.*;
import util.CloneObject;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by cellargalaxy on 2017/6/1.
 */
public class BusListener implements ServletContextListener {
	private String rootPath;
	private static DataSet dataSet;
	
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			rootPath = servletContextEvent.getServletContext().getRealPath(File.separator);
			File[] dateSetFiles = {new File(rootPath + File.separator + "WEB-INF/classes/11路.csv"), new File(rootPath + File.separator + "WEB-INF/classes/30路.csv"),
					new File(rootPath + File.separator + "WEB-INF/classes/39路.csv"), new File(rootPath + File.separator + "WEB-INF/classes/54路.csv"),
					new File(rootPath + File.separator + "WEB-INF/classes/252路.csv"), new File(rootPath + File.separator + "WEB-INF/classes/468路.csv")};
			dataSet = new DataSet(dateSetFiles, ",", "utf-8");
			dataSet.setSites(Graph.createSitesGraph(dataSet));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
	
	}
	
	public static DataSet getCloneDataSet() throws IOException, ClassNotFoundException {
		return CloneObject.clone(dataSet);
	}
	
	public static DataSet getDataSet() {
		return dataSet;
	}
	
}
