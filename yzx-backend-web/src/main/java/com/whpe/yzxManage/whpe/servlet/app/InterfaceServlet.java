package com.whpe.yzxManage.whpe.servlet.app;

import com.alibaba.fastjson.JSONObject;
import com.whpe.yzxManage.Common.Global;
import com.whpe.yzxManage.Common.Sendsms;
import com.whpe.yzxManage.whpe.bean.app.People;
import com.whpe.yzxManage.whpe.bean.user.User;
import com.whpe.yzxManage.whpe.dao.app.IInterfaceDao;
import com.whpe.yzxManage.whpe.dao.app.InterfaceDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InterfaceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject json = new JSONObject();
		String method = request.getParameter("method");
		if("login".equals(method)){
			login(request, response, json);
		}else if("regist".equals(method)){
			regist(request, response, json);
		}else if("sendcode".equals(method)){
			sendcode(request, response, json);
		}else if("changepwd".equals(method)){
			changepwd(request, response, json);
		}
		String result="";//返回的json串
		result = json.toString();
//		System.out.println(result);
		response.getWriter().print(result);
		
	}
	
	public boolean login(HttpServletRequest request, HttpServletResponse response, JSONObject json){
		String phone = request.getParameter("phone");
		String pwd = request.getParameter("pwd");
		String success="true";
		String msg="登录成功";
		try {
			if(phone==null||"".equals(phone)){
				success="false";
				msg="请输入手机号";
				return false;
			}
			
			if(pwd==null||"".equals(pwd)){
				success="false";
				msg="请输入密码";
				return false;
			}
			
			User user = new User();
			user.setPhone(phone);
			user.setPwd(pwd);
			
			IInterfaceDao userdao = new InterfaceDaoImpl();
			boolean islogin = userdao.login(user);
			if(!islogin){
				success="false";
				msg="用户名密码输入不正确";
				return false;
			}
			json.put("user", user);
			request.getSession().setAttribute("USER", user);
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
	
	public boolean regist(HttpServletRequest request, HttpServletResponse response, JSONObject json){
		String phone = request.getParameter("phone");
		String pwd = request.getParameter("pwd");
		String code = request.getParameter("code");
		String success="true";
		String msg="注册成功";
		try {
			if(phone==null||"".equals(phone)){
				success="false";
				msg="请输入手机号";
				return false;
			}
			
			if(pwd==null||"".equals(pwd)){
				success="false";
				msg="请输入密码";
				return false;
			}
			
			String mobile_code = (String)request.getSession().getAttribute("mobile_code");
			Long mobile_code_time = (Long)request.getSession().getAttribute("mobile_code_time");
			if(mobile_code==null){
				success="false";
				msg="请选获取注册验证码";
				return false;
			}
			Calendar cal = Calendar.getInstance();
			Long xx = cal.getTimeInMillis()-mobile_code_time;
			if(xx>1000*60*20){
				success="false";
				msg="验证码超过20分钟有效时间";
				return false;
			}
			if(!(code!=null&&code.equals(mobile_code))){
				success="false";
				msg="验证码不正确";
				return false;
			}
			
			User user = new User();
			user.setName(phone);
			user.setPhone(phone);
			user.setPwd(pwd);
			user.setGid(UUID.randomUUID().toString());
			user.setSyzt("2");
			IInterfaceDao userdao = new InterfaceDaoImpl();
			
			String[] Msg =  new String[2];
			boolean flag = userdao.saveUser(user,Msg);
			if(!flag){
				success="false";
				msg=Msg[0];
				return false;
			}
			People people = new People();
			people.setAppid(user.getGid());
			people.setPeopleid(UUID.randomUUID().toString());
			people.setNickname(phone);
			flag = userdao.savePeople(people,Msg);
			if(!flag){
				success="false";
				msg=Msg[0];
				return false;
			}
			user.setPeople(people);
			json.put("user", user);
			request.getSession().removeAttribute("mobile_code");
			request.getSession().removeAttribute("mobile_code_time");
			request.getSession().setAttribute("USER", user);
			request.getSession().setAttribute(Global.Session_UserName, user.getName());
			request.getSession().setAttribute(Global.Session_Pwd, user.getPwd());
		} catch (Exception e) {
			success="false";
			msg="注册发生异常";
			Global.PrintExp(msg, e);
			return false;
		}finally{
			json.put("success", success);
			json.put("msg", msg);
		}
		return true;
	}
	
	public boolean sendcode(HttpServletRequest request, HttpServletResponse response, JSONObject json){
		String phone = request.getParameter("phone");
		String type = request.getParameter("type");//1注册，2修改密码
		if(type==null){
			type = "1";
		}
		String success="true";
		String msg="";
		if(phone==null||"".equals(phone)){
			success="false";
			msg="请输入手机号";
			return false;
		}
		try {
			int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);
			Map<String,String> map =new HashMap<String, String>();
			Sendsms.send(phone, mobile_code + "", map, type);
			Calendar cal = Calendar.getInstance();
			if("1".equals(type)){
				request.getSession().setAttribute("mobile_code", mobile_code+"");
				request.getSession().setAttribute("mobile_code_time",cal.getTimeInMillis());
			}else{
				request.getSession().setAttribute("pwd_code", mobile_code+"");
				request.getSession().setAttribute("pwd_code_time",cal.getTimeInMillis());
			}
			json.put("result", map.get("result"));
			json.put("description", map.get("description"));
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
	
	public boolean changepwd(HttpServletRequest request, HttpServletResponse response, JSONObject json){
		String phone = request.getParameter("phone");
		String pwd = request.getParameter("pwd");
		String newpwd = request.getParameter("newpwd");
		String code = request.getParameter("code");
		String success="true";
		String msg="修改成功";
		try {
			if(pwd==null||"".equals(pwd)){
				success="false";
				msg="请输入原始密码";
				return false;
			}
			
			if(newpwd==null||"".equals(newpwd)){
				success="false";
				msg="请输入新密码";
				return false;
			}
			
			String pwd_code = (String)request.getSession().getAttribute("pwd_code");
			Long pwd_code_time = (Long)request.getSession().getAttribute("pwd_code_time");
			if(pwd_code==null){
				success="false";
				msg="修改密码请选获取验证码";
				return false;
			}
			Calendar cal = Calendar.getInstance();
			Long xx = cal.getTimeInMillis()-pwd_code_time;
			if(xx>1000*60*20){
				success="false";
				msg="验证码超过20分钟有效时间";
				return false;
			}
			if(!(code!=null&&code.equals(pwd_code+""))){
				success="false";
				msg="验证码不正确";
				return false;
			}
			
			User user = new User();
			user.setPhone(phone);
			user.setPwd(pwd);
			user.setNewpwd(newpwd);
			IInterfaceDao userdao = new InterfaceDaoImpl();
			
			String[] Msg =  new String[2];
			boolean flag = userdao.changePwd(user,Msg);
			if(!flag){
				success="false";
				msg=Msg[0];
				return false;
			}
			json.put("user",user);
			request.getSession().removeAttribute("pwd_code");
			request.getSession().removeAttribute("pwd_code_time");
			request.getSession().setAttribute("USER", user);
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
	
}
