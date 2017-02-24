package com.whpe.yzxManage.Common;

import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


public class Global {
	public static String Admin="yjt";
	public static String Session_UserName="SESSIONUSERNAME";
	public static String Session_Pwd="SESSIONPWD";
	public static String Session_Power="SESSIONPOWER";
	
	public static String InitPassword="123456";
	//操作卡信息密文
	public static String Session_OpCardParam="SESSIONOPCARDPARAM";
	public static String Session_WebUserParam="SESSIONWEBUSERPARAM";
	public static String Session_RandParam="SESSIONRANDPARAM";
	//操作卡信息明文
	public static String Session_OpCard="SESSIONOPCARD";
	
	public static void MsgLog(String msg){
		Logger log= Logger.getLogger("log");
		log.info(msg);
	}
	
	public static void MsgLogOpRecord(String msg,String name){
		try{
			Logger log= Logger.getAnonymousLogger();
			Calendar cal = Calendar.getInstance();
			String file=RootDir()+"\\logs\\OpRecord\\"+new SimpleDateFormat("yyyyMM").format(cal.getTime());
			new File(file).mkdirs();
			file+="\\"+name+".log";
			FileHandler fileHandler = new FileHandler(file,true );
			fileHandler.setFormatter(new MyLogHander());
			log.addHandler(fileHandler);			
			log.info(msg);
			fileHandler.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void MsgLog(String msg,String name){
		try{
			Logger log= Logger.getAnonymousLogger();
			Calendar cal = Calendar.getInstance();
			String file=RootDir()+"\\logs\\"+name;
			new File(file).mkdirs();
			file+="\\"+new SimpleDateFormat("yyyyMMdd").format(cal.getTime())+".log";
			FileHandler fileHandler = new FileHandler(file,true );
			fileHandler.setFormatter(new MyLogHander());
			log.addHandler(fileHandler);			
			log.info(msg);
			fileHandler.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void LogTable(String[] list,String tablename){
		try{
			Calendar cal = Calendar.getInstance();
			String file=RootDir()+"\\logs\\Table\\"+new SimpleDateFormat("yyyyMM").format(cal.getTime());
			new File(file).mkdirs();
			FileWriter fw=new FileWriter(file+"\\"+tablename+".txt",true);
			for(String str : list){
				fw.write(str+"\t");
			}
			fw.write("\r\n");			
			fw.close();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static Properties CongfigMap=GetProps();
	
	private static Properties GetProps(){
		InputStream ins =Global.class.getResourceAsStream("/config.properties");
		Properties m_props = new Properties();
	    try {	    	    	
			m_props.load(ins);
			ins.close();
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}	    		
	    
	    Enumeration ee= m_props.propertyNames();
		while(ee.hasMoreElements()){
			Object obj=ee.nextElement();
			if(obj==null){
				break;
			}
			String s=obj.toString();
			System.out.println(s+":"+m_props.getProperty(s));
		}
		return m_props;
	}	
	//常用变量
	public static String SysName="宜知行后台管理系统";
	public static String SysRoot="YZXManage";
	
	public static String SysCardSvrUrl=Global.CongfigMap.getProperty("SvrUrl");
	
	public static org.apache.log4j.Logger logger = getLogger();//在监听加载配置时初始化日志类
	public static org.apache.log4j.Logger getLogger(){
		//加载log4j 配置文件
    	try {
    	
    		InputStream ins =Global.class.getResourceAsStream("/log4j.properties");
    		Properties m_props = new Properties();
    		m_props.load(ins);
    		ins.close();
        	PropertyConfigurator.configure(m_props);
        	System.out.println("log4j.propreties文件加载成功");
        	//加载之后,初始化日志函数
        	return org.apache.log4j.Logger.getLogger(Global.class);
    	   
		} catch (Exception e) {
			 System.out.println("log4j.propreties文件加载失败");
		}
    	return null;
	}
	
	public static void PrintOut(String msg){
		logger.info(msg);
	}
	
	public static void PrintExp(String msg,Exception ex){
		logger.info(msg, ex);
	}
	
	/**
	 * 按照字节串进行解析
	 * @param Str
	 * @param roles
	 * @return
	 */
	public static HashMap<String,String> publicAnalyByte(String Str,LinkedHashMap<String,Integer> roles){
		return  publicAnaly(Str, roles, 2);
	}
	
	/**
	 * 按照字符串进行解析
	 * @param Str 需要解析的串
	 * @param roles 解析规则
	 * @return
	 */
	public static HashMap<String,String> publicAnalyString(String Str,LinkedHashMap<String,Integer> roles){
		return  publicAnaly(Str, roles, 1);
	}
	
	public static LinkedHashMap<String,String> publicAnalyString2(String Str,LinkedHashMap<String,Integer> roles){
		return  publicAnaly2(Str, roles, 1);
	}
	
	
	
	/**
	 * 通用定长解析
	 * @param Str  需要解析的串
	 * @param roles 解析规则
	 * @param unitlen 单位长度 字符单位长度是1 如果是字节则单位长度是2
	 * @return
	 */
	public static HashMap<String,String> publicAnaly(String Str,LinkedHashMap<String,Integer> roles,int unitlen){
		
		HashMap<String,String> map = null;
		int i_temp=0;
		int i_start=0;
		int i_end=0;
		try{
			if(roles.size()>=0){
				map = new HashMap<String,String>();
				for(String key : roles.keySet()){
					i_start=i_temp;
					i_temp+=roles.get(key)*unitlen;
					i_end=i_temp;
					map.put(key, Str.substring(i_start,i_end));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return map;
	}
	
	
public static LinkedHashMap<String,String> publicAnaly2(String Str,LinkedHashMap<String,Integer> roles,int unitlen){
		
	 LinkedHashMap<String,String> map = null;
		int i_temp=0;
		int i_start=0;
		int i_end=0;
		try{
			if(roles.size()>=0){
				map = new LinkedHashMap<String,String>();
				for(String key : roles.keySet()){
					i_start=i_temp;
					i_temp+=roles.get(key)*unitlen;
					i_end=i_temp;
					map.put(key, Str.substring(i_start,i_end));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return map;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	public static String RootDir(){
		String dir="";
		try{
			dir= System.getenv("CATALINA_HOME");
			if(dir==null||dir.equals("")){
				dir=Global.CongfigMap.getProperty("LogFileDir");
			}			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return dir;
	}
	
	public static SimpleDateFormat sDateFormat =new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS");
	
	
}

class MyLogHander extends Formatter {
		@Override
	public String format(LogRecord record) {
		// TODO Auto-generated method stub				
		return Global.sDateFormat.format(new Date(record.getMillis()))+":" + record.getMessage()+"\r\n" ;
	} 
}
