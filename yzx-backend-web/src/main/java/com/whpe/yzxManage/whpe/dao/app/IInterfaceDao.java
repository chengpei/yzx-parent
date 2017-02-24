package com.whpe.yzxManage.whpe.dao.app;

import com.whpe.yzxManage.whpe.bean.app.People;
import com.whpe.yzxManage.whpe.bean.user.User;

public interface IInterfaceDao {

	boolean login(User user);

	boolean saveUser(User user, String[] Msg);

	boolean changePwd(User user, String[] msg);

	boolean savePeople(People people, String[] msg);

}
