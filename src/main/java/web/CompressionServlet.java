package web;

import huffman.HuffmanCoding;
import huffman.HuffmanCountInputStream;
import huffman.HuffmanEncodingOutputStream;
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
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cellargalaxy on 2017/5/28.
 */
public class CompressionServlet extends HttpServlet {
	private String path;
	private String errorPath;
	private String tempPath;
	private String filePath;
	private String errorInfo;
	private String title;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletContext = getServletContext();
		path = config.getInitParameter("path");
		errorPath = config.getInitParameter("errorPath");
		errorInfo = config.getInitParameter("errorInfo");
		title = config.getInitParameter("title");
		tempPath = servletContext.getRealPath("/" + config.getInitParameter("tempPath"));
		filePath = servletContext.getRealPath("/" + config.getInitParameter("filePath"));
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		session.setAttribute("cd", true);
		req.setAttribute("title", title);
		req.getRequestDispatcher(path).forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HuffmanCountInputStream countInputStream = null;
		BufferedOutputStream outputStream = null;
		BufferedInputStream inputStream = null;
		HuffmanEncodingOutputStream encodingOutputStream = null;
		try {
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			diskFileItemFactory.setSizeThreshold(1024 * 1024);
			diskFileItemFactory.setRepository(new File(tempPath));
			ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
//			servletFileUpload.setSizeMax(4 * 1024 * 1024);
			
			List fileItems = servletFileUpload.parseRequest(request);
			Iterator iterator = fileItems.iterator();
			FileItem item = (FileItem) iterator.next();
			
			String filename = item.getName();
			filename = filename.substring(filename.lastIndexOf('\\') + 1, filename.length());
			File uploadFile = new File(filePath + "/" + filename);
			
			countInputStream = new HuffmanCountInputStream(new BufferedInputStream(item.getInputStream()));
			outputStream = new BufferedOutputStream(new FileOutputStream(uploadFile));
			int len;
			byte[] bytes = new byte[1024 * 10];
			while ((len = countInputStream.read(bytes, 0, bytes.length)) != -1) {
				outputStream.write(bytes, 0, len);
			}
			outputStream.flush();
			
			long[] counts = countInputStream.getCounts();
			HuffmanCoding huffmanCoding = new HuffmanCoding(counts, uploadFile.getName());
			String[] coding = huffmanCoding.getCodings();
			long size = 0;
			for (int i = 0; i < counts.length; i++) {
				if (counts[i] > 0 && coding[i] != null) {
					size += counts[i] * coding[i].length();
				}
			}
			HttpSession session = request.getSession();
			Object object = session.getAttribute("compressionFiles");
			LinkedList<FileBean> fileBeans;
			if (object == null) {
				fileBeans = new LinkedList<FileBean>();
			} else {
				fileBeans = (LinkedList) object;
			}
			fileBeans.add(new FileBean(item.getName(), uploadFile.length(), (size / 8)));
			session.setAttribute("compressionFiles", fileBeans);
			
			inputStream = new BufferedInputStream(new FileInputStream(uploadFile));
			encodingOutputStream = new HuffmanEncodingOutputStream(response.getOutputStream(), huffmanCoding);
			ServletSendFile.sendFile(response, inputStream, encodingOutputStream, true, uploadFile.getName() + ".ha");
			
			uploadFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
			
			response.reset();
			response.setContentType("text/plain;charset=utf-8");
			request.setAttribute("error", errorInfo);
			request.getRequestDispatcher(errorPath).forward(request, response);
		} finally {
			try {
				if (countInputStream != null) {
					countInputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (encodingOutputStream != null) {
					encodingOutputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
