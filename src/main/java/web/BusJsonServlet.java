package web;

import org.json.JSONArray;
import org.json.JSONObject;
import pathSelection.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * Created by cellargalaxy on 2017/6/1.
 */
public class BusJsonServlet extends HttpServlet {
	private String errorPath;
	private String errorInfo;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		errorPath = config.getInitParameter("errorPath");
		errorInfo = config.getInitParameter("errorInfo");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Object object = req.getSession().getAttribute("dataSet");
		DataSet dataSet;
		if (object != null) {
			dataSet = (DataSet) object;
		} else {
			dataSet = BusListener.getDataSet();
		}
		Site[] sites = dataSet.getSites();
		JSONArray nodes = new JSONArray();
		for (int i = 0; i < sites.length; i++) {
			JSONObject jsonObject1 = new JSONObject();
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("id", i);
			jsonObject2.put("name", sites[i].getName());
			jsonObject1.put("data", jsonObject2);
			nodes.put(jsonObject1);
		}
		
		JSONArray edges = new JSONArray();
		for (Site site : sites) {
			for (GoSite goSite : site.getGoSites()) {
				JSONObject jsonObject1 = new JSONObject();
				JSONObject jsonObject2 = new JSONObject();
				jsonObject2.put("source", goSite.getFirst().getId());
				jsonObject2.put("target", goSite.getEnd().getId());
				jsonObject2.put("len", (int) goSite.getLen() + "m");
				
				jsonObject1.put("data", jsonObject2);
				edges.put(jsonObject1);
			}
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("nodes", nodes);
		jsonObject.put("edges", edges);
		
		resp.setContentType("application/json");
		PrintWriter writer = resp.getWriter();
		writer.write(jsonObject.toString());
		writer.close();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String startIdString = req.getParameter("startId");
			String endIdString = req.getParameter("endId");
			int startId = new Integer(startIdString);
			int endId = new Integer(endIdString);
			
			DataSet dataSet;
			Object object = req.getSession().getAttribute("dataSet");
			if (object != null) {
				dataSet = (DataSet) object;
			} else {
				dataSet = BusListener.getCloneDataSet();
			}
			Site[] sites = dataSet.getSites();
			Site startSite = sites[startId];
			Site endSite = sites[endId];
			
			
			sites = Dijkstra.dijkstra(sites, startSite, endSite);
			LinkedList<LinkedList<Site>> paths = Dijkstra.createPaths(endSite);
			
			
			LinkedList<LinkedList<GoBus>> busPaths = Dijkstra.dijkstraBuses(paths, dataSet.getBusRoutes());
			String[] busNames = dataSet.getBusNames();
			JSONArray jsonArray = new JSONArray();
			for (LinkedList<GoBus> busPath : busPaths) {
				JSONArray array = new JSONArray();
				for (GoBus goBus : busPath) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("start", goBus.getStart().getName());
					jsonObject.put("bus", busNames[goBus.getBusId()]);
					jsonObject.put("end", goBus.getEnd().getName());
					array.put(jsonObject);
				}
				jsonArray.put(array);
			}
			
			resp.setContentType("application/json");
			PrintWriter out = resp.getWriter();
			out.println(jsonArray);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			
			resp.reset();
			resp.setContentType("text/plain;charset=utf-8");
			req.setAttribute("error", errorInfo);
			req.getRequestDispatcher(errorPath).forward(req, resp);
		}
	}
}
