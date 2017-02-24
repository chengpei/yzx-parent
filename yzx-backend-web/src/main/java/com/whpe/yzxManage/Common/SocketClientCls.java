package com.whpe.yzxManage.Common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.LinkedHashMap;


public class SocketClientCls {

	private static String TXIp = Global.CongfigMap.getProperty("JmjIp");
	private static int TxPort = Integer.valueOf(Global.CongfigMap.getProperty("JmjPort"));

	private Socket m_socket = null;
	Calendar lastActive=null;//最后活跃时间

	public SocketClientCls() {
		try {
			m_socket = new Socket();
			m_socket.setSoTimeout(8000);
			m_socket.connect(new InetSocketAddress(InetAddress.getByName(TXIp), TxPort), 8000);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public SocketClientCls(String ip,int port) {
		try {
			m_socket = new Socket();
			m_socket.setSoTimeout(5000);
			m_socket.connect(new InetSocketAddress(InetAddress.getByName(ip), port), 5000);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Calendar getLastActive() {
		return lastActive;
	}

	public void setLastActive(Calendar lastActive) {
		this.lastActive = lastActive;
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		CloseLink();
		super.finalize();
	}

	public boolean SendMsg(byte[] buf) {
		try {
			DataOutputStream doc = new DataOutputStream(m_socket.getOutputStream());
			doc.write(buf);
			doc.flush();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	public byte[] ReceiveMsg() {
		try {
			DataInputStream in = new DataInputStream(m_socket.getInputStream());
			int itime = 0;
			byte[] byteMac = null;
			do {
				Thread.sleep(10);
				itime++;
				byteMac = new byte[in.available()];

			} while (byteMac.length == 0 && itime < 500);
			in.read(byteMac, 0, byteMac.length);
			return byteMac;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public boolean SendText(String txt) {
		return SendMsg(txt.getBytes());
	}

	public String ReceiveText() {
		return (new String(ReceiveMsg()));
	}

	public boolean CloseLink() {
		if (m_socket != null) {
			try {
				m_socket.close();
			} catch (Exception e) {
			}
		}
		return true;
	}

	// 判断是否关闭,关闭为T
	public boolean isClose() {
		if (m_socket == null) {
			System.out.println("socket 为空");
			return true;
			// return false;
		} else {
			if (!m_socket.isConnected()) {
				System.out.println("Connect 断开");
			}
			if (m_socket.isInputShutdown()) {
				System.out.println("isInput 被关闭");
				return true;
			}
			if (m_socket.isOutputShutdown()) {
				System.out.println("OutInput 被关闭");
				return true;
			}
			if (m_socket.isClosed()) {
				System.out.println("socket 被关闭");
				return true;
			}
		}
		return false;
	}

	public boolean SendHexString(String hexStr) {
		byte[] data = StringCls.HexString2Bytes(hexStr, false);
		return SendMsg(data);
	}

	public String ReceiveHexString() {
		byte[] data = ReceiveMsg();
		if (data == null)
			return null;
		return StringCls.Bytes2HexString(data, false);
	}
	
	/**
	 * 通用定长解析
	 * @param temp  需要解析的串
	 * @param roles 解析规则
	 * @return
	 */
	public static LinkedHashMap<String,String> publicAnaly(String temp,LinkedHashMap<String,Integer> roles){
		
		LinkedHashMap<String,String> map = null;
		int i_temp=0;
		int i_start=0;
		int i_end=0;
		try{
			if(roles.size()>=0){
				map = new LinkedHashMap<String,String>();
				for(String key : roles.keySet()){
					i_start=i_temp;
					i_temp+=roles.get(key)*2;
					i_end=i_temp;
					map.put(key, temp.substring(i_start,i_end));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return map;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		SocketClientCls client = new SocketClientCls();
		//002B 0000 00 7043 01 20151009143222 70270003 102700036352 201510091406902711033999192000000064EF37
		
	}

}
