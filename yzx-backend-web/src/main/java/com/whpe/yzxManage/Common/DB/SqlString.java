package com.whpe.yzxManage.Common.DB;

public class SqlString {
	//pageindex从0开始    
	public static String MarkSelect(int PageSize,int pageindex,String SQL,String sort){
		if(PageSize==0){
			return SQL+" order by "+sort;
		}
		pageindex=pageindex-1;
		int top2=PageSize*pageindex+1;		
		String StrSql= String.format("SELECT b.* FROM ( SELECT ROWNUM r,a.* FROM ( %s  ORDER BY %s ) a ) b  WHERE b.r>=%s AND b.r<%s ",
				SQL, sort, top2, top2 + PageSize);
		return StrSql;
	}

	public static String GetRecordCount(String noOrderSql)	{
		String strGetCount = "SELECT COUNT(*)  FROM ( " + noOrderSql + " ) DERIVEDTBL";
		
		return strGetCount;
	}
	
	
	public static String pagingSql(String sql,int page, int rows, String sort, String order){
		if(page==0){
			return sql+" order by "+sort +"  "+order;
		}
		int offset = (page - 1) * rows;
		String pagingSql = "SELECT * FROM (SELECT a.*,ROWNUM RN FROM (" + sql + " ORDER BY " + sort + " " + order + ") a WHERE ROWNUM<=" + (offset + rows) + ") WHERE RN>" + offset;
		return pagingSql;
	}
	
}
