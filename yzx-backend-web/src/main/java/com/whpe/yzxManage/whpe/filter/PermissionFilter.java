package com.whpe.yzxManage.whpe.filter;

import com.whpe.yzxManage.Common.Global;
import com.whpe.yzxManage.whpe.bean.user.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 权限过滤器
 */
@SuppressWarnings("unchecked")
public class PermissionFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", -1);
		response.setHeader("X-UA-Compatible", "IE=EmulateIE7");
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type", "text/html;charset=UTF-8");

		String url = request.getRequestURI().toString().substring(1);
		url = url.substring(url.indexOf("/") + 1);
		//System.out.println("url=="+url);
		// 放行*.css, *.html, *.jpg, *.gif, *.png, *.js, *.htm, *-exist, *-select,
		// *-exportExcel, user/user-login, user/user-logout, json/json
		//randomcoad.jsp
		if (url.endsWith("randomcoad.jsp")||
			url.endsWith("login.jsp")||
			url.endsWith(".css") ||
			url.endsWith(".html") || 
			url.endsWith(".jpg") || 
			url.endsWith(".gif") || 
			url.endsWith(".png") || 
			url.endsWith(".js") || 
			url.endsWith(".htm") || 
			url.endsWith("-exist.action") || 
			url.endsWith("-select.action") || 
			url.endsWith("-exportExcel") || 
			url.endsWith("UserServlet.do") || 
			url.endsWith("user/user-logout.action") || 
			url.endsWith("json/json.action-") || 
			url.endsWith("-getNew.action") || 
			url.endsWith("app.do") || 
			url.equals("")) {
			chain.doFilter(request, response); // 显示页面内容
			return;
		}
		
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

		// 检测用户是否登录
		User user = (User) request.getSession().getAttribute("USER");

		if (user == null) { // 用户登录了

			response.sendRedirect(basePath + "login.jsp"); // 默认jsp页面是可以显示的
			
			return ;
		}
		
		if(url.indexOf("kfop")!=-1){
			
			Object objOpCard=request.getSession().getAttribute(Global.Session_OpCard);
			if(objOpCard==null ||  objOpCard.equals("")){
				response.sendRedirect(basePath + "cardlogin.jsp"); // 默认jsp页面是可以显示的
				return;
			}
		}
		
		chain.doFilter(request, response); // 显示页面内容
	}

	@Override
	public void init(FilterConfig config) throws ServletException {

	}

}
