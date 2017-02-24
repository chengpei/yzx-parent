package com.whpe.yzxManage.whpe.dao.user;

import com.whpe.yzxManage.whpe.bean.user.User;

import java.util.List;
import java.util.Map;

public interface IUserDao {
	
	 boolean save(User user, String[] Msg);
	 boolean update(User user, String[] Msg);
	 boolean updatePwd(User user, String[] Msg);
	 List<User> list(int page, int rows, String sort, String order, Map<String, String> keywords, User user, String[] Msg);
	 boolean login(User user);
	 boolean loginout();
	boolean initPwd(User user, String[] msg);
	 
}
