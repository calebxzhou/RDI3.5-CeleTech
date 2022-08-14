package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.RDICeleTech;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.apache.http.Consts;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class HttpUtils {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final String ADDR= RDICeleTech.DEBUG?"http://localhost:26889/":"http://www.davisoft.cn:26889/";
    public static String sendRequest(String type, String shortUrl, String... params){
        return sendRequestFullUrl(type,ADDR+shortUrl,params);

    }
    public static Map<String, String> setBodyParamsForRequest(String... params){
        Map<String, String> data = new HashMap<>();
        for (String param : params) {
            String[] split = param.split("=");
            data.put(split[0],split[1]);
        }
        return data;
    }
    public static String sendRequestFullUrl(String type, String fullUrl, String... params){
        HttpRequest.Builder builder = HttpRequest.newBuilder().headers("Content-Type", "application/x-www-form-urlencoded");



        final Map<String, String> data = setBodyParamsForRequest(params);
        String form = data.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        switch (type){
                case "post" -> {
                    builder.uri(URI.create(fullUrl)).POST(HttpRequest.BodyPublishers.ofString(form));
                }
                case "put" ->{
                    builder.uri(URI.create(fullUrl)).PUT(HttpRequest.BodyPublishers.ofString(form));
                }
                case "delete" ->{
                    builder.uri(URI.create(fullUrl+"?"+concatParamString(params))).DELETE();
                }
                default->
                        builder.uri(URI.create(fullUrl+"?"+concatParamString(params))).GET();
            }
        HttpResponse<String> send = null;
        try {
            send = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return send.body();
    }

    private static String concatParamString(String ... params){
        StringBuilder sb = new StringBuilder();
        for (int i = 0, paramsLength = params.length; i < paramsLength; i++) {
            String param = params[i];
            sb.append(param);
            //最后一个参数末尾不加&
            if(i<paramsLength-1)
                sb.append("&");
        }
        return sb.toString();
    }

    public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}