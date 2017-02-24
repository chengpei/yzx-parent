package com.whpe.yzxManage.Common.DB;

import com.whpe.yzxManage.Common.Global;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;


public class DBBase {
	public Connection GetReadConn() {
		try {
			return GetConnByName("webread");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
	}	
	
	public Connection GetWriteConn() {
		try {
			return GetConnByName("webwrite");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
     
	}
	
	public Connection GetConnByName(String name) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Properties props= Global.CongfigMap;
		String ServerAddress=props.getProperty(name+".DBServerAddress");
		String DBPort=props.getProperty(name+".DBServerPort");
	    String OpName=props.getProperty(name+".DBOpName");
	    String OpPwd=props.getProperty(name+".DBOpPwd");
	    String DBName=props.getProperty(name+".DBDBName");
	    String DBType=props.getProperty(name+".DBType");
	    
	    if(DBType.toLowerCase().startsWith("oracle")){
	    	
	    	String Url ="";
			if(DBType.toLowerCase().equals("oracle")){
				Url="jdbc:oracle:thin:@" + ServerAddress + ":" + DBPort + ":" + DBName;
			}else{
				Url="jdbc:oracle:thin:@"
						+"(DESCRIPTION =(ADDRESS_LIST=";
				String[] AddressS=ServerAddress.split("\\|");
				String[] PortS=DBPort.split("\\|");
				
				for(int i=0;i<AddressS.length;i++){
					Url+="(ADDRESS = (PROTOCOL = TCP)(HOST = "+AddressS[i]+")(PORT = "+PortS[i]+"))";
				}				
				//Url+="(ADDRESS = (PROTOCOL = TCP)(HOST = node1)(PORT = 1521))"
				Url+="(LOAD_BALANCE = yes)(FAILOVER = on))"
					+"(CONNECT_DATA =(SERVICE_NAME = "+DBName+")(FAILOVER_MODE = (TYPE = session)(METHOD = BASIC))))";
				//System.out.println(Url);
			}
	    	
//	    	String Url="jdbc:oracle:thin:@"+ServerAddress+":"+DBPort+":"+DBName;		
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			if(OpName.toLowerCase().equals("sys")){
				Properties conProps=new Properties();
				conProps.put("user", OpName); 
		        conProps.put("password", OpPwd); 
		        conProps.put("internal_logon", "sysdba"); 
		        return DriverManager.getConnection(Url, conProps);
			}else{
				return DriverManager.getConnection(Url, OpName, OpPwd);
			}
	    }else{	    	
	    	String Url="jdbc:jtds:sqlserver://"+ServerAddress+":"+DBPort+"/"+DBName;
			Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
			
			return DriverManager.getConnection(Url, OpName, OpPwd);
	    }			
	}
	
	public int UpdateByOneSql(String sql,ArrayList<String> WhereParameter){
		Connection conn=null;
		PreparedStatement pstmt=null;
		int iresult=-1;
		try{
			conn=GetWriteConn();
			pstmt=conn.prepareStatement(sql);			
			if(WhereParameter!=null){
				for(int i=0;i<WhereParameter.size();i++){				
					pstmt.setString(i+1,WhereParameter.get(i));
				}	
			}
			iresult=pstmt.executeUpdate();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				if(pstmt!=null)pstmt.close();
				if(conn!=null)conn.close();
			}catch(Exception e){
			}
		}
		return iresult;
	}
	
	public String GetFirstByOneSql(String sql,ArrayList<String> WhereParameter){
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try{
			conn=GetWriteConn();
			pstmt=conn.prepareStatement(sql);			
			if(WhereParameter!=null){
				for(int i=0;i<WhereParameter.size();i++){				
					pstmt.setString(i+1,WhereParameter.get(i));
				}	
			}
			rs=pstmt.executeQuery();
			if(rs.next())return rs.getString(1);			 
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				if(rs!=null)rs.close();
				if(pstmt!=null)pstmt.close();
				if(conn!=null)conn.close();
			}catch(Exception e){
			}
		}
		return null;		 
	}

	public HashMap<String, Object> SelectOneRowByOneSql(String sql,ArrayList<String> WhereParameter){
		HashMap<String, Object> map=null;
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try{
			conn=GetReadConn();			
			pstmt=conn.prepareStatement(sql);
			if(WhereParameter!=null){
				for(int i=0;i<WhereParameter.size();i++){				
					pstmt.setString(i+1,WhereParameter.get(i));
				}	
			}
			rs= pstmt.executeQuery();
			if(rs.next()){
				map=new HashMap<String, Object>();
				ResultSetMetaData rsmd=rs.getMetaData();
				int icount=rsmd.getColumnCount();				
				
				for(int i=1;i<=icount;i++){					 
					if(rs.getObject(i)==null){
						map.put(rsmd.getColumnName(i).toLowerCase(),"");					
					}else{
						map.put(rsmd.getColumnName(i).toLowerCase(), rs.getObject(i));
					}					
				}
			}			
		}catch(Exception ex){
			map=null;
			ex.printStackTrace();
		}finally{
			try{
				if(rs!=null)rs.close();
				if(pstmt!=null)pstmt.close();
				if(conn!=null)conn.close();
			}catch(Exception e){
			}
		}
		return map;
	}
	
	public static int GetDbConType(Connection cnn) throws SQLException {
		String drivername=cnn.getMetaData().getDriverName();
		/*jTDS Type 4 JDBC Driver for MS SQL Server and Sybase
		 *Oracle JDBC driver
		 * */
		if(drivername.startsWith("jTDS")){
			return 1;
		}else if(drivername.startsWith("Oracle")){
			return 2;
		}else{
			return 0;
		}
	}
	
	public static int ExeUpdate(PreparedStatement ppstmt){
		try{
			return ppstmt.executeUpdate();
		}catch(Exception ex){
			ex.printStackTrace();
			return -1;
		}		
	}
	
	
	public static boolean ExeProcedure(String spname){
		Connection conn=null;
		CallableStatement Cstmt=null;
		try{			
			conn=new DBBase().GetWriteConn();
			Cstmt=conn.prepareCall("{call "+spname+"}");			
			Cstmt.execute();
			return true;

		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}finally{
			try{				
				if(Cstmt!=null)Cstmt.close();
				if(conn!=null)conn.close();
			}catch(Exception e){
			}
		}
	}
	
	public static int ExeProcedurReturnInt(String spname){
		Connection conn=null;
		CallableStatement Cstmt=null;
		try{			
			conn=new DBBase().GetWriteConn();
			Cstmt=conn.prepareCall("{call "+spname+"(?) }");	
			Cstmt.registerOutParameter(1, Types.INTEGER);
			Cstmt.execute();
			return Cstmt.getInt(1);	

		}catch(Exception ex){
			ex.printStackTrace();
			return -1;
		}finally{
			try{				
				if(Cstmt!=null)Cstmt.close();
				if(conn!=null)conn.close();
			}catch(Exception e){
			}
		}
	}
	
	public static void close(Connection conn){
		
		try {
			
			if(conn!=null){
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void close(PreparedStatement pstmt){
		try {
			if(pstmt!=null){
				pstmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rs){
		try {
			if(rs!=null){
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setAutoCommit(Connection conn,boolean isauto){
		try {
			if(conn!=null){
				conn.setAutoCommit(isauto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void commit(Connection conn){
		try {
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void rollback(Connection conn){
		try {
			conn.rollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try{
			//conn=new DBBase().Test();
			conn=new DBBase().GetWriteConn();
			pstmt=conn.prepareStatement("update gprs.t_czjl set flag='3' where gid=?"); 	
			pstmt.setString(1, "A62D2A2ED20544C4A1289A0A9473E46D");		
			if(pstmt.executeUpdate()!=1){
				System.out.println("失败");
				 
			}else{
				System.out.println("ok");
			}
//			rs=pstmt.executeQuery();
//			if(rs.next()){
//				System.out.println(rs.getString(1));
//			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				if(rs!=null)rs.close();
				if(pstmt!=null)pstmt.close();
				if(conn!=null)conn.close();
			}catch(Exception e){
			}
		}
		 
		//new DBBase().GetReadConn();
		 //new DBBase().CheckSqlns("select count(*) from gjic.dw_gs",new ArrayList());
		// System.out.println("OK");
	}
//	switch(idb){
//	case 1:
//		break;
//	case 2:
//		break;
//	}
}
