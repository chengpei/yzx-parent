package com.whpe.yzxManage.whpe.bean.user;

/**
 * 用户权限
 * @author Yin
 *
 */
public class Right {
	
	private String gid;//权限标识
	private String name;//权限名称
	private String tag;//权限标签
	private String type;//权限类型
	private String code;//权限代码
	private String father;//权限父节点代码
	
	public String getGid() {
		return gid;
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
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getFather() {
		return father;
	}
	public void setFather(String father) {
		this.father = father;
	}
	
}
