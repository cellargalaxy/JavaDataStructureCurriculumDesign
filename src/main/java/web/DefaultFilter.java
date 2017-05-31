package web;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by cellargalaxy on 2017/5/30.
 */
public class DefaultFilter implements Filter{
	private String coding;
	private boolean disable;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		coding=filterConfig.getInitParameter("coding");
		String disbaleFilter=filterConfig.getInitParameter("disableFilter");
		if (disbaleFilter.toLowerCase().equals("n")) {
			disable=true;
		}else {
			disable=false;
		}
	}
	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		if (!disable) {
			filterChain.doFilter(servletRequest,servletResponse);
			return;
		}
		
		servletRequest.setCharacterEncoding(coding);
		servletResponse.setCharacterEncoding(coding);
		filterChain.doFilter(servletRequest,servletResponse);
	}
	
	public void destroy() {
	
	}
}
