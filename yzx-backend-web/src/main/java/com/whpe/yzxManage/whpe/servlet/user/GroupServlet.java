package com.whpe.yzxManage.whpe.servlet.user;

import com.alibaba.fastjson.JSONObject;
import com.whpe.yzxManage.whpe.bean.user.Group;
import com.whpe.yzxManage.whpe.bean.user.Role;
import com.whpe.yzxManage.whpe.bean.user.User;
import com.whpe.yzxManage.whpe.dao.user.GroupDaoImpl;
import com.whpe.yzxManage.whpe.dao.user.IGroupDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Servlet implementation class UserServlet
 */
public class GroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupServlet() {
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
		}else if("addRoles".equals(method)){
			addRoles(request, response, json);
		}else if("rolesOfGroup".equals(method)){
			rolesOfGroup(request, response, json);
		}else{
			
		}
		String result="";//返回的json串
		result = json.toString();
		//System.out.println(result);
		response.getWriter().print(result);
	}
	
	public void list(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws IOException {
		IGroupDao groupDao = new GroupDaoImpl();
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
		List list = groupDao.list(page, rows, sort, order, keywords, Msg);
		int total = Integer.parseInt(Msg[0]);
		json.put("total", total);
		json.put("rows", list);
	}
	
	
	public void add(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws IOException {
		String name = request.getParameter("name");
		String remark = request.getParameter("remark");
		User user = (User)request.getSession().getAttribute("USER");
		Group group = new Group();
		group.setGid(UUID.randomUUID().toString());
		group.setName(name);
		group.setRemark(remark);
		String Msg[] = new String[1];
		IGroupDao groupDao = new GroupDaoImpl();
		boolean flag = groupDao.save(group,user, Msg);
		json.put("Msg", Msg[0]);
		json.put("success", flag);
	}
	
	public void update(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws IOException {
		String name = request.getParameter("name");
		String gid = request.getParameter("gid");
		String remark = request.getParameter("remark");
		User user = (User)request.getSession().getAttribute("USER");
		Group group = new Group();
		group.setGid(gid);
		group.setName(name);
		group.setRemark(remark);
		String Msg[] = new String[1];
		IGroupDao groupDao = new GroupDaoImpl();
		boolean flag = groupDao.update(group,user, Msg);
		json.put("Msg", Msg[0]);
		json.put("success", flag);
	}
	
	public void addRoles(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws IOException {
		String gid = request.getParameter("gid");
		Group group = new Group();
		group.setGid(gid);
		String[] rolesgid = request.getParameterValues("rolesgid");
		List<Role> roleList = new ArrayList<Role>();
		for(String ugid:rolesgid){
			Role r = new Role();
			r.setRole_id(ugid);
			roleList.add(r);
		}
		group.setRoles(roleList);
		String Msg[] = new String[1];
		IGroupDao groupDao = new GroupDaoImpl();
		boolean flag= groupDao.addRoles(group, Msg);
		json.put("Msg", Msg[0]);
		json.put("success", flag);
	}
	
	public void rolesOfGroup(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws IOException {

		String gid = request.getParameter("gid");
		Group group = new Group();
		group.setGid(gid);
		String Msg[] = new String[1];
		IGroupDao groupDao = new GroupDaoImpl();
		List list = groupDao.rolesOfGroup(group, Msg);
		json.put("rows", list);
	}
	
}
