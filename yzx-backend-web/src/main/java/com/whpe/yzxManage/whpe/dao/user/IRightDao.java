package com.whpe.yzxManage.whpe.dao.user;

import com.whpe.yzxManage.whpe.bean.user.Right;
import com.whpe.yzxManage.whpe.bean.user.Role;

import java.util.List;

public interface IRightDao {
	List getRights(Role role);
	List righttree();
	boolean addrights(Role role, List<Right> Rights, String[] Msg);
}
