package web;

import huffman.HuffmanDecodingInputStream;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import util.ServletSendFile;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cellargalaxy on 2017/5/31.
 */
public class DecompressionServlet extends HttpServlet{
	private String path;
	private String errorPath;
	private String tempPath;
	private String filePath;
	private String errorInfo;
	private String title;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		title=config.getInitParameter("title");
		path=config.getInitParameter("path");
		errorPath=config.getInitParameter("errorPath");
		errorInfo=config.getInitParameter("errorInfo");
		ServletContext servletContext=getServletContext();
		tempPath=servletContext.getRealPath("/"+config.getInitParameter("tempPath"));
		filePath=servletContext.getRealPath("/"+config.getInitParameter("filePath"));
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		session.setAttribute("cd",false);
		req.setAttribute("title",title);
		req.getRequestDispatcher(path).forward(req,resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HuffmanDecodingInputStream decodingInputStream = null;
		BufferedOutputStream outputStream=null;
		try {
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			diskFileItemFactory.setSizeThreshold(1024 *1024);
			diskFileItemFactory.setRepository(new File(tempPath));
			ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
//			servletFileUpload.setSizeMax(4 * 1024 * 1024);
			
			List fileItems = servletFileUpload.parseRequest(request);
			Iterator iterator = fileItems.iterator();
			FileItem item = (FileItem) iterator.next();
			
			decodingInputStream=new HuffmanDecodingInputStream(new BufferedInputStream(item.getInputStream()));
			outputStream=new BufferedOutputStream(response.getOutputStream());
			ServletSendFile.sendFile(response,decodingInputStream,outputStream,true,decodingInputStream.getFileName());
		}catch (Exception e){
			e.printStackTrace();
			
			response.reset();
			response.setContentType("text/plain;charset=utf-8");
			request.setAttribute("error",errorInfo);
			request.getRequestDispatcher(errorPath).forward(request,response);
		}finally {
			try {
				if (decodingInputStream!=null) {
					decodingInputStream.close();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			try {
				if (outputStream!=null) {
					outputStream.close();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
