package com.whpe.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    private static final Log logger = LogFactory.getLog(MD5Utils.class);
    private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * md5加密
     * @param source
     * @return
     */
    public static StringBuffer getMD5(byte[] source) {
        MessageDigest md;
        try {
            md = java.security.MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        StringBuffer sb = new StringBuffer();
        try {
            md.update(source);
            byte tmp[] = md.digest();
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                sb.append(hexDigits[byte0 >>> 4 & 0xf]);
                sb.append(hexDigits[byte0 & 0xf]);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return sb;
    }

    /**
     * md5加密
     * @param source
     * @return
     */
    public static StringBuffer getMD5(String source) {
        return getMD5(source.getBytes());
    }
}
