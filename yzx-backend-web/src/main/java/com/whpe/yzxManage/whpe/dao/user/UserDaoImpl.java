package com.whpe.yzxManage.whpe.dao.user;


import com.whpe.yzxManage.Common.DB.DBBase;
import com.whpe.yzxManage.Common.DB.SqlString;
import com.whpe.yzxManage.Common.Transition;
import com.whpe.yzxManage.whpe.bean.user.Right;
import com.whpe.yzxManage.whpe.bean.user.Role;
import com.whpe.yzxManage.whpe.bean.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDaoImpl implements IUserDao {

	@Override
	public boolean save(User user, String[] Msg) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = new DBBase().GetReadConn();
			String countSql = "select count(*)  from YZX.SYS_USER where u_account=?";
			pstmt = conn.prepareStatement(countSql);
			if (user.getName() != null) {
				user.setName(user.getName().trim());
			}
			pstmt.setString(1, user.getName());
			rs = pstmt.executeQuery();
			rs.next();
			if (rs.getInt(1) > 0) {
				Msg[0] = "该用户名已存在,请重新输入！";
				return false;
			}
			DBBase.close(rs);
			DBBase.close(pstmt);

			String sql = " insert into YZX.SYS_USER(u_id,u_account,u_password,u_phone,u_enabled) values (?,?,?,?,?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getGid());
			pstmt.setString(2, user.getName());
			pstmt.setString(3, Transition.EncrypMD5(user.getPwd()));
			pstmt.setString(4, user.getPhone());
			pstmt.setString(5, user.getSyzt());
			if (pstmt.executeUpdate() <= 0) {
				Msg[0] = "操作失败--影响记录行数为0";
			}
			Msg[0] = "操作成功";
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Msg[0] = "程序异常--" + e.getMessage().replace("\"", "'");
			return false;
		} finally {
			DBBase.close(rs);
			DBBase.close(pstmt);
			DBBase.close(conn);
		}
	}

	@Override
	public boolean update(User user, String[] Msg) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = new DBBase().GetReadConn();
			String sql = " update YZX.SYS_USER set u_phone=?,u_enabled=? where u_id=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getPhone());
			pstmt.setString(2, user.getSyzt());
			pstmt.setString(3, user.getGid());
			if (pstmt.executeUpdate() <= 0) {
				Msg[0] = "操作失败--影响记录行数为0";
			}
			Msg[0] = "操作成功";
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Msg[0] = "程序异常--" + e.getMessage().replace("\"", "'");
			return false;
		} finally {
			DBBase.close(rs);
			DBBase.close(pstmt);
			DBBase.close(conn);
		}
	}

	@Override
	public List<User> list(int page, int rows, String sort, String order,
			Map<String, String> keywords, User user, String[] Msg) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		List<User> list = new ArrayList<User>();
		List<String> params = new ArrayList<String>();
		try {
			conn = new DBBase().GetReadConn();
			String sql = " select U_ID gid, U_ACCOUNT name,U_PHONE phone, U_ENABLED syzt from  YZX.SYS_USER where 1=1  ";
			if (keywords != null && keywords.size() != 0) {
				if (keywords.containsKey("name")) {
					if (keywords.get("name") != null
							&& !keywords.get("name").equals("")) {
						sql += " and U_ACCOUNT = ?";
						params.add(keywords.get("name"));
					}
				}
				if (keywords.containsKey("phone")) {
					if (keywords.get("phone") != null
							&& !keywords.get("phone").equals("")) {
						sql += " and U_PHONE like ? ";
						params.add("%" + keywords.get("phone") + "%");
					}
				}
			}
			// 获取总条数
			pstmt = conn.prepareStatement(SqlString.GetRecordCount(sql
					.toString()));
			for (int i = 0; i < params.size(); i++) {
				pstmt.setString(i + 1, params.get(i));
			}
			rs = pstmt.executeQuery();
			rs.next();
			Msg[0] = rs.getString(1);
			DBBase.close(rs);
			DBBase.close(pstmt);

			pstmt = conn.prepareStatement(SqlString.MarkSelect(rows, page,
					sql.toString(), sort));
			for (int i = 0; i < params.size(); i++) {
				pstmt.setString(i + 1, params.get(i));
			}
			rs = pstmt.executeQuery();
			User u = null;
			Right right = null;
			Role role = null;
			List<Right> rights = null;
			List<Role> roles = null;
			while (rs.next()) {

				u = new User();
				u.setName(rs.getString("name"));
				u.setPhone(rs.getString("phone"));
				u.setSyzt(rs.getString("syzt"));
				u.setGid(rs.getString("gid"));

				rights = new ArrayList<Right>();
				pstmt2 = conn
						.prepareStatement("select distinct m.model_name from YZX.SYS_USER t left join yzx.sys_user_role r on t.u_id=r.ur_userid left join YZX.SYS_ROLE_MODEL rm on r.ur_roleid=rm.rm_roleid"
								+ "  left join yzx.sys_model m on rm.rm_modelid=m.model_id where t.u_id = ?");
				pstmt2.setString(1, u.getGid());
				rs2 = pstmt2.executeQuery();
				while (rs2.next()) {
					right = new Right();
					right.setName(rs2.getString("model_name"));
					rights.add(right);
				}
				u.setRights(rights);
				DBBase.close(rs2);
				DBBase.close(pstmt2);

				roles = new ArrayList<Role>();
				pstmt2 = conn
						.prepareStatement("select distinct o.role_name from YZX.SYS_USER t left join yzx.sys_user_role r on t.u_id=r.ur_userid left join yzx.sys_role o on r.ur_roleid=o.role_id  where t.u_id = ? ");
				pstmt2.setString(1, u.getGid());
				rs2 = pstmt2.executeQuery();
				while (rs2.next()) {
					role = new Role();
					role.setRole_name(rs2.getString("role_name"));
					roles.add(role);
				}
				u.setRoles(roles);
				DBBase.close(rs2);
				DBBase.close(pstmt2);

				list.add(u);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBBase.close(rs);
			DBBase.close(pstmt);
			DBBase.close(rs2);
			DBBase.close(pstmt2);
			DBBase.close(conn);
		}
		return list;
	}

	@Override
	public boolean login(User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			conn = new DBBase().GetReadConn();

			pstmt = conn
					.prepareStatement(" select U_ACCOUNT ,U_PASSWORD ,U_ID,u_phone from YZX.SYS_USER where U_ACCOUNT=? and U_PASSWORD=? and U_ENABLED='2' ");
			pstmt.setString(1, user.getName());
			pstmt.setString(2, Transition.EncrypMD5(user.getPwd()));
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				return false;
			}
			user.setName(rs.getString("U_ACCOUNT"));
			user.setPwd(rs.getString("U_PASSWORD"));
			user.setGid(rs.getString("U_ID"));
			DBBase.close(rs);
			DBBase.close(pstmt);

			List<Right> rights = new ArrayList<Right>();
			// 查找用户的所有权限
			pstmt = conn
					.prepareStatement(" select distinct m.model_code from YZX.SYS_USER t left join yzx.sys_user_role r on t.u_id=r.ur_userid left join YZX.SYS_ROLE_MODEL rm on r.ur_roleid=rm.rm_roleid"
							+ "  left join yzx.sys_model m on rm.rm_modelid=m.model_id where t.u_id = ?  ");
			pstmt.setString(1, user.getGid());
			rs = pstmt.executeQuery();
			Right right = null;
			while (rs.next()) {
				right = new Right();
				right.setTag(rs.getString("model_code"));
				rights.add(right);
			}
			user.setRights(rights);

			DBBase.close(rs);
			DBBase.close(pstmt);

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
	public boolean loginout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean initPwd(User user, String[] Msg) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			conn = new DBBase().GetReadConn();
			sql = "select U_PASSWORD pwd from  YZX.SYS_USER where U_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getGid());
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				Msg[0] = "未发现修改的用户,请联系管理员";
				return false;
			}

			DBBase.close(rs);
			DBBase.close(pstmt);

			sql = " update YZX.SYS_USER set U_PASSWORD = ?  where U_ID = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, Transition.EncrypMD5(user.getNewpwd()));
			pstmt.setString(2, user.getGid());
			if (pstmt.executeUpdate() <= 0) {
				Msg[0] = "操作失败--影响记录行数为0";
			}
			Msg[0] = "修改密码成功！";
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			Msg[0] = "程序异常--" + e.getMessage().replace("\"", "'");
			return false;
		} finally {
			DBBase.close(rs);
			DBBase.close(pstmt);
			DBBase.close(conn);
		}
	}

	@Override
	public boolean updatePwd(User user, String[] Msg) {
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

			sql = "select U_PASSWORD pwd from  YZX.SYS_USER where U_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getGid());
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				Msg[0] = "未发现修改的用户,请联系管理员";
				return false;
			}

			if (!Transition.EncrypMD5(user.getPwd())
					.equals(rs.getString("pwd"))) {
				Msg[0] = "原始密码不对,请重新输入!";
				return false;
			}
			DBBase.close(rs);
			DBBase.close(pstmt);

			sql = " update YZX.SYS_USER set U_PASSWORD = ?  where U_ID = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, Transition.EncrypMD5(user.getNewpwd()));
			pstmt.setString(2, user.getGid());
			if (pstmt.executeUpdate() <= 0) {
				Msg[0] = "操作失败--影响记录行数为0";
			}
			Msg[0] = "修改密码成功！";
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			Msg[0] = "程序异常--" + e.getMessage().replace("\"", "'");
			return false;
		} finally {
			DBBase.close(rs);
			DBBase.close(pstmt);
			DBBase.close(conn);
		}
	}
}
