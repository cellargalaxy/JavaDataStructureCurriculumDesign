package web;

import pathSelection.DataSet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by cellargalaxy on 2017/6/1.
 */
public class BusListener implements ServletContextListener{
	public static DataSet dataSet;
	
	public void contextInitialized(ServletContextEvent servletContextEvent) {
	
	}
	
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
	
	}
}
