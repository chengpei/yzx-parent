package com.whpe.yzxManage.whpe.servlet.user;

import com.alibaba.fastjson.JSONObject;
import com.whpe.yzxManage.Common.Global;
import com.whpe.yzxManage.whpe.bean.user.Right;
import com.whpe.yzxManage.whpe.bean.user.User;
import com.whpe.yzxManage.whpe.dao.user.IUserDao;
import com.whpe.yzxManage.whpe.dao.user.UserDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		JSONObject json = new JSONObject();
		String method = request.getParameter("method");
		if("login".equals(method)){
			login(request, response, json);
		}else if("loginout".equals(method)){
			 loginout(request, response, json);
			 return;
		}else if("add".equals(method)){
			add(request, response, json);
		}else if("update".equals(method)){
			update(request, response, json);
		}else if("list".equals(method)){
			list(request, response, json);
		}else if("initpwd".equals(method)){
			initpwd(request, response, json);
		}else if("updatepwd".equals(method)){
			updatepwd(request, response, json);
		}else{
			
		}
		String result="";//返回的json串
		result = json.toString();
//		System.out.println(result);
		response.getWriter().print(result);
	}
	
	public void list(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws IOException {
		IUserDao userDao = new UserDaoImpl();
		String pageStr = request.getParameter("page");
		String rowsStr = request.getParameter("rows");
		int page = Integer.parseInt(pageStr);
		int rows = Integer.parseInt(rowsStr);
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		String name = request.getParameter("name");
		String xm = request.getParameter("xm");
		String[] Msg = new String[2];
		Map<String, String> keywords = new HashMap<String, String>();
		
		if(name!=null&&!"".equals(name)){
			keywords.put("name", name);
		}
		
		if(xm!=null&&!"".equals(xm)){
			keywords.put("xm", xm);
		}
		
		List list = userDao.list(page, rows, sort, order, keywords, null, Msg);
		int total = Integer.parseInt(Msg[0]);
		json.put("total", total);
		json.put("rows", list);
	}
	public void initpwd(HttpServletRequest request, HttpServletResponse response, JSONObject json){
		User user = new User();
		IUserDao userDao = new UserDaoImpl();
		String Msg[] = new String[1];
		String gid = request.getParameter("gid");
		user.setGid(gid);
		user.setNewpwd(Global.InitPassword);
		boolean flag = userDao.initPwd(user, Msg);
		json.put("success", flag);
		json.put("Msg", Msg[0]);
	}
	
	public void updatepwd(HttpServletRequest request, HttpServletResponse response, JSONObject json){
		String pwd = request.getParameter("pwd");
		String newpwd = request.getParameter("newpwd");
		String name = request.getParameter("name");
		String gid = request.getParameter("gid");
		User user = new User();
		IUserDao userDao = new UserDaoImpl();
		String Msg[] = new String[1];
		user.setPwd(pwd);
		user.setName(name);
		user.setNewpwd(newpwd);
		user.setGid(gid);
		boolean flag = userDao.updatePwd(user, Msg);
		json.put("success", flag);
		json.put("Msg", Msg[0]);
	}
	
	public void add(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws IOException {
		String phone = request.getParameter("phone");
		String name = request.getParameter("name");
		String syzt = request.getParameter("syzt");
		User user = new User();
		user.setSyzt(syzt);
		user.setPhone(phone);
		user.setName(name);
		user.setPwd(Global.InitPassword);
		user.setGid(UUID.randomUUID().toString());
		String Msg[] = new String[1];
		IUserDao userdao = new UserDaoImpl();
		boolean flag = userdao.save(user, Msg);
		json.put("Msg", Msg[0]);
		json.put("success", flag);
	}
	
	public void update(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws IOException {
		String gid = request.getParameter("gid");
		String phone = request.getParameter("phone");
		String name = request.getParameter("name");
		String syzt = request.getParameter("syzt");
		User user = new User();
		user.setSyzt(syzt);
		user.setPhone(phone);
		user.setGid(gid);
		user.setName(name);
		String Msg[] = new String[1];
		IUserDao userdao = new UserDaoImpl();
		boolean flag = userdao.update(user, Msg);
		json.put("Msg", Msg[0]);
		json.put("success", flag);
	}
	
	public void loginout(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws IOException {
		request.getSession().invalidate();//清理session
		response.sendRedirect("login.jsp"); // 默认jsp页面是可以显示的
	}
	
	public boolean login(HttpServletRequest request, HttpServletResponse response, JSONObject json){
		String username = request.getParameter("name");
		String pwd = request.getParameter("pwd");
		String authCode = request.getParameter("authCode");
		String success="true";
		String msg="";
		try {
			if(username==null||"".equals(username)){
				success="false";
				msg="请输入用户名";
				return false;
			}
			
			if(pwd==null||"".equals(pwd)){
				success="false";
				msg="请输入密码";
				return false;
			}
			
			if(authCode==null||"".equals(authCode)){
				success="false";
				msg="请输入验证码";
				return false;
			}
			
			String rand = (String)request.getSession().getAttribute("rand");
			
			if(!authCode.equals(rand)){
				success="false";
				msg="验证码输入不正确";
				return false;
			}
			
			User user = new User();
			user.setName(username);
			user.setPwd(pwd);
			
			IUserDao userdao = new UserDaoImpl();
			boolean islogin = userdao.login(user);
			if(!islogin){
				success="false";
				msg="用户名密码输入不正确";
				return false;
			}
			
			Cookie cookie = new Cookie("USERNAME", URLEncoder.encode(user.getName(), "UTF-8"));
			cookie.setMaxAge(60 * 60 * 24 * 30); // a month
			cookie.setPath("/"); // send cookie to client
			response.addCookie(cookie);
			
			ArrayList<String> powerList = new ArrayList<String>();
			for(Right right: user.getRights()){
				powerList.add(right.getTag());
			}
			// 将用户信息写入会话
			request.getSession().setAttribute("USER", user);
			request.getSession().setAttribute("PowerLevel", powerList);
			request.getSession().setAttribute(Global.Session_UserName, user.getName());
			request.getSession().setAttribute(Global.Session_Pwd, user.getPwd());
		} catch (Exception e) {
			success="false";
			msg="登入发生异常";
			Global.PrintExp(msg, e);
			return false;
		}finally{
			json.put("success", success);
			json.put("msg", msg);
		}
		return true;
	}

	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString());
	}
}
