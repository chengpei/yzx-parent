package com.whpe.yzxManage.Common;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class Sendsms {

	private static final String SpCode = Global.CongfigMap.getProperty("smsSpCode");
	private static final String LoginName = Global.CongfigMap.getProperty("smsLoginName");
	private static final String Password = Global.CongfigMap.getProperty("smsPassword");
	private static final SimpleDateFormat simform = new SimpleDateFormat( "yyyyMMddHH:mm:ss.SSS");
	private static Calendar cal = Calendar.getInstance();
	
	public static boolean send(String mobile, String mobile_code,Map<String,String> map,String type){
		String info = null;
		if(mobile==null||mobile_code==null||map==null){
			return false;
		}
		try{
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost post = new HttpPost("https://api.ums86.com:9600/sms/Api/Send.do");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("SpCode", SpCode));
			nvps.add(new BasicNameValuePair("LoginName", LoginName));
			nvps.add(new BasicNameValuePair("Password", Password));
			map.put("mobliecode",mobile_code+"");
			nvps.add(new BasicNameValuePair("MessageContent","您的验证码为{"+mobile_code+"}"));
			nvps.add(new BasicNameValuePair("UserNumber", mobile));
			String sn = simform.format(cal.getTime());
			map.put("SerialNumber",sn);
			nvps.add(new BasicNameValuePair("SerialNumber", sn));
			nvps.add(new BasicNameValuePair("ScheduleTime", ""));
			nvps.add(new BasicNameValuePair("ExtendAccessNum", ""));
			nvps.add(new BasicNameValuePair("f", "1"));
			post.setEntity(new UrlEncodedFormEntity(nvps, "GBK"));
			CloseableHttpResponse response = httpclient.execute(post);
			HttpEntity responseEntity = response.getEntity();
			BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
			info = reader.readLine();
			System.out.println(info);
			if(info!=null){
				String[] as = info.split("&");
				for(String temp:as){
					String[] temp1 = temp.split("=");
					if(temp1.length>1){
						map.put(temp1[0],temp1[1]);
					}else{
						map.put(temp1[0],"");
					}
			}
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);
		Map<String,String> map = new HashMap<String, String>();
		 Sendsms.send("13476073978",mobile_code+"",map,"1");
		System.out.println(map);
	}

}
