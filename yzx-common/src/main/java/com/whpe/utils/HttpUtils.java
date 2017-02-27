package com.whpe.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by chengpei on 2017/2/26.
 */
public class HttpUtils {
    protected static Log logger = LogFactory.getLog(HttpUtils.class);

    private static CloseableHttpClient httpClient;

    static {
        httpClient = HttpClients.createDefault();
    }

    /**
     * 发送HTTP post请求
     * @param url
     * @param params
     * @param encoding
     * @return
     */
    public static String urlPost(String url, Map<String, String> params, String encoding) throws IOException {
        HttpPost post = new HttpPost(url);
        logger.debug("发送HTTP请求 == " + url);
        if(params != null && params.size() > 0){
            logger.debug("参数 == " + JSON.toJSONString(params));
            List<NameValuePair> requestParams = new ArrayList<NameValuePair>();
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
            while (iterator.hasNext()){
                Map.Entry<String, String> next = iterator.next();
                requestParams.add(new BasicNameValuePair(next.getKey(), next.getValue()));
            }
            post.setEntity(new UrlEncodedFormEntity(requestParams, StringUtils.isEmpty(encoding)?"UTF-8":encoding));
        }
        CloseableHttpResponse response = httpClient.execute(post);
        HttpEntity responseEntity = response.getEntity();
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
        String responseInfo = reader.readLine();
        logger.debug("返回 == " + responseInfo);
        return responseInfo;
    }

    public static String urlPost(String url, Map<String, String> params) throws IOException {
        return urlPost(url, params, null);
    }

}
