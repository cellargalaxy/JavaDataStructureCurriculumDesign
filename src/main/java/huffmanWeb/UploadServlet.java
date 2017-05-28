package huffmanWeb;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cellargalaxy on 2017/5/28.
 */
public class UploadServlet extends HttpServlet{
	public static final String UPLOAD_PATH="/huffman.jsp";
	private String tempPath;
	private String filePath;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletContext=getServletContext();
		tempPath=servletContext.getRealPath("/"+config.getInitParameter("tempPath"));
		filePath=servletContext.getRealPath("/"+config.getInitParameter("filePath"));
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(UPLOAD_PATH).forward(req,resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter writer=response.getWriter();
		
		try{
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			diskFileItemFactory.setSizeThreshold(1024 *1024 );
			diskFileItemFactory.setRepository(new File(tempPath));
			ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
			servletFileUpload.setSizeMax(4 * 1024 * 1024);
			List fileItems = servletFileUpload.parseRequest(request);
			Iterator iterator = fileItems.iterator();
			while (iterator.hasNext()){
				FileItem item = (FileItem) iterator.next();
				if (item.isFormField()){
					writer.println(item.getFieldName()+" : "+item.getString());
				}else{
					String filename = item.getName();
					System.out.println("原来文件名："+filename);
					filename = filename.substring(filename.lastIndexOf('\\')+1,filename.length());
					File uploadFile = new File(filePath+"/"+filename);
					InputStream inputStream=item.getInputStream();
					item.write(uploadFile);
					writer.println("Get file:"+ filename);
					writer.println(" filetype: "+item.getContentType());
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		writer.println("Finished uploading files!");
		writer.close();
	}
}
