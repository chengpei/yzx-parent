package com.whpe.yzxManage.whpe.dao.user;

import com.whpe.yzxManage.Common.DB.DBBase;
import com.whpe.yzxManage.whpe.bean.user.Right;
import com.whpe.yzxManage.whpe.bean.user.Role;
import com.whpe.yzxManage.whpe.bean.user.TreeBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RightDaoImpl implements IRightDao {

	@Override
	public List getRights(Role role) {
		List<Right> rights = new ArrayList<Right>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			conn = new DBBase().GetReadConn();
			pstmt = conn
					.prepareStatement("select t.rm_modelid from YZX.SYS_ROLE_MODEL t where t.rm_roleid =?");
			pstmt.setString(1, role.getRole_id());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Right right = new Right();
				right.setGid(rs.getString("rm_modelid"));
				rights.add(right);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBBase.close(rs);
			DBBase.close(pstmt);
			DBBase.close(conn);
		}
		return rights;
	}

	@Override
	public List righttree() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TreeBean> trees = new ArrayList<TreeBean>();

		TreeBean root = new TreeBean();// 根节点
		root.setId("qxfp");
		root.setText("权限分配");
		root.setState("open");
		trees.add(root);

		try {

			// 将权限的所有结果全部读入mapNode中存在使用code 做为key
			Map<String, TreeBean> mapNode = new LinkedHashMap<String, TreeBean>();// 存储所有的节点

			conn = new DBBase().GetReadConn();

			String sql = " select t.model_id,t.model_code,t.model_name,t.model_parentid from YZX.SYS_MODEL t  ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				String bh = rs.getString("model_id");
				String code = rs.getString("model_code").trim();
				String fatherCode = rs.getString("model_parentid");
				String txt_name = rs.getString("model_name").trim();

				TreeBean node = new TreeBean();// 节点
				node.setId(bh);
				node.getAttributes().setCode(code);
				node.getAttributes().setFather(fatherCode);
				node.setText(txt_name);

				mapNode.put(bh, node);
			}

			// state: 节点状态，有两个值 'open' or 'closed', 默认为'open'.
			// 当为‘closed’时说明此节点下有子节点否则此节点为叶子节点
			for (String code : mapNode.keySet()) {
				// 忽略父节点是0的节点
				String father = mapNode.get(code).getAttributes().getFather();// 得到父节点编号
				if (father != null && !"".equals(father)) {
					if (mapNode.get(father) != null) {
						mapNode.get(father).setState("closed");
						mapNode.get(father).getChildren()
								.add(mapNode.get(code));// 将子节点放入 父节点的节点集合中
					}
				} else {
					if (mapNode.get(code).getChildren().size() > 0) {
						mapNode.get(code).setState("closed");
					}
					root.getChildren().add(mapNode.get(code));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBBase.close(rs);
			DBBase.close(pstmt);
			DBBase.close(conn);
		}
		return trees;
	}

	@Override
	public boolean addrights(Role role, List<Right> Rights, String[] Msg) {

		boolean flag = true;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = new DBBase().GetReadConn();

			DBBase.setAutoCommit(conn, false);// 设置手动提交

			pstmt = conn.prepareStatement("delete from  YZX.SYS_ROLE_MODEL where rm_roleid=? ");
			pstmt.setString(1, role.getRole_id());
			pstmt.executeUpdate();
			pstmt.close();

			if (Rights.size() > 0) {
				pstmt = conn.prepareStatement("insert into YZX.SYS_ROLE_MODEL(rm_roleid ,rm_modelid) VALUES (?,?) ");
				for (Right right : Rights) {
					pstmt.setString(1, role.getRole_id());
					pstmt.setString(2, right.getGid());
					pstmt.addBatch();
				}
				pstmt.executeBatch();// 批量提交
				DBBase.close(pstmt);
			}
			DBBase.commit(conn);// 手动提交

			Msg[0] = "权限分配成功";

		} catch (Exception e) {
			e.printStackTrace();
			Msg[0] = "权限分配失败  "
					+ e.getMessage().replace("\"", "'").replace("\n", "");
			DBBase.rollback(conn);
		} finally {
			DBBase.setAutoCommit(conn, true);
			DBBase.close(pstmt);
			DBBase.close(conn);
		}
		return flag;

	}

}
