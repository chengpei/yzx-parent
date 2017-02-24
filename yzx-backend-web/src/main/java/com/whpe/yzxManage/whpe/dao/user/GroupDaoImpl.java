package com.whpe.yzxManage.whpe.dao.user;

import com.whpe.yzxManage.Common.DB.DBBase;
import com.whpe.yzxManage.Common.DB.SqlString;
import com.whpe.yzxManage.whpe.bean.user.Group;
import com.whpe.yzxManage.whpe.bean.user.Role;
import com.whpe.yzxManage.whpe.bean.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupDaoImpl implements IGroupDao {

	@Override
	public boolean save(Group group,User user, String[] Msg) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			
			String sql="insert into YZX.SYS_GROUP (group_id,group_name,group_remark,group_createid,group_createdate)values(?,?,?,?,sysdate)";
			conn = new DBBase().GetWriteConn();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, group.getGid());
			pstmt.setString(2, group.getName());
			pstmt.setString(3, group.getRemark());
			pstmt.setString(4, user.getGid());
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
	public boolean update(Group group,User user, String[] Msg) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			
			String sql="update YZX.SYS_GROUP set group_name=?,group_remark=?,group_updateid=?,group_updatedate=sysdate where group_id=?";
			conn = new DBBase().GetWriteConn();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, group.getName());
			pstmt.setString(2, group.getRemark());
			pstmt.setString(3, user.getGid());
			pstmt.setString(4, group.getGid());
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
		
		List<Group> list = new ArrayList<Group>();
		List<String> params = new ArrayList<String>();
		try {
				conn = new DBBase().GetReadConn();
			
				String sql=" select group_id gid, group_name name,group_remark remark from  YZX.SYS_GROUP where 1=1 ";
			
				if(keywords!=null&&keywords.size()!=0){
					if(keywords.containsKey("name")){
						sql+=" and group_name like ? ";
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
				while(rs.next()){
					Group group = new Group();
					group.setGid(rs.getString("gid"));
					group.setName(rs.getString("name"));
					group.setRemark(rs.getString("remark"));
					
					List<Role> roles =new ArrayList<Role>();
					pstmt2=conn.prepareStatement("select r.role_id,r.role_name from YZX.SYS_ROLE r where r.role_id in(select t.rg_roleid from YZX.SYS_ROLE_GROUP t where t.rg_groupid = ?) ");
					pstmt2.setString(1, group.getGid());
					rs2=pstmt2.executeQuery();
					while(rs2.next()){
						Role role =new Role();
						role.setRole_id(rs2.getString("role_id"));
						role.setRole_name(rs2.getString("role_name"));
						roles.add(role);
					}
					DBBase.close(rs2);
					DBBase.close(pstmt2);
					
					group.setRoles(roles);
					
					List<User> users =new ArrayList<User>();
					pstmt2=conn.prepareStatement("select u.u_id,u.u_account from yzx.sys_user u where u.u_id in (select distinct(r.ur_userid) from YZX.SYS_USER_ROLE r where r.ur_roleid in (select t.rg_roleid from YZX.SYS_ROLE_GROUP t where t.rg_groupid = ?))");
					pstmt2.setString(1, group.getGid());
					rs2=pstmt2.executeQuery();
					while(rs2.next()){
						User u =new User();
						u.setGid(rs2.getString("u_id"));
						u.setName(rs2.getString("u_account"));
						users.add(u);
					}
					DBBase.close(rs2);
					DBBase.close(pstmt2);
					
					group.setUsers(users);
					
					list.add(group);
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
	public boolean addRoles(Group group, String[] Msg) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			conn = new DBBase().GetWriteConn();
			DBBase.setAutoCommit(conn, false);//设置手工提交
			
			String sql=" delete from YZX.SYS_ROLE_GROUP where rg_groupid= ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, group.getGid());
			pstmt.executeUpdate();
			
			DBBase.close(pstmt);
			if(group.getRoles()!=null){
				sql=" insert into YZX.SYS_ROLE_GROUP (rg_roleid,rg_groupid) values (?,?)  ";
				pstmt = conn.prepareStatement(sql);
				for(Role u : group.getRoles()){
					pstmt.setString(1, u.getRole_id());
					pstmt.setString(2, group.getGid());
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
	public List rolesOfGroup(Group group, String[] Msg) {
		// TODO Auto-generated method stub
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		List<Role> roles = new ArrayList<Role>();
		try {
			
			conn = new DBBase().GetWriteConn();
			
			String sql=" select role_id,role_name from YZX.SYS_ROLE  "; //查询出所有的角色
			pstmt = conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while(rs.next()){
				Role u = new Role();
				u.setRole_id(rs.getString("role_id"));
				u.setRole_name(rs.getString("role_name"));
				u.setBz("1");
				roles.add(u);
			}
			DBBase.close(rs);
			DBBase.close(pstmt);
			
			sql="select t.rg_roleid from YZX.SYS_ROLE_GROUP t where t.rg_groupid = ?";//查询出已经在组中的角色
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, group.getGid());
			rs=pstmt.executeQuery();
			while(rs.next()){
				for(Role u : roles){
					if(u.getRole_id().equals(rs.getString("rg_roleid"))){
						u.setBz("2");
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
		return roles;
	}

}
