package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.RDICeleTech;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

public class HttpUtils {
    private static final String ADDR= RDICeleTech.DEBUG?"http://localhost:26888/":"http://www.davisoft.cn:26888/";
    public static URL getFullUrl(String shortUrl){
        try {
            return new URL(ADDR + shortUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sendRequest(String type, String shortUrl, String... params){
        return sendRequestFullUrl(type,ADDR+shortUrl,params);


    }
    public static String sendRequestFullUrl(String type, String fullUrl, String... params){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_2);
        switch (type){

            case "post" ->
                    builder=builder.POST(concatParams(params)).uri(URI.create(fullUrl));
            case "put" ->
                    builder=builder.PUT(concatParams(params)).uri(URI.create(fullUrl));
            case "delete" ->
                    builder = builder.DELETE().uri(URI.create(fullUrl+"?"+concatParamString(params)));
            default->
                    builder=builder.GET().uri(URI.create(fullUrl+"?"+concatParamString(params)));
        }

        HttpRequest request = builder//.setHeader("User-Agent", "Minecraft-Client")
                //.header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return  response.body() ;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

   /* public static ApiResponse sendRequest(String type, String shortUrl,String... params){
        String requestRaw = sendRequestRaw(type, shortUrl, params);
        ApiResponse response = new Gson().fromJson(requestRaw, ApiResponse.class);
        return response;
    }

    public static ApiResponse sendRequestV2(String type, String shortUrl,String... params){
        String requestRaw = sendRequestRaw(type, shortUrl, params);
        JsonObject rootObj = JsonParser.parseString(requestRaw).getAsJsonObject();
        //出现错误 handler
        JsonObject errorObj = rootObj.getAsJsonObject("error");
        if (errorObj != null) {
            String errorMsg = errorObj.get("message").getAsString();
            throw new IslandException(errorMsg);
        }
        //-------------
        JsonObject succObj = rootObj.getAsJsonObject("response");
        if(succObj != null){
            String succMsg = succObj.get("message")==null?"":succObj.get("message").getAsString();
            ApiResponse response = new ApiResponse<>("success",succMsg,succObj.get("body")==null?"":succObj.get("body").toString());
            return response;
        }
        return null;
    }*/
    //类名= url POST
    /*public static <T extends Serializable> void asyncSendObject(T object){
        asyncSendObject(object.getClass().getSimpleName(),object,"");
    }
    //param &开头 POST
    public static <T extends Serializable> void asyncSendObject(String shortUrl, T object, String params){
        asyncSendObject("POST",shortUrl,object,params);

    }
    //param &开头
    public static <T extends Serializable> void asyncSendObject(String type,String shortUrl, T object, String params){
        ThreadPool.newThread(()-> sendRequest(type,shortUrl, "obj="+new Gson().toJson(object),params));
    }*/
    private static HttpRequest.BodyPublisher concatParams(String ... params){
        return HttpRequest.BodyPublishers.ofString(concatParamString(params));
    }
    private static String concatParamString(String ... params){
        StringBuilder sb = new StringBuilder();
        Arrays.stream(params).forEach((param)->{
            sb.append(param);
            sb.append("&");
        });
        return sb.toString();
    }

}