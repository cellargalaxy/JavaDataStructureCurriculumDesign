package web;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import pathSelection.DataSet;
import pathSelection.Graph;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cellargalaxy on 2017/6/4.
 */
public class BusServlet extends HttpServlet {
	private String filePath;
	private String tempPath;
	private String path;
	private String errorPath;
	private String errorInfo;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletContext = getServletContext();
		path = config.getInitParameter("path");
		errorPath = config.getInitParameter("errorPath");
		errorInfo = config.getInitParameter("errorInfo");
		tempPath = servletContext.getRealPath("/" + config.getInitParameter("tempPath"));
		filePath = servletContext.getRealPath("/" + config.getInitParameter("filePath"));
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(path).forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			diskFileItemFactory.setSizeThreshold(1024 * 1024);
			diskFileItemFactory.setRepository(new File(tempPath));
			ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
//			servletFileUpload.setSizeMax(4 * 1024 * 1024);
			
			List fileItems = servletFileUpload.parseRequest(req);
			Iterator iterator = fileItems.iterator();
			LinkedList<File> dateSetFiles = new LinkedList<File>();
			while (iterator.hasNext()) {
				FileItem item = (FileItem) iterator.next();
				if (!item.isFormField()) {
					String filename = item.getName();
					filename = filename.substring(filename.lastIndexOf('\\') + 1, filename.length());
					File uploadFile = new File(filePath + "/" + filename + (int) (Math.random() * 100000));
					item.write(uploadFile);
					dateSetFiles.add(uploadFile);
				} else {
//					String desc = item.getString("UTF-8");
//					System.out.println("文件描述：" + desc);
				}
			}
			DataSet dataSet = new DataSet(dateSetFiles, ",", "utf-8");
			dataSet.setSites(Graph.createSitesGraph(dataSet.getSites(), dataSet.getBusRoutes(), dataSet));
			req.getSession().setAttribute("dataSet", dataSet);
			doGet(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
			
			resp.reset();
			resp.setContentType("text/plain;charset=utf-8");
			req.setAttribute("error", errorInfo);
			req.getRequestDispatcher(errorPath).forward(req, resp);
		}
	}
}
