package com.goopal.rate.jobs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by junze on 18/3/9.
 */
public class OkHttpClientUtil {
    private static int connTimeOut = 5;
    private static int readTimeOut = 20;
    private static int writeTimeOut = 10;
    private static OkHttpClient client = null;

    static {
        client = new OkHttpClient.Builder()
            .connectTimeout(5L, TimeUnit.SECONDS)
            .readTimeout(20L, TimeUnit.SECONDS)
            .writeTimeout(10L, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();
    }

    public OkHttpClientUtil() {
    }

    public static String doGet(String host, String path, Map<String, String> headers, Map<String, String> querys) throws Exception {
        StringBuffer url = new StringBuffer(host + (path == null?"":path));
        if(querys != null) {
            url.append("?");
            Iterator iterator = querys.entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry<String, String> e = (Map.Entry)iterator.next();
                url.append((String)e.getKey()).append("=").append((String)e.getValue() + "&");
            }
            url = new StringBuffer(url.substring(0,url.length()-1));
        }
        Request.Builder requestBuilder = new Request.Builder();
        if(headers != null && headers.size() > 0) {
            Iterator iterator = headers.keySet().iterator();
            while(iterator.hasNext()) {
                String key = (String)iterator.next();
                requestBuilder.addHeader(key, (String)headers.get(key));
            }
        }
        Request request = (requestBuilder).url(url.toString()).build();
        Response response = client.newCall(request).execute();
        String responseStr = response.body() == null?"":response.body().string();
        return responseStr;
    }

    public static String doPost(String host, String path, Map<String, String> headers, Map<String, String> querys) throws Exception {
        okhttp3.FormBody.Builder builder = new okhttp3.FormBody.Builder();
        Iterator iterator = querys.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry<String, String> elem = (Map.Entry)iterator.next();
            builder.add((String)elem.getKey(), (String)elem.getValue());
        }

        RequestBody body = builder.build();
        Request.Builder requestBuilder = (new Request.Builder()).url(host + path);
        if(headers != null && headers.size() > 0) {
            Iterator iteratorHeader = headers.keySet().iterator();
            while(iteratorHeader.hasNext()) {
                String key = (String)iteratorHeader.next();
                requestBuilder.addHeader(key, (String)headers.get(key));
            }
        }

        Request requet = requestBuilder.post(body).build();
        Response response = client.newCall(requet).execute();
        String responseStr = response.body() == null?"":response.body().string();
        return responseStr;
    }

    public static String doPost(String host, String path, Map<String, String> headers, String sendMessage) throws Exception {

        RequestBody body = FormBody.create(MediaType.parse("application/json; charset=utf-8"), sendMessage);;
        Request.Builder requestBuilder = (new Request.Builder()).url(host + path);
        if(headers != null && headers.size() > 0) {
            Iterator iteratorHeader = headers.keySet().iterator();
            while(iteratorHeader.hasNext()) {
                String key = (String)iteratorHeader.next();
                requestBuilder.addHeader(key, (String)headers.get(key));
            }
        }

        Request requet = requestBuilder.post(body).build();
        Response response = client.newCall(requet).execute();
        String responseStr = response.body() == null?"":response.body().string();
        return responseStr;
    }

    public static String doPut(String host, String path, Map<String, String> headers, Map<String, String> querys) throws Exception {
        okhttp3.FormBody.Builder builder = new okhttp3.FormBody.Builder();
        Iterator iterator = querys.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry<String, String> elem = (Map.Entry)iterator.next();
            builder.add((String)elem.getKey(), (String)elem.getValue());
        }

        RequestBody body = builder.build();
        Request.Builder requestBuilder = (new Request.Builder()).url(host + path);
        if(headers != null && headers.size() > 0) {
            Iterator iteratorHeader = headers.keySet().iterator();
            while(iteratorHeader.hasNext()) {
                String key = (String)iteratorHeader.next();
                requestBuilder.addHeader(key, (String)headers.get(key));
            }
        }

        Request requet = requestBuilder.put(body).build();
        Response response = client.newCall(requet).execute();
        String responseStr = response.body() == null?"":response.body().string();
        return responseStr;
    }
}
