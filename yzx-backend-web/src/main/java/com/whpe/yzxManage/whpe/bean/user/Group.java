package com.whpe.yzxManage.whpe.bean.user;

import java.util.List;

/**
 * 用户组
 * @author Yin
 *
 */
public class Group {
	
	private String gid;//组标识
	private String name;//组名称
	private String remark;//组描述
	private List<Role> roles;//组拥有的角色
	private List<User> users;//组拥有的用户
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getName() {
		return name;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
}
