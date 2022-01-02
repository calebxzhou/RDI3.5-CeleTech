package calebzhou.rdimc.celestech.utils;

import com.google.gson.Gson;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static calebzhou.rdimc.celestech.constant.ServiceConstants.ADDR;

public class HttpUtils {
    //类名= url
    public static <T extends Serializable> void postObject(T object){
        postObject(object.getClass().getSimpleName(),object,"");
    }
    //param &开头
    public static <T extends Serializable> void postObject(String shortUrl,T object,String params){
        ThreadPool.newThread(()->{
            post(shortUrl, "obj="+new Gson().toJson(object),params);
        });

    }

    public static String post(String shortUrl,String... params){
        StringBuilder sb = new StringBuilder();
        Arrays.stream(params).forEach((param)->{
            sb.append(param);
            sb.append("&");
        });
        return doPost(ADDR+shortUrl,sb.toString());
    }

    public static String doGet(String fullUrl) {
        long t1= System.currentTimeMillis();
        HttpURLConnection connection = null;
        InputStream is;
        BufferedReader br;
        String result = null;
        try {
            URL url = new URL(fullUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    //sbf.append("\r\n");
                }
                result = sbf.toString();
            }else{
                return "HTTP服务错误："+connection.getResponseCode();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            return "HTTP服务错误："+e.getMessage();
        } finally {
            connection.disconnect();// 关闭远程连接
        }
        long t2= System.currentTimeMillis();
        ServerUtils.recordHttpReqDelay(t1,t2);
        return result;
    }

    public static String doPost(String httpUrl, String param) {
        long startTime = System.currentTimeMillis();
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的

            os.write(param.getBytes(StandardCharsets.UTF_8));

            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    //sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        long endTime = System.currentTimeMillis();
        ServerUtils.recordHttpReqDelay(startTime,endTime);
        return result;
    }
}