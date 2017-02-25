package com.whpe.utils;

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

}
