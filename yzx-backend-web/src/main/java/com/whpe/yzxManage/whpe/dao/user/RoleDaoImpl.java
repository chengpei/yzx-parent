package com.whpe.yzxManage.whpe.dao.user;

import com.whpe.yzxManage.Common.DB.DBBase;
import com.whpe.yzxManage.Common.DB.SqlString;
import com.whpe.yzxManage.whpe.bean.user.Group;
import com.whpe.yzxManage.whpe.bean.user.Right;
import com.whpe.yzxManage.whpe.bean.user.Role;
import com.whpe.yzxManage.whpe.bean.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoleDaoImpl implements IRoleDao {

	@Override
	public boolean save(Role role,User user, String[] Msg) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			
			String sql="insert into YZX.SYS_ROLE (role_id,role_name,role_createid,role_updatedate)values(?,?,?,sysdate)";
			conn = new DBBase().GetWriteConn();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, role.getRole_id());
			pstmt.setString(2, role.getRole_name());
			pstmt.setString(3, user.getGid());
			if(pstmt.executeUpdate()<=0){
				Msg[0]="操作错误--影响行数为0";
				return false;
			}
			Msg[0]="操作成功";
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Msg[0]="程序异常--"+e.getMessage().replace("\"", "'").replace("\n", "");
			return false;
		}finally{
			DBBase.close(pstmt);
			DBBase.close(conn);
		}
	}

	@Override
	public boolean update(Role role,User user, String[] Msg) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			
			String sql="update  YZX.SYS_ROLE set role_name=?,role_updateid=?,role_updatedate=sysdate where role_id=?";
			conn = new DBBase().GetWriteConn();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, role.getRole_name());
			pstmt.setString(2, user.getGid());
			pstmt.setString(3, role.getRole_id());
			if(pstmt.executeUpdate()<=0){
				Msg[0]="操作错误--影响行数为0";
				return false;
			}
			Msg[0]="操作成功";
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Msg[0]="程序异常--"+e.getMessage().replace("\"", "'").replace("\n", "");
			return false;
		}finally{
			DBBase.close(pstmt);
			DBBase.close(conn);
		}
	}

	@Override
	public List list(int page, int rows, String sort, String order,
			Map<String, String> keywords, String[] Msg) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		PreparedStatement pstmt2=null;
		ResultSet rs2=null;
		
		List<Role> list = new ArrayList<Role>();
		List<String> params = new ArrayList<String>();
		try {
				conn = new DBBase().GetReadConn();
			
				String sql=" select role_id , role_name  from  YZX.SYS_ROLE where 1=1 ";
			
				if(keywords!=null&&keywords.size()!=0){
					if(keywords.containsKey("name")){
						sql+=" and role_name like ? ";
						params.add("%"+keywords.get("name")+"%");
					}
				}
				//获取总条数
				pstmt=conn.prepareStatement(SqlString.GetRecordCount(sql.toString()));
				for(int i=0;i<params.size();i++){
					pstmt.setString(i+1, params.get(i));
				}
				rs=pstmt.executeQuery();
				rs.next();
				Msg[0]=rs.getString(1);
				DBBase.close(rs);
				DBBase.close(pstmt);
				
				pstmt=conn.prepareStatement(SqlString.MarkSelect(rows, page, sql.toString(), sort ));
				for(int i=0;i<params.size();i++){
					pstmt.setString(i+1, params.get(i));
				}
				rs=pstmt.executeQuery();
				List<User> users =null;
				List<Group> groups =null;
				List<Right> rights =null;
				Right right = null;
				Role role = null;
				Group g =null;
				User u = null;
				while(rs.next()){
					role = new Role();
					role.setRole_id(rs.getString("role_id"));
					role.setRole_name(rs.getString("role_name"));
					
					 users =new ArrayList<User>();
					 groups =new ArrayList<Group>();
					 rights =new ArrayList<Right>();
					pstmt2=conn.prepareStatement("select distinct m.model_name from YZX.SYS_ROLE_MODEL t left join  YZX.SYS_MODEL m on t.rm_modelid = m.model_id where t.rm_roleid = ?");
					pstmt2.setString(1,role.getRole_id());
					rs2=pstmt2.executeQuery();
					while(rs2.next()){
						 right =new Right();
						right.setName(rs2.getString("model_name"));
						rights.add(right);
					}
					DBBase.close(rs2);
					DBBase.close(pstmt2);
					role.setRights(rights);
					
					pstmt2=conn.prepareStatement("select distinct g.group_name from YZX.SYS_ROLE_GROUP t left join YZX.SYS_GROUP g on t.rg_groupid=g.group_id where t.rg_roleid = ?");
					pstmt2.setString(1,role.getRole_id());
					rs2=pstmt2.executeQuery();
					while(rs2.next()){
						 g =new Group();
						g.setName(rs2.getString("group_name"));
						groups.add(g);
					}
					DBBase.close(rs2);
					DBBase.close(pstmt2);
					
					role.setGroups(groups);
					
					pstmt2=conn.prepareStatement("select distinct u.u_account from YZX.SYS_USER_ROLE r left join yzx.sys_user u on r.ur_userid = u.u_id where r.ur_roleid = ? ");
					pstmt2.setString(1, role.getRole_id());
					rs2=pstmt2.executeQuery();
					while(rs2.next()){
						 u = new User();
						u.setName(rs2.getString("u_account"));
						users.add(u);
					}
					DBBase.close(rs2);
					DBBase.close(pstmt2);
					role.setUsers(users);
					
					list.add(role);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBBase.close(rs);
			DBBase.close(pstmt);
			DBBase.close(conn);
		}
		return list;
	}

	@Override
	public boolean addUsers(Role role, String[] Msg) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			conn = new DBBase().GetWriteConn();
			DBBase.setAutoCommit(conn, false);//设置手工提交
			
			String sql=" delete from YZX.SYS_USER_ROLE t where t.ur_roleid = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, role.getRole_id());
			pstmt.executeUpdate();
			
			DBBase.close(pstmt);
			if(role.getUsers()!=null){
				sql=" insert into YZX.SYS_USER_ROLE (ur_userid,ur_roleid) values (?,?)  ";
				pstmt = conn.prepareStatement(sql);
				for(User u : role.getUsers()){
					pstmt.setString(1, u.getGid());
					pstmt.setString(2, role.getRole_id());
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			}
			Msg[0]="操作成功";
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			DBBase.rollback(conn);
			Msg[0]="程序异常--"+e.getMessage().replace("\"", "'").replace("\n", "");
			return false;
		}finally{
			DBBase.setAutoCommit(conn, true);
			DBBase.close(rs);
			DBBase.close(pstmt);
			DBBase.close(conn);
		}
	}

	@Override
	public List usersOfRole(Role role, String[] Msg) {
		// TODO Auto-generated method stub
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		List<User> users = new ArrayList<User>();
		try {
			
			conn = new DBBase().GetWriteConn();
			
			String sql=" select t.u_id,t.u_account from YZX.SYS_USER t "; //查询出所有的用户
			pstmt = conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while(rs.next()){
				User u = new User();
				u.setGid(rs.getString("u_id"));
				u.setName(rs.getString("u_account"));
				u.setBz("1");
				users.add(u);
			}
			DBBase.close(rs);
			DBBase.close(pstmt);
			
			sql="select t.ur_userid from YZX.SYS_USER_ROLE t where t.ur_roleid = ?";//查询出已经在组中的成员
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, role.getRole_id());
			rs=pstmt.executeQuery();
			while(rs.next()){
				for(User use : users){
					if(use.getGid().equals(rs.getString("ur_userid"))){
						use.setBz("2");
						break;
					}
				}
			}
			DBBase.close(rs);
			DBBase.close(pstmt);
			
		} catch (Exception e) {
			e.printStackTrace();
			DBBase.rollback(conn);
			Msg[0]="程序异常--"+e.getMessage().replace("\"", "'").replace("\n", "");
			return null;
		}finally{
			DBBase.setAutoCommit(conn, true);
			DBBase.close(rs);
			DBBase.close(pstmt);
			DBBase.close(conn);
		}
		return users;
	}

}
