package com.yeepay.yop.library.oauth2;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * title: <br/>
 * description:描述<br/>
 * Copyright: Copyright (c)2016<br/>
 * Company: 易宝支付(YeePay)<br/>
 *
 * @author guoliang.li
 * @version 1.0.0
 * @since 16/10/11 下午5:27
 */
public class OAuth2Client {

//    private static String serverUrl="http://172.17.102.173:8064/yop-center";
    private static String serverUrl="http://10.151.30.80:18064/yop-center";
//    public static String serverUrl="https://open.yeepay.com/yop-center";
    private static String access_token="";
    private static String refresh_token="";

    /**
     * OAuth2授权过程
     * @param methodUrl
     * @param username
     * @param password
     * @return
     */
    public static Boolean getAuthenCredentials(String methodUrl, String username, String password, Map<String,String>paramsMap){
        OkHttpClient okHttpClient=new OkHttpClient();
        Response response=null;
        Boolean result=false;
        Gson json=new Gson();
        FormBody.Builder builder=new FormBody.Builder();
        for(Map.Entry<String,String>entry:paramsMap.entrySet()){
                if(!entry.getValue().isEmpty()){
//                    encodeMap.put(entry.getKey(), URLEncoder.encode(entry.getValue(),"utf-8"));
                    builder.add(entry.getKey(),entry.getValue());
                }
        }
        RequestBody requestBody=builder.add("username",username).add("password",password)
                .add("grant_type","password").add("scope","test").build();
        Request request=new Request.Builder().url(serverUrl+methodUrl).post(requestBody).build();
        try {
            response=okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!(response==null)&&response.isSuccessful()){
            result=true;
            String responseResult = response.body().toString();
            Authen authen=json.fromJson(responseResult,Authen.class);
            access_token=authen.getAccess_token();
            refresh_token=authen.getRefresh_token();
        }
        return result;
    }

    /**
     * 使用access_token鉴权后台的接口
     * @param methodUrl
     * @return
     */
    public static String oauthCertificate(String methodUrl,String accesstoken,Map<String,String>params){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=null;
        Response response=null;
        RequestBody requestBody=null;
        FormBody.Builder builderParams=new FormBody.Builder();
        String responseResult=null;
        StringBuilder builder=new StringBuilder();
        StringBuilder paramsToString=new StringBuilder();
        builder.append("Bearer").append(" ");
        if (null!=params){
            for(Map.Entry<String,String>entry:params.entrySet()){
                paramsToString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
//            builderParams.add(entry.getKey(),entry.getValue());
                try {
                    builderParams.add(entry.getKey(),URLEncoder.encode(entry.getValue(),"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            requestBody=builderParams.build();
            request=new Request.Builder().url(serverUrl+methodUrl).post(requestBody).header("X-YOP-AppKey","NjAwOSMxNTAxMDM5ODE0ODEx")
                    .header("Authorization",builder.append(accesstoken).toString()).build();
        }else {
            request=new Request.Builder().url(serverUrl+methodUrl).header("X-YOP-AppKey","NjAwOSMxNTAxMDM5ODE0ODEx")
                    .header("Authorization",builder.append(accesstoken).toString()).build();
        }
//        paramsToString.deleteCharAt(paramsToString.length()-1);
//        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"),
//                paramsToString.toString());
        try {
            response=okHttpClient.newCall(request).execute();
            if(!(response==null)&&response.isSuccessful()){
                responseResult = response.body().string();
                Log.e("methodUrl",methodUrl);
                Log.e("responseResult",responseResult);
            }else{
                throw new IOException(response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseResult;
    }

    public static String refreshApi(String methodUrl,String refreshToken){
        OkHttpClient okHttpClient=new OkHttpClient();
        Response response=null;
        String responseResult=null;
        RequestBody requestBody=new FormBody.Builder().add("refresh_token",refreshToken).add("grant_type","refresh_token").build();
        Request request=new Request.Builder().url(serverUrl+methodUrl).post(requestBody).build();
        try {
            response=okHttpClient.newCall(request).execute();
            if(!(response==null)&&response.isSuccessful()){
                responseResult = response.body().toString();
            }else{
                throw new IOException(response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseResult;
    }

}
