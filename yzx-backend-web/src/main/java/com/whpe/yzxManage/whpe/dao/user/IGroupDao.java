package com.whpe.yzxManage.whpe.dao.user;

import com.whpe.yzxManage.whpe.bean.user.Group;
import com.whpe.yzxManage.whpe.bean.user.User;

import java.util.List;
import java.util.Map;

public interface IGroupDao {
	boolean save(Group group, User user, String[] Msg);
	boolean update(Group group, User user, String[] Msg);
	boolean addRoles(Group group, String[] Msg);
	List rolesOfGroup(Group group, String[] Msg);
	List list(int page, int rows, String sort, String order, Map<String, String> keywords, String[] Msg);
}
