package com.whpe.yzxManage.whpe.servlet.user;

import com.alibaba.fastjson.JSONObject;
import com.whpe.yzxManage.whpe.bean.user.Right;
import com.whpe.yzxManage.whpe.bean.user.Role;
import com.whpe.yzxManage.whpe.dao.user.IRightDao;
import com.whpe.yzxManage.whpe.dao.user.RightDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class RightServlet
 */
public class RightServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RightServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		String method = request.getParameter("method");
		System.out.println("method==="+method);
		if("addrights".equals(method)){
			addrights(request, response, json);
		}else if("righttree".equals(method)){
			righttree(request, response, json);
			return ;
		}else if("getRights".equals(method)){
			getRights(request, response, json);
			return ;
		}
		String result="";//返回的json串
		result = json.toString();
		response.getWriter().print(result);
	}
	
	public void getRights(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws IOException {
		String gid = request.getParameter("gid");
		IRightDao rightDao = new RightDaoImpl();
		Role role = new Role();
		role.setRole_id(gid);
		List list=rightDao.getRights(role);
		String result = JSONObject.toJSONString(list);
		response.getWriter().println(result);
	}
	
	public void righttree(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws IOException {
		IRightDao rightDao = new RightDaoImpl();
		List list = rightDao.righttree();
		String result = JSONObject.toJSONString(list);
		result=result.replace("\"children\":[],", "");
		response.getWriter().println(result);
	}
	
	public void addrights(HttpServletRequest request, HttpServletResponse response, JSONObject json) {
		String nodevalue = request.getParameter("nodevalue");
		String gid = request.getParameter("gid");
		Role role = new Role();
		role.setRole_id(gid);
		List<Right> rights = new ArrayList<Right>();
		if(nodevalue!=null&&!"".equals(nodevalue)){
			String[] newRights=nodevalue.split(",");
			for(String ggid: newRights){
				Right right = new Right();
				right.setGid(ggid);
				rights.add(right);
			}
		}
		IRightDao rightDao = new RightDaoImpl();
		String[] Msg = {"0"};
		boolean flag=rightDao.addrights(role, rights, Msg);
		json.put("success", flag);
		json.put("Msg", Msg[0]);
	}

}
