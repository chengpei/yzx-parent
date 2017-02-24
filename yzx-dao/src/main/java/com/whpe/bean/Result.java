package com.whpe.bean;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class Result {

    private boolean success;

    private String message;

    private Map<String, Object> data;

    public Result(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public Result(boolean success, String message, Map<String, Object> data){
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Result(boolean success){
        this.success = success;
    }

    public void put(String key, Object value){
        if(data == null){
            data = new HashMap<String, Object>();
        }
        data.put(key, value);
    }

    public void putAll(Map<String, Object> dataMap){
        if(data == null){
            data = new HashMap<String, Object>();
        }
        data.putAll(dataMap);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
