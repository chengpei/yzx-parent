package com.whpe.yzxManage.Common;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;


//DES鍔犺В瀵�鏀寔涓巇elphi浜や簰(瀛楃涓茬紪鐮侀渶缁熶竴涓篣TF-8)
public class DESCipher {

	/**  
     * 瀵嗛挜  
     */  
    public static final String KEY = "12345678";
  
    private final static String DES = "DES";
  
    //DES/CBC/NoPadding
    //DES/ECB/PKCS5Padding
    //DES
    //DES/ECB/NoPadding
    private final static String DESPadding = "DES/ECB/NoPadding";
    /**  
     * 鍔犲瘑  
     *   
     * @param src  
     *            鏄庢枃(瀛楄妭)  
     * @param key  
     *            瀵嗛挜锛岄暱搴﹀繀椤绘槸8鐨勫�鏁� 
     * @return 瀵嗘枃(瀛楄妭)  
     * @throws Exception
     */  
    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        // DES绠楁硶瑕佹眰鏈変竴涓彲淇′换鐨勯殢鏈烘暟婧�  
        SecureRandom sr = new SecureRandom();
        // 浠庡師濮嬪瘑鍖欐暟鎹垱寤篋ESKeySpec瀵硅薄   
        DESKeySpec dks = new DESKeySpec(key);
        // 鍒涘缓涓�釜瀵嗗寵宸ュ巶锛岀劧鍚庣敤瀹冩妸DESKeySpec杞崲鎴�  
        // 涓�釜SecretKey瀵硅薄   
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher瀵硅薄瀹為檯瀹屾垚鍔犲瘑鎿嶄綔   
        Cipher cipher = Cipher.getInstance(DESPadding);
        // 鐢ㄥ瘑鍖欏垵濮嬪寲Cipher瀵硅薄   
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        // 鐜板湪锛岃幏鍙栨暟鎹苟鍔犲瘑   
        // 姝ｅ紡鎵ц鍔犲瘑鎿嶄綔   
        return cipher.doFinal(src);   
    }   
  
    /**  
     * 瑙ｅ瘑  
     *   
     * @param src  
     *            瀵嗘枃(瀛楄妭)  
     * @param key  
     *            瀵嗛挜锛岄暱搴﹀繀椤绘槸8鐨勫�鏁� 
     * @return 鏄庢枃(瀛楄妭)  
     * @throws Exception
     */  
    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        // DES绠楁硶瑕佹眰鏈変竴涓彲淇′换鐨勯殢鏈烘暟婧�  
        SecureRandom sr = new SecureRandom();
        // 浠庡師濮嬪瘑鍖欐暟鎹垱寤轰竴涓狣ESKeySpec瀵硅薄   
        DESKeySpec dks = new DESKeySpec(key);
        // 鍒涘缓涓�釜瀵嗗寵宸ュ巶锛岀劧鍚庣敤瀹冩妸DESKeySpec瀵硅薄杞崲鎴�  
        // 涓�釜SecretKey瀵硅薄   
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher瀵硅薄瀹為檯瀹屾垚瑙ｅ瘑鎿嶄綔   
        Cipher cipher = Cipher.getInstance(DESPadding);
        // 鐢ㄥ瘑鍖欏垵濮嬪寲Cipher瀵硅薄   
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        
        // 鐜板湪锛岃幏鍙栨暟鎹苟瑙ｅ瘑   
        // 姝ｅ紡鎵ц瑙ｅ瘑鎿嶄綔   
        return cipher.doFinal(src);   
    }   
  
    /**  
     * 鍔犲瘑  
     *   
     * @param src  
     *            鏄庢枃(瀛楄妭)  
     * @return 瀵嗘枃(瀛楄妭)  
     * @throws Exception
     */  
    public static byte[] encrypt(byte[] src) throws Exception {
        return encrypt(src, KEY.getBytes());   
    }   
  
    /**  
     * 瑙ｅ瘑  
     *   
     * @param src  
     *            瀵嗘枃(瀛楄妭)  
     * @return 鏄庢枃(瀛楄妭)  
     * @throws Exception
     */  
    public static byte[] decrypt(byte[] src) throws Exception {
        return decrypt(src, KEY.getBytes());   
    }   
  
    /**  
     * 鍔犲瘑  
     *   
     * @param src  
     *            鏄庢枃(瀛楃涓�  
     * @return 瀵嗘枃(16杩涘埗瀛楃涓�  
     * @throws Exception
     */  
    public final static String encrypt(String src) {
        try {   
            return byte2hex(encrypt(src.getBytes(), KEY.getBytes()));   
        } catch (Exception e) {
            e.printStackTrace();   
        }   
        return null;   
    }   
  
    /**  
     * 瑙ｅ瘑  
     *   
     * @param src  
     *            瀵嗘枃(瀛楃涓�  
     * @return 鏄庢枃(瀛楃涓�  
     * @throws Exception
     */  
    public final static String decrypt(String src) {
        try {   
            return new String(decrypt(hex2byte(src.getBytes()), KEY.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();   
        }   
        return null;   
    }   
  
    /**  
     * 鍔犲瘑  
     *   
     * @param src  
     *            鏄庢枃(瀛楄妭)  
     * @return 瀵嗘枃(16杩涘埗瀛楃涓�  
     * @throws Exception
     */  
    public static String encryptToString(byte[] src) throws Exception {
        return encrypt(new String(src));
    }   
  
    /**  
     * 瑙ｅ瘑  
     *   
     * @param src  
     *            瀵嗘枃(瀛楄妭)  
     * @return 鏄庢枃(瀛楃涓�  
     * @throws Exception
     */  
    public static String decryptToString(byte[] src) throws Exception {
        return decrypt(new String(src));
    }   
  
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {   
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)   
                hs = hs + "0" + stmp;   
            else  
                hs = hs + stmp;   
        }   
        return hs.toUpperCase();   
    }   
  
    public static byte[] hex2byte(byte[] b) {   
        if ((b.length % 2) != 0)   
            throw new IllegalArgumentException("闀垮害涓嶆槸鍋舵暟");
        byte[] b2 = new byte[b.length / 2];   
        for (int n = 0; n < b.length; n += 2) {   
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }   
        return b2;   
    }   
    
    public static String Encrypt(String hexsrc,String hexkey){
    	try{
	    	byte[] bresult= encrypt( StringCls.HexString2Bytes(hexsrc, false) ,StringCls.HexString2Bytes(hexkey, false) );
	    	return StringCls.Bytes2HexString(bresult, false);
    	}catch(Exception e){
    		return null;
    	}
    }
    
    public static String Decrypt(String hexsrc,String hexkey){
    	try{
	    	byte[] bresult= decrypt( StringCls.HexString2Bytes(hexsrc, false) ,StringCls.HexString2Bytes(hexkey, false) );
	    	return StringCls.Bytes2HexString(bresult, false);
    	}catch(Exception e){
    		return null;
    	}
    }
    
    public static String Encrypt3(String hexsrc,String hexkey){
    	try{
	    	byte[] bresult= encrypt( StringCls.HexString2Bytes(hexsrc, false) ,StringCls.HexString2Bytes(hexkey.substring(0,16), false) );
	    	bresult=decrypt(bresult,StringCls.HexString2Bytes(hexkey.substring(16,32), false));
	    	bresult=encrypt(bresult,StringCls.HexString2Bytes(hexkey.substring(0,16), false));
	    	return StringCls.Bytes2HexString(bresult, false);
    	}catch(Exception e){
    		return null;
    	}
    }
    
    public static String Decrypt3(String hexsrc,String hexkey){
    	try{
	    	byte[] bresult= decrypt( StringCls.HexString2Bytes(hexsrc, false) ,StringCls.HexString2Bytes(hexkey.substring(0,16), false) );
	    	bresult=encrypt(bresult,StringCls.HexString2Bytes(hexkey.substring(16,32), false));
	    	bresult=decrypt(bresult,StringCls.HexString2Bytes(hexkey.substring(0,16), false));
	    	return StringCls.Bytes2HexString(bresult, false);
    	}catch(Exception e){
    		return null;
    	}
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*byte[] key={ (byte) 0xB9,
				(byte) 0xD3, (byte) 0xB5, (byte) 0xD7, (byte) 0xAB, (byte) 0xC3,
				(byte) 0xC0, (byte) 0xCD, (byte) 0xB3, (byte) 0xEE, (byte) 0xF2,
				(byte) 0xAC, (byte) 0xCF, (byte) 0xCC, (byte) 0xB6, (byte) 0xD0 };*/
		String key = "b9d3b5d7abc3c0cdb3eef2accfccb6d0";
		String s = "8E69D8857BDFC8AD0000000000000000";
		System.out.println(Decrypt3(s,key));
//		System.out.println("鍔�+Encrypt3("67C6B70FC894439D67C6B70FC894439D","56A0DFCC8DB885AB88FB640A4084305F"));
//		System.out.println("瑙�+Decrypt3("67C6B70FC894439D67C6B70FC894439D","56A0DFCC8DB885AB88FB640A4084305F"));
		// TODO Auto-generated method stub
//		try {
//			String Str="906824";
//			String Str2="F7B90C857FD93EE6";
//			String key="20110501";
//			key+="0000000000000000";
//			key=key.substring(0,16);
//			
//			while(Str.length()%16!=0){
//				Str+="00";
//			}
//			
////			while(Str2.length()%16!=0){
////				Str2+="00";
////			}
//			
//			
//			System.out.println(byte2hex(encrypt(MessageCenter.HexString2Bytes(Str, false),
//					MessageCenter.HexString2Bytes(key, false))));
//			System.out.println(byte2hex(decrypt(
//					new String(MessageCenter.HexString2Bytes(Str2,false),"utf-8").getBytes(),
//					//MessageCenter.HexString2Bytes(Str2, false),
//					//Base64.decode(MessageCenter.HexString2Bytes(Str2, false)),
//					MessageCenter.HexString2Bytes(key, false))));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
