package com.whpe.yzxManage.whpe.dao.user;

import com.whpe.yzxManage.whpe.bean.user.Role;
import com.whpe.yzxManage.whpe.bean.user.User;

import java.util.List;
import java.util.Map;

public interface IRoleDao {
	boolean save(Role role, User user, String[] Msg);
	boolean update(Role role, User user, String[] Msg);
	boolean addUsers(Role role, String[] Msg);
	List list(int page, int rows, String sort, String order, Map<String, String> keywords, String[] Msg);
	List usersOfRole(Role role, String[] msg);
}
