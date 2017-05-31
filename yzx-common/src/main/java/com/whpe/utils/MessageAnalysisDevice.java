package com.whpe.utils;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 消息解析类
 */
public class MessageAnalysisDevice {

    private Map<String,Integer> analysisLengthMap = new LinkedHashMap<String, Integer>();

    private Map<String, String> analysisResultMap = new HashMap<String, String>();

    public void putAnalysisLength(String key, Integer length){
        analysisLengthMap.put(key, length);
    }

    public boolean analysis(String data){
        int start = 0;
        int end = 0;
        String previousValue = null;
        Iterator<Map.Entry<String, Integer>> iterator = analysisLengthMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Integer> next = iterator.next();
            String key = next.getKey();
            Integer length = next.getValue();
            if(length.intValue() == -1){ // 如果长度为-1，则长度取上一个key的值
                length = Integer.parseInt(previousValue);
            }
            end = start + length;
            String value = data.substring(start, end).trim();
            start = end;
            analysisResultMap.put(key, value);
            previousValue = value;
        }
        return true;
    }

    public boolean analysis(byte[] message) {
        int start = 0;
        int end = 0;
        String previousValue = null;
        Iterator<Map.Entry<String, Integer>> iterator = analysisLengthMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Integer> next = iterator.next();
            String key = next.getKey();
            Integer length = next.getValue();
            if(length.intValue() == -1){ // 如果长度为-1，则长度取上一个key的值
                length = Integer.parseInt(previousValue);
            }
            end = start + length;
            byte[] btValue = new byte[length];
            System.arraycopy(message, start, btValue, 0, length);
            start = end;
            String value = null;
            try {
                value = new String(btValue, "GBK").trim();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }
            analysisResultMap.put(key, value);
            previousValue = value;
        }
        return true;
    }

    public String getValue(String key){
        return analysisResultMap.get(key);
    }

    public boolean isEmpty(){
        return analysisLengthMap.isEmpty();
    }

    public void clear(){
        analysisLengthMap.clear();
        analysisResultMap.clear();
    }

    public Map<String, String> getAnalysisResultMap() {
        return analysisResultMap;
    }
}
