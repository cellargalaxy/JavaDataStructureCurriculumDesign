package web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by cellargalaxy on 2017/5/30.
 */
public class ListFileServlet extends HttpServlet{
	private String path;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		path=config.getInitParameter("path");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(path).forward(req,resp);
	}
}
