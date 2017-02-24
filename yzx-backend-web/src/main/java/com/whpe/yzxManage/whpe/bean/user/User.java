package com.whpe.yzxManage.whpe.bean.user;

import com.whpe.yzxManage.whpe.bean.app.People;

import java.util.List;

/**
 * 登入用户信息
 * @author Yin
 *
 */
public class User {
	
	private String gid;//用户标识
	private String name;//用户名称
	private String pwd;//用户密码
	private String newpwd;
	private String phone;//手机号
	private String syzt;//使用状态
	private String bz;//备注 (其他作用)
	private List<Right> rights;//用户拥有的权限集合
	private List<Role> roles;//用户所属的组
	private People people;
	public String getGid() {
		return gid;
	}
	public People getPeople() {
		return people;
	}
	public void setPeople(People people) {
		this.people = people;
	}
	public String getNewpwd() {
		return newpwd;
	}
	public void setNewpwd(String newpwd) {
		this.newpwd = newpwd;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSyzt() {
		return syzt;
	}
	public void setSyzt(String syzt) {
		this.syzt = syzt;
	}
	public List<Right> getRights() {
		return rights;
	}
	public void setRights(List<Right> rights) {
		this.rights = rights;
	}
	
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	
}
