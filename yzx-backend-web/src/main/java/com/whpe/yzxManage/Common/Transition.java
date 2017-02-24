package com.whpe.yzxManage.Common;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

public class Transition {

	public static String EncrypMD5(String str)
	 {
		  if (str == null || str.length() == 0) {
		   throw new IllegalArgumentException("String to encript cannot be null or zero length");
		  }
		  
		  StringBuffer hexString = new StringBuffer();
		  
		  try {
			   MessageDigest md = MessageDigest.getInstance("MD5");
			   md.update(str.getBytes());
			   byte[] hash = md.digest();
			   
			   for (int i = 0; i < hash.length; i++) {
			    if ((0xff & hash[i]) < 0x10) {
			     hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
			    }    
			    else {
			     hexString.append(Integer.toHexString(0xFF & hash[i]));
			    }    
			   }
		  }
		  catch (NoSuchAlgorithmException e) {
			  e.printStackTrace();
		  }
		  String temp=hexString.toString();
		  return temp.toUpperCase();
	 }
	
	public static String EncrypKey(String str,String key)
	{
		String dest="" ;
		String dd=DateOpCls.DateToFormat(new Date(), 0);
		int KeyLen;
		int KeyPos=-1;
		int offset;		
		int SrcAsc=0;	
		int Range=256;
		if(key=="")key="yjt"+dd+".";
		KeyLen=key.length();
		offset=(new Random()).nextInt(Range);
		
		dest= String.format("%2x", offset).replace(" ", "0");
		int icount=str.length();
		for(int i=0;i<icount;i++)
		{
			SrcAsc=(((int)str.charAt(i))+offset)%255;
			KeyPos=KeyPos+1;			
			if(KeyPos>=KeyLen)KeyPos=0;		
			SrcAsc=SrcAsc^((int)key.charAt(KeyPos));
			dest=dest+ String.format("%2x", SrcAsc).replace(" ", "0");
			offset=SrcAsc;
		}
		return dest.toUpperCase();
	}
	
	public static String DecrypKey(String str,String key)
	{
		String dest="" ;
		String dd=DateOpCls.DateToFormat(new Date(), 0);
		int KeyLen;
		int KeyPos=-1;
		int offset;		
		int SrcAsc=0;	
		int SrcPos=0;		
		int TmpSrcAsc;
		if(key=="")key="yjt"+dd+".";
		KeyLen=key.length();
		offset= Integer.parseInt(str.substring(0, 2), 16);
		SrcPos=2;
		do
		{			
			SrcAsc= Integer.parseInt(str.substring(SrcPos, SrcPos + 2), 16);
			KeyPos=KeyPos+1;			
			if(KeyPos>=KeyLen)KeyPos=0;	
			int itemp=(int)key.charAt(KeyPos);
			if(itemp<0)itemp=itemp+256;
			TmpSrcAsc=SrcAsc^(itemp);
			if(TmpSrcAsc<=offset)
			{
				TmpSrcAsc=255+TmpSrcAsc-offset;
			}else
			{
				TmpSrcAsc=TmpSrcAsc-offset;
			}
			if(TmpSrcAsc>127)TmpSrcAsc=TmpSrcAsc-256;
			if(TmpSrcAsc<-128)TmpSrcAsc=TmpSrcAsc+256;
			dest=dest+(char)TmpSrcAsc;
			offset=SrcAsc;
			SrcPos=SrcPos+2;
		}while(SrcPos<str.length());	
		
		return dest;
		
	}
}
