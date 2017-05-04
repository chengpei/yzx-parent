//package com.unionpay.acp.demo;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.unionpay.acp.sdk.LogUtil;
//import com.unionpay.acp.sdk.SDKConfig;
//import com.unionpay.acp.sdk.SDKConstants;
//import com.unionpay.acp.sdk.SDKUtil;
//
//
///**
// * 名称：商户后台通知类
// * 功能：
// * 类属性：
// * 方法调用 版本：5.0
// * 日期：2014-07
// * 作者：中国银联ACP团队
// * 版权：中国银联
// * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
// * */
//
//public class BackRcvResponse extends HttpServlet{
//
///**
//	 *
//	 */
//	private static final long serialVersionUID = 3414800502432002480L;
//
//	@Override
//	public void init() throws ServletException {
//		/**
//		 * 参数初始化
//		 * 在java main 方式运行时必须每次都执行加载
//		 * 如果是在web应用开发里,这个方写在可使用监听的方式写入缓存,无须在这出现
//		 */
//		//SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件
//		super.init();
//	}
//
//	@Override
//	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//			throws ServletException, IOException {
//
//		LogUtil.writeLog("BackRcvResponse接收后台通知开始");
//
//		req.setCharacterEncoding("ISO-8859-1");
//		String encoding = req.getParameter(SDKConstants.param_encoding);
//		// 获取请求参数中所有的信息
//		Map<String, String> reqParam = getAllRequestParam(req);
//		// 打印请求报文
//		LogUtil.printRequestLog(reqParam);
//
//		Map<String, String> valideData = null;
//		if (null != reqParam && !reqParam.isEmpty()) {
//			Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
//			valideData = new HashMap<String, String>(reqParam.size());
//			while (it.hasNext()) {
//				Entry<String, String> e = it.next();
//				String key = (String) e.getKey();
//				String value = (String) e.getValue();
//				value = new String(value.getBytes("ISO-8859-1"), encoding);
//				valideData.put(key, value);
//			}
//		}
//         String orderId = valideData.get("orderId");
//		// 验证签名
//		if (!SDKUtil.validate(valideData, encoding)) {
//			LogUtil.writeLog("验证签名结果[失败].");
//			boolean g = updateOrderStatus(orderId, "02");
//			System.out.println("g"+g);
//		} else {
//			System.out.println(valideData.get("orderId")); //其他字段也可用类似方式获取
//			LogUtil.writeLog("验证签名结果[成功].");
//			boolean s = updateOrderStatus(orderId, "01");
//			System.out.println(orderId+"-->"+s);
//		}
//
//		LogUtil.writeLog("BackRcvResponse接收后台通知结束");
//	}
//
//	@Override
//	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
//			IOException {
//		this.doPost(req, resp);
//	}
//
//	/**
//	 * 获取请求参数中所有的信息
//	 *
//	 * @param request
//	 * @return
//	 */
//	public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
//		Map<String, String> res = new HashMap<String, String>();
//		Enumeration<?> temp = request.getParameterNames();
//		if (null != temp) {
//			while (temp.hasMoreElements()) {
//				String en = (String) temp.nextElement();
//				String value = request.getParameter(en);
//				res.put(en, value);
//				//在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
//				//System.out.println("ServletUtil类247行  temp数据的键=="+en+"     值==="+value);
//				if (null == res.get(en) || "".equals(res.get(en))) {
//					res.remove(en);
//				}
//			}
//		}
//		return res;
//	}
//
//
//
//	//修改订单状态
//	public boolean updateOrderStatus(String orderId,String backresponse){
//		Connection conn = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		 try {
//			 System.out.println("---->更改状态");
////			conn =DBBase.getInstall().GetConnByName("main");
//			ps = conn.prepareStatement("select count(*) from im.nfc_card_recharge where orderNo =?");
//			ps.setString(1, orderId);
//			rs = ps.executeQuery();
//			rs.next();
//			boolean isflag = false;
//			if(rs.getInt(1)==0){
//				isflag = true;
//			}
//			System.out.println("---->更改状态1");
//			rs.close();
//			ps.close();
//
//			if(isflag){
//				ps = conn.prepareStatement("insert into im.backlog values(sysdate,?)");
//				ps.setString(1, orderId);
//				int num = ps.executeUpdate();
//				if(num>0){
//					return false;
//				}
//				ps.close();
//			}
//			/*System.out.println("---->更改状态2");
//			 ps = conn.prepareStatement("select count(*) from ysjl.ys_cz_d_now_BD_temp where orderno=?");
//    		 ps.setString(1, orderId);
//    		 rs = ps.executeQuery();
//    		 rs.next();
//    		 boolean flag = true;
//    		 if(rs.getInt(1)==1){
//    			 flag = false;
//    		 }
//    		 rs.close();
//			 ps.close();
//			 System.out.println("---->更改状态3");
//    		 if(flag){
//    			 ps = conn.prepareStatement("insert into ysjl.ys_cz_d_now_BD_temp(gid,orderno,ordertime,ordermount,cardno,cardcount,cccount,ye,status,czsj,paytype) select gid,orderno,ordertime,ordermount,cardno,jyxh,cccount,ye,'0',sysdate,paytype from im.nfc_card_recharge where orderNo=?");
//    			 ps.setString(1, orderId);
//    			 int num = ps.executeUpdate();
//    			 ps.close();
//    		 }*/
//    		 System.out.println("---->更改状态4");
//			ps = conn.prepareStatement("update im.nfc_card_recharge set BACKRCVRESPONSE=?  where orderno=? ");
//			ps.setString(1, backresponse);
//			ps.setString(2, orderId);
//			int num = ps.executeUpdate();
//			System.out.println(orderId+"更新成功"+num);
//			if(num==1){
//				return true;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally{
//			try {
//				rs.close();
//				ps.close();
//				conn.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//
//		 return false;
//	}
//
//
//
//	public static void main(String[] args) {
//		LogUtil.writeLog("验证签名结果[成功].");
//	}
//}
