package com.whpe.utils;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by chengpei on 2017/2/26.
 */
public class StringUtils {

    // 验证码可选字符
    private static char[] codeSequence = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    // 创建一个随机数生成器类
    private static Random random = new Random();

    public static String getRadomString(int length) {
        StringBuffer randomCode = new StringBuffer();
        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < length; i++) {
            // 得到随机产生的验证码数字。
            String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            // 将产生的随机数组合在一起。
            randomCode.append(strRand);
        }
        return randomCode.toString();
    }

    public static boolean isEmpty(CharSequence value) {
        return value == null || value.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence value){
        return !isEmpty(value);
    }

    /**
     * 将byte数组转换为HEX字符串
     * @param byteData
     * @param hasSpace
     * @return
     */
    public static String bytes2HexString(byte[] byteData, boolean hasSpace) {
        String ret = "";
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(byteData[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
            if (hasSpace)
                ret += " ";
        }
        return ret;
    }

    /**
     * 将HEX字符串转换为byte数组
     * @param hexStr
     * @param hasSpace
     * @return
     */
    public static byte[] hexString2Bytes(String hexStr, boolean hasSpace) {
        if (hasSpace){
            hexStr = hexStr.replace(" ", "");
        }
        byte[] ret = new byte[hexStr.length() / 2];
        byte[] tmp = hexStr.getBytes();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    /**
     * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
     * @param src0
     * @param src1
     * @return byte
     */
    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 | _b1);
        return ret;
    }

    public static String strToHex(String str, boolean mSpace) {
        return strToHex(str, mSpace, "gb2312");
    }

    /**
     * 字符串 转换为 HEX
     * @param str
     * @param mSpace
     * @param charsetname
     * @return
     */
    public static String strToHex(String str, boolean mSpace, String charsetname) {
        if (str == null)
            return null;
        try {
            return bytes2HexString(str.getBytes(charsetname), mSpace);
        } catch (UnsupportedEncodingException e) {

            return null;
        }
    }

    public static String hexToStr(String str) {
        return hexToStr(str, "gb2312");
    }

    /**
     * HEX 转换为 字符串
     * @param str
     * @param charsetname
     * @return
     */
    public static String hexToStr(String str, String charsetname) {
        if (str == null)
            return null;
        try {
            return (new String(hexString2Bytes(str, false), charsetname));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
