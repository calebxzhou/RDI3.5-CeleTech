package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.thread.RdiHttpRequest;
import com.google.common.util.concurrent.FutureCallback;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.player.Player;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.function.Consumer;

public class HttpUtils {
    /*public static void main(String[] args) {
        sendRequestAsync(new RdiHttpRequest(RdiHttpRequest.Type.get,"island/e7f80658-17ff-4cd0-afab-a26da0e0224a"), System.out::println, Throwable::printStackTrace);
    }*/
    private static final OkHttpClient client = RDICeleTech.DEBUG?getUnsafeOkHttpClient():new OkHttpClient();

    private static final String ADDR=  (RDICeleTech.DEBUG?"127.0.0.1":"www.davisoft.cn");


    public static Consumer<Exception> universalHttpRequestFailureConsumer(Player player){
        return exception -> {
            TextUtils.sendChatMessage(player, MessageType.ERROR, "请求出现错误：" + exception.toString() + " " + exception.getLocalizedMessage());
            if(RDICeleTech.DEBUG) exception.printStackTrace();
        };
    }
    public static void universalHttpRequestFailureConsumer(Exception exception){
            RDICeleTech.LOGGER.error("请求出现错误：" + exception.toString() + " " + exception.getLocalizedMessage());
            if(RDICeleTech.DEBUG) exception.printStackTrace();
    }
    public static void sendRequestAsync(RdiHttpRequest request, Player player,Consumer<String> doOnSuccess){
        sendRequestAsync(request,doOnSuccess,HttpUtils.universalHttpRequestFailureConsumer(player));
    }
    public static void sendRequestAsync(RdiHttpRequest request, Consumer<String> doOnSuccess,Consumer<Exception> doOnFailure){
        Request.Builder okreq = new Request.Builder();
        okreq.url("https://"+ADDR+":26890/"+request.url);
        switch (request.type){
            case post -> okreq.post(request.getParamBody());
            case put -> okreq.put(request.getParamBody());
            case delete -> okreq.delete(request.getParamBody());
            default -> okreq.get();
        }
        client.newCall(okreq.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                doOnFailure.accept(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try (ResponseBody body = response.body()){
                    if (!response.isSuccessful()) {
                        System.err.println(response);
                        return;
                    }
                    doOnSuccess.accept(body.string());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}/*
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
*/

    /*public static String sendRequest(String type, String shortUrl, String... params){
        return sendRequestFullUrl(type,ADDR+shortUrl,params);

    }*/
   /*public static HttpUriRequest setBodyParamsForRequest(HttpUriRequestBase request, String... params){
        List<NameValuePair> paramList = new ObjectArrayList<>();
        for (String param : params) {
            String[] split = param.split("=");
            paramList.add(new BasicNameValuePair(split[0],split[1]));
        }
        request.setEntity(new UrlEncodedFormEntity(paramList, StandardCharsets.UTF_8));
        return null;
    }*/
   /*public static String sendRequestFullUrl(String type, String fullUrl, String... params){
        try(CloseableHttpClient client = HttpClients.custom().build()){
            HttpUriRequest request;
            switch (type){
                case "post" -> request=setBodyParamsForRequest( new HttpPost(URI.create(fullUrl)),params);
                case "put" ->
                        request=setBodyParamsForRequest(new HttpPut(URI.create(fullUrl)),params);
                case "delete" ->
                        request=new HttpDelete(URI.create(fullUrl+"?"+concatParamString(params)));
                default->
                        request=new HttpGet(URI.create(fullUrl+"?"+concatParamString(params)));
            }

            CloseableHttpResponse response = client.execute(request);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }*/
    /*public static String sendRequestFullUrl(String type, String fullUrl, String... params){
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
*/
   /* private static String concatParamString(String ... params){
        StringBuilder sb = new StringBuilder();
        for (int i = 0, paramsLength = params.length; i < paramsLength; i++) {
            String param = params[i];
            sb.append(param);
            //最后一个参数末尾不加&
            if(i<paramsLength-1)
                sb.append("&");
        }
        return sb.toString();
    }*/