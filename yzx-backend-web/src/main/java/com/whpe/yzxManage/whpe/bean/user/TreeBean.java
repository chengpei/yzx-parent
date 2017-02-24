package com.whpe.yzxManage.whpe.bean.user;

import java.util.ArrayList;

public class TreeBean {

	
	private String id;//绑定到节点的标识值
	private String text;//显示的文字
	private String state;//节点状态， 'open' 或 'closed'
	private TreeAttributes attributes = new TreeAttributes();//父节点ID
	private ArrayList<TreeBean> children=new ArrayList<TreeBean>() ;//定义了一些子节点的节点数组
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public TreeAttributes getAttributes() {
		return attributes;
	}
	public void setAttributes(TreeAttributes attributes) {
		this.attributes = attributes;
	}
	public ArrayList<TreeBean> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<TreeBean> children) {
		this.children = children;
	}
	

}
