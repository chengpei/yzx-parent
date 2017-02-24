package com.whpe.yzxManage.Common;

import java.util.Calendar;


public class DateTimeCls {
	
	
	public static String Time12HexToTime8Hex(String time){
		if(time.length()!=12)return null;
		byte[] temp=StringCls.HexString2Bytes(time, false);		
		int[] tm=new int[6];
		for(int i=0;i<6;i++){
			int j=(int)temp[i];
			tm[i]=(j>=0)?j:256+j;
		}
		byte[] Retm=new byte[4];
		Retm[0]=(byte)(tm[0]*4+(tm[1]/0x4));
		Retm[1]=(byte)((tm[1]%0x4)*0x40+tm[2]*2+(tm[3]/0x10));
		Retm[2]=(byte)((tm[3]%0x10)*0x10+(tm[4]/4));
		Retm[3]=(byte)((tm[4]%4)*0x40+tm[5]);		
		return StringCls.Bytes2HexString(Retm, false);		
	}	
	
	//110315155000->2CDEFC80
	public static String Time12DecToTime8Hex(String time){
		return Time12HexToTime8Hex(StrDecToStrHex(time));
	}
	
	//2CDEFC80->110315155000
	public static String Time8HexToTime12Hex(String time){
		if(time.length()!=8)return null;
		byte[] temp=StringCls.HexString2Bytes(time, false);	
		int[] tm=new int[4];
		for(int i=0;i<4;i++){
			int j=(int)temp[i];
			tm[i]=(j>=0)?j:256+j;
		}
		byte[] Retm=new byte[6];
		Retm[0]=(byte)(tm[0]/4);
		Retm[1]=(byte)(((tm[0]%4)*4)+(tm[1]/64));
		Retm[2]=(byte)((tm[1]%64)/2);
		Retm[3]=(byte)(((tm[1]%2)*16)+(tm[2]/16));
		Retm[4]=(byte)(((tm[2]%16)*4)+(tm[3]/64));
		Retm[5]=(byte)(tm[3]%64);
		return StringCls.Bytes2HexString(Retm, false);
	}
	
	//2CDEFC80->0B030F0F3200
	public static String Time8HexToTime12Dec(String time){
		return StrHexToStrDec(Time8HexToTime12Hex(time));
	}
	
	
	
	
	public static String StrDecToStrHex(String Dec){
		String Str="";
		for(int i=0;i<Dec.length()/2;i++){			 
			Str+= String.format("%02X", Integer.parseInt(Dec.substring((i * 2), (i * 2) + 2), 10));
		}
		return Str;
	}
	
	public static String StrHexToStrDec(String Hex){
		String Str="";
		for(int i=0;i<Hex.length()/2;i++){			 
			Str+= String.format("%02d", Integer.parseInt(Hex.substring((i * 2), (i * 2) + 2), 16));
		}
		return Str;
	}
	
	public static Calendar Time8HexToCalendar(String time){
		String temp=Time8HexToTime12Dec(time);
//		temp="20"+temp.substring(0,2)+"-"+temp.substring(2,4)+"-"+temp.substring(4,6)+" "
//			+temp.substring(6,8)+":"+temp.substring(8,10)+":"+temp.substring(10,12);		
		Calendar ca= Calendar.getInstance();
		ca.set(Integer.parseInt("20" + temp.substring(0, 2)), Integer.parseInt(temp.substring(2, 4))-1,
				Integer.parseInt(temp.substring(4, 6)), Integer.parseInt(temp.substring(6, 8)),
				Integer.parseInt(temp.substring(8, 10)), Integer.parseInt(temp.substring(10, 12)));
		return ca;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(Time8HexToCalendar("8F3D7EFB"));
//		System.out.println(Time12DecToTime8Hex("110315155000"));
//		System.out.println(Time8HexToTime12Dec("2CDEFC80"));
//		
//		System.out.println(Time12DecToTime8Hex("991230235959"));
//		System.out.println(Time12DecToTime8Hex("999999999999"));
//		
//		System.out.println(Time8HexToTime12Dec("8F3D7EFB"));
//		System.out.println(Time8HexToTime12Dec("A48C4823"));
//		
//		System.out.println(Time12DecToTime8Hex("351230235959"));
		//System.out.println(Time8HexToTime12Dec("2CDEFC80"));
		//System.out.println(Time14ToTime8(TimeDecToTimeHex("110415134617")));
		//System.out.println(Time14ToTime8(TimeDecToTimeHex("110315155000")));
		
		//System.out.println(Time8ToTime142("2CDEFC80"));
		//System.out.println(Time14ToTime8("110315155000"));
	}

}
