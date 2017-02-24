package com.whpe.yzxManage.Common;

public class CRCCls {	
	
	public static String CRC8(byte[] data){
		int mac=0xC7;
		for(int i=0;i<data.length;i++){    		
    	 	mac=mac^data[i]; 
    	 	for(int j=0;j<8;j++){
    	 		if((mac&0x80)!=0)
    	 			mac=(mac<<1)^0x1D;
    	 		else
    	 			mac=mac<<1;   
    	 	}    	 	
    	}
		String hexStr= Integer.toHexString(mac);
		if(hexStr.length()<2)hexStr="00"+hexStr;
		if(hexStr.length()>2)hexStr=hexStr.substring(hexStr.length()-2);
		return hexStr.toUpperCase();	    
	}
	
	public static String CRC8(String HexStr){
		return CRC8(StringCls.HexString2Bytes(HexStr, false));
	}
	
	public static String CRC16(byte[] data){
		int mac=0x31e3;
		for(int i=0;i<data.length;i++){    		
    	 	mac=mac^(data[i]<<8); 
    	 	for(int j=0;j<8;j++){
    	 		if((mac&0x8000)!=0)
    	 			mac=(mac<<1)^0x1021;
    	 		else
    	 			mac=mac<<1;   
    	 	}    	 	
    	}
		String hexStr= Integer.toHexString(mac);
		if(hexStr.length()<4)hexStr="0000"+hexStr;
		if(hexStr.length()>4)hexStr=hexStr.substring(hexStr.length()-4);
		return hexStr.toUpperCase();	    
	}
	
	public static String CRC16(String HexStr){
		return CRC16(StringCls.HexString2Bytes(HexStr, false));
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//byte[] date=new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
		//byte[] date=new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
		//System.out.println(CRC16("204220110418011742525d44"));
		//System.out.println(CRC16("20220000000000000000000000000000000100000000000000020000000000000003000000000000000400000000000000050000000000000006000000000000000700000000000000080000000000000009000000000000000A000000000000000B000000000000000C000000000000000D000000000000000E000000000000000F0000000000000010000000000000001100000000000000120000000000000013000000000000001400000000000000150000000000000016000000000000001700000000000000180000000000000019000000000000001A000000000000001B000000000000001C000000000000001D000000000000001E000000000000001F0000000000000020000000000000002100000000000000220000000000000023000000000000002400000000000000250000000000000026000000000000002700000000000000280000000000000029000000000000002A000000000000002B000000000000002C000000000000002D000000000000002E000000000000002F0000000000000030000000000000003100000000000000320000000000000033000000000000003400000000000000350000000000000036000000000000003700000000000000380000000000000039000000000000003A000000000000003B000000000000003C000000000000003D000000000000003E000000000000003F"));
		System.out.println(CRC8("A6014180FFFFFFFFFF437001000100012DBD6E72000000094370021100000132021100010002AB330025B0AB0EC42012080143704370FFFFFFFFFFFFFFFFFF"));
		//System.out.println(CRC8("01020304050607080910"));
		//System.out.println(CRC8("c600112233000000998877000000000000050cc2df0000000043700201000000120201000055a3000100000000ffff110328000000000000000000000000ff"));
		if(true)return;
		System.out.println(CRC16("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F404142434445464748494A4B4C4D4E4F505152535455565758595A5B5C5D5E5F606162636465666768696A6B6C6D6E6F707172737475767778797A7B7C7D7E7F808182838485868788898A8B8C8D8E8F909192939495969798999A9B9C9D9E9FA0A1A2A3A4A5A6A7A8A9AAABACADAEAFB0B1B2B3B4B5B6B7B8B9BABBBCBDBEBFC0C1C2C3C4C5C6C7C8C9CACBCCCDCECFD0D1D2D3D4D5D6D7D8D9DADBDCDDDEDFE0E1E2E3E4E5E6E7E8E9EAEBECEDEEEFF0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF66"));
		
		byte[] date=new byte[]{0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39};
		System.out.println(StringCls.Bytes2HexString(date, false));
		System.out.println(CRC16(date));
		System.out.println(CRC8(date));
		//byte[] date2=new byte[]{20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1};
		byte[] date2=new byte[]{0x39,0x38,0x37,0x36,0x35,0x34,0x33,0x32,0x31};
		System.out.println(CRC16(date2));
		System.out.println(CRC8(date2));
		// TODO Auto-generated method stub
		
		if(true)return;
		System.out.println(Integer.valueOf("0708", 16));
		System.out.println(Integer.toHexString(1800));
		
		
		byte[] temp=new byte[]{1,2};
		System.out.println(StringCls.Bytes2HexString(temp, false));
		
		byte[] temp1= String.valueOf(new char[]{1, 2}).getBytes();
		System.out.println(StringCls.Bytes2HexString(temp1, false));
		
		byte[] temp2=StringCls.HexString2Bytes("31E3", false);
		System.out.println(StringCls.Bytes2HexString(temp2, false));
		
		byte[] temp3= String.valueOf(new char[]{0x31, 0xE3}).getBytes();
		System.out.println(StringCls.Bytes2HexString(temp3, false));

	}

}
