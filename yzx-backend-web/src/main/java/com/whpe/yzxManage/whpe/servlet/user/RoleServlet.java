package com.whpe.yzxManage.whpe.servlet.user;

import com.alibaba.fastjson.JSONObject;
import com.whpe.yzxManage.whpe.bean.user.Role;
import com.whpe.yzxManage.whpe.bean.user.User;
import com.whpe.yzxManage.whpe.dao.user.IRoleDao;
import com.whpe.yzxManage.whpe.dao.user.RoleDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Servlet implementation class UserServlet
 */
public class RoleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RoleServlet() {
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
		if("add".equals(method)){
			add(request, response, json);
		}else if("update".equals(method)){
			update(request, response, json);
		}else if("list".equals(method)){
			list(request, response, json);
		}else if("addUsers".equals(method)){
			addUsers(request, response, json);
		}else if("usersOfRole".equals(method)){
			usersOfRole(request, response, json);
		}else{
			
		}
		String result="";//返回的json串
		result = json.toString();
		//System.out.println(result);
		response.getWriter().print(result);
	}
	
	public void list(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws IOException {
		IRoleDao roleDao = new RoleDaoImpl();
		String pageStr = request.getParameter("page");
		String rowsStr = request.getParameter("rows");
		int page = Integer.parseInt(pageStr);
		int rows = Integer.parseInt(rowsStr);
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		String name = request.getParameter("name");
		String[] Msg = new String[2];
		Map<String, String> keywords = new HashMap<String, String>();
		if(name!=null&&!"".equals(name)){
			keywords.put("name", name);
		}
		List list = roleDao.list(page, rows, sort, order, keywords, Msg);
		int total = Integer.parseInt(Msg[0]);
		json.put("total", total);
		json.put("rows", list);
	}
	
	
	public void add(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws IOException {
		User user = (User)request.getSession().getAttribute("USER");
		String name = request.getParameter("name");
		Role role = new Role();
		role.setRole_id(UUID.randomUUID().toString());
		role.setRole_name(name);
		String Msg[] = new String[1];
		IRoleDao roleDao = new RoleDaoImpl();
		boolean flag = roleDao.save(role,user, Msg);
		json.put("Msg", Msg[0]);
		json.put("success", flag);
	}
	
	public void update(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws IOException {
		String name = request.getParameter("name");
		String gid = request.getParameter("gid");
		User user = (User)request.getSession().getAttribute("USER");
		Role role = new Role();
		role.setRole_name(name);
		role.setRole_id(gid);
		String Msg[] = new String[1];
		IRoleDao roleDao = new RoleDaoImpl();
		boolean flag = roleDao.update(role,user, Msg);
		json.put("Msg", Msg[0]);
		json.put("success", flag);
	}
	
	public void addUsers(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws IOException {
		String gid = request.getParameter("gid");
		Role role = new Role();
		role.setRole_id(gid);
		String[] usersgid = request.getParameterValues("usersgid");
		List<User> userList = new ArrayList<User>();
		for(String ugid:usersgid){
			User user = new User();
			user.setGid(ugid);
			userList.add(user);
		}
		role.setUsers(userList);
		String Msg[] = new String[1];
		IRoleDao roleDao = new RoleDaoImpl();
		boolean flag= roleDao.addUsers(role, Msg);
		json.put("Msg", Msg[0]);
		json.put("success", flag);
	}
	
	public void usersOfRole(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws IOException {

		String gid = request.getParameter("gid");
		Role role = new Role();
		role.setRole_id(gid);
		String Msg[] = new String[1];
		IRoleDao roleDao = new RoleDaoImpl();
		List list = roleDao.usersOfRole(role, Msg);
		json.put("rows", list);
	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString());
	}
}
