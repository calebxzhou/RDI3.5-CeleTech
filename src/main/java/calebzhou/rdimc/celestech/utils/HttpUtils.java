package calebzhou.rdimc.celestech.utils;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.RdiSharedConstants;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.thread.RdiHttpRequest;
import net.minecraft.world.entity.player.Player;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class HttpUtils {
    /*public static void main(String[] args) {
        sendRequestAsync(new RdiHttpRequest(RdiHttpRequest.Type.get,"island/e7f80658-17ff-4cd0-afab-a26da0e0224a"), System.out::println, Throwable::printStackTrace);
    }*/
    private static final int CONNECTION_TIME_OUT = 10;
    private static OkHttpClient client;

    static {
        try {
            client = RdiSharedConstants.DEBUG ?
                    getUnsafeOkHttpClient() :
                    new OkHttpClient.Builder()
                            .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                            .connectionPool(new ConnectionPool(32, 60, TimeUnit.SECONDS))
                            .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String ADDR=  (RdiSharedConstants.DEBUG?"127.0.0.1":"www.davisoft.cn");


    public static Consumer<Exception> universalHttpRequestFailureConsumer(Player player){
        return exception -> {
            TextUtils.sendChatMessage(player, MessageType.ERROR, "请求出现错误：请立刻联系服主！" + exception.toString());
            if(RdiSharedConstants.DEBUG) exception.printStackTrace();
        };
    }
    public static void universalHttpRequestFailureConsumer(Exception exception){
            RDICeleTech.LOGGER.info("请求出现错误：" + exception.toString() + " " + exception.getLocalizedMessage());
            if(RdiSharedConstants.DEBUG) exception.printStackTrace();
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
        Response response = null;
        try {
            response = client.newCall(okreq.build()).execute();
            if(!response.isSuccessful()){
                doOnFailure.accept(new IOException("RDI_RESPONSE_FAIL"));
                return;
            }
            ResponseBody body = response.body();
            doOnSuccess.accept(body.string());
        } catch (Exception e) {
            doOnFailure.accept(e);
        }

    }
    /*response.enqueue(new Callback() {
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

              } catch (Exception e) {
                  e.printStackTrace();
              }

          }
      });*/
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
}