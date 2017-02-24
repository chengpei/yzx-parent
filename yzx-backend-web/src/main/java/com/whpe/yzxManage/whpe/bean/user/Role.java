package com.whpe.yzxManage.whpe.bean.user;

import java.util.List;

public class Role {
	
	private String role_id;
	private String role_name;
	private String role_createdate;
	private String role_createid;
	private String role_updatedate;
	private String role_updateid;
	private String bz;
	private List<Group> groups;
	private List<User> users;
	private List<Right> rights;
	
	public List<Right> getRights() {
		return rights;
	}
	public void setRights(List<Right> rights) {
		this.rights = rights;
	}
	public String getRole_id() {
		return role_id;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public List<Group> getGroups() {
		return groups;
	}
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	public String getRole_createdate() {
		return role_createdate;
	}
	public void setRole_createdate(String role_createdate) {
		this.role_createdate = role_createdate;
	}
	public String getRole_createid() {
		return role_createid;
	}
	public void setRole_createid(String role_createid) {
		this.role_createid = role_createid;
	}
	public String getRole_updatedate() {
		return role_updatedate;
	}
	public void setRole_updatedate(String role_updatedate) {
		this.role_updatedate = role_updatedate;
	}
	public String getRole_updateid() {
		return role_updateid;
	}
	public void setRole_updateid(String role_updateid) {
		this.role_updateid = role_updateid;
	}
	
}
