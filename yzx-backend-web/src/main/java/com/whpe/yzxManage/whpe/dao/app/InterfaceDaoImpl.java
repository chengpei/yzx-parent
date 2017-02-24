package com.whpe.yzxManage.whpe.dao.app;

import com.whpe.yzxManage.Common.DB.DBBase;
import com.whpe.yzxManage.Common.Transition;
import com.whpe.yzxManage.whpe.bean.app.People;
import com.whpe.yzxManage.whpe.bean.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InterfaceDaoImpl implements IInterfaceDao {

	@Override
	public boolean login(User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			conn = new DBBase().GetReadConn();

			pstmt = conn.prepareStatement(" select U_ACCOUNT ,U_PASSWORD ,U_ID,u_phone from YZX.SYS_app_USER where  U_ACCOUNT=? and U_PASSWORD=? and U_ENABLED='2'  ");
			pstmt.setString(1, user.getPhone());
			pstmt.setString(2, Transition.EncrypMD5(user.getPwd()));
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				return false;
			}
			user.setName(rs.getString("U_ACCOUNT"));
			user.setPwd(rs.getString("U_PASSWORD"));
			user.setPhone(rs.getString("u_phone"));
			user.setGid(rs.getString("U_ID"));
			DBBase.close(rs);
			DBBase.close(pstmt);
			
			pstmt = conn.prepareStatement(" select pu_people_id ,pu_app_id ,pu_nickname from YZX.SYS_PEOPLE where  pu_app_id=? ");
			pstmt.setString(1, user.getGid());
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				return false;
			}
			People people = new People();
			people.setAppid(rs.getString("pu_app_id"));
			people.setPeopleid(rs.getString("pu_people_id"));
			people.setNickname(rs.getString("pu_nickname"));
			user.setPeople(people);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			DBBase.close(rs);
			DBBase.close(pstmt);
			DBBase.close(conn);
		}
		return true;
	}

	@Override
	public boolean changePwd(User user, String[] Msg) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			conn = new DBBase().GetReadConn();
			if (user.getNewpwd() == null || "".equals(user.getNewpwd())) {
				Msg[0] = "请输入新密码";
				return false;
			}

			sql = "select U_PASSWORD pwd,U_id from  YZX.SYS_app_USER where U_ACCOUNT = ?  ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getPhone());
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				Msg[0] = "未查询到用户信息";
				return false;
			}

			if (!Transition.EncrypMD5(user.getPwd())
					.equals(rs.getString("pwd"))) {
				Msg[0] = "原始密码不对,请重新输入!";
				return false;
			}
			user.setGid(rs.getString("U_id"));
			DBBase.close(rs);
			DBBase.close(pstmt);

			sql = " update YZX.SYS_app_USER set U_PASSWORD = ?,u_updatedate=sysdate  where U_ID = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, Transition.EncrypMD5(user.getNewpwd()));
			pstmt.setString(2, user.getGid());
			if (pstmt.executeUpdate() <= 0) {
				Msg[0] = "修改密码失败";
			}
			Msg[0] = "修改密码成功！";
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			Msg[0] = "程序异常--";
			return false;
		} finally {
			DBBase.close(rs);
			DBBase.close(pstmt);
			DBBase.close(conn);
		}
	}

	@Override
	public boolean savePeople(People people, String[] Msg) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = new DBBase().GetReadConn();

			String sql = " insert into YZX.SYS_PEOPLE(pu_people_id,pu_app_id,pu_nickname,pu_createdate) values (?,?,?,sysdate) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,people.getPeopleid());
			pstmt.setString(2, people.getAppid());
			pstmt.setString(3, people.getNickname());
			if (pstmt.executeUpdate() <= 0) {
				Msg[0] = "用户基础信息保存失败";
			}
			Msg[0] = "操作成功";
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Msg[0] = "程序异常--";
			return false;
		} finally {
			DBBase.close(rs);
			DBBase.close(pstmt);
			DBBase.close(conn);
		}
	}

	@Override
	public boolean saveUser(User user,String[] Msg) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = new DBBase().GetReadConn();
			String countSql = "select count(*)  from YZX.SYS_app_USER where  u_account=? ";
			pstmt = conn.prepareStatement(countSql);
			user.setPhone(user.getPhone().trim());
			pstmt.setString(1, user.getPhone());
			rs = pstmt.executeQuery();
			rs.next();
			if (rs.getInt(1) > 0) {
				Msg[0] = "该手机号已注册！";
				return false;
			}
			DBBase.close(rs);
			DBBase.close(pstmt);

			String sql = " insert into YZX.SYS_app_USER(u_id,u_account,u_phone,u_password,u_enabled,u_createdate) values (?,?,?,?,?,sysdate) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getGid());
			pstmt.setString(2, user.getPhone());
			pstmt.setString(3, user.getPhone());
			pstmt.setString(4, Transition.EncrypMD5(user.getPwd()));
			pstmt.setString(5, user.getSyzt());
			if (pstmt.executeUpdate() <= 0) {
				Msg[0] = "用户信息保存失败";
			}
			Msg[0] = "操作成功";
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Msg[0] = "程序异常--";
			return false;
		} finally {
			DBBase.close(rs);
			DBBase.close(pstmt);
			DBBase.close(conn);
		}
	}

}
