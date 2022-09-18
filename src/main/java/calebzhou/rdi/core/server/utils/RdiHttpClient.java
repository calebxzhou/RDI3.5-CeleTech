package calebzhou.rdi.core.server.utils;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.RdiSharedConstants;
import calebzhou.rdi.core.server.model.ResultData;
import calebzhou.rdi.core.server.thread.RdiHttpRequest;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.entity.player.Player;
import okhttp3.*;

import javax.annotation.Nullable;
import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class RdiHttpClient {
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



	@SafeVarargs
	public static ResultData sendRequest(String type, String url, Pair<String, Object>... params){
        Request.Builder okreq = new Request.Builder();
        okreq.url("https://"+ADDR+":26890"+url);
		final FormBody bodyFromParams = getFormBodyFromParams(params);

		switch (type){
			case "post" -> okreq.post(bodyFromParams);
            case "put" -> okreq.put(bodyFromParams);
            case "delete" -> okreq.delete(bodyFromParams);
            default -> okreq.get();
        }
		Response response = null;
		try {
			response =client.newCall(okreq.build()).execute();
		} catch (IOException e) {
			RdiCoreServer.LOGGER.info("请求出现错误："+e.getMessage()+e.getCause());
			return new ResultData(-404,"请求超时",e.getMessage()+e.getCause());
		}
		return RdiSerializer.GSON.fromJson(response.body() + "", ResultData.class);
	}


	@SafeVarargs
	private static FormBody getFormBodyFromParams(Pair<String,Object>... params){
		if(params==null)
			return null;
		FormBody.Builder builder = new FormBody.Builder();
		for (Pair<String, Object> param : params) {
			builder.add(param.left(), String.valueOf(param.right()));
		}
		return builder.build();
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
}
/* public static Consumer<Exception> universalHttpRequestFailureConsumer(Player player){
        return exception -> {
            PlayerUtils.sendChatMessage(player, PlayerUtils.RESPONSE_ERROR, "请求出现错误：请立刻联系服主！" + exception.toString());
            if(RdiSharedConstants.DEBUG) exception.printStackTrace();
        };
    }
    public static void universalHttpRequestFailureConsumer(Exception exception){
            RdiCoreServer.LOGGER.info("请求出现错误：" + exception.toString() + " " + exception.getLocalizedMessage());
            if(RdiSharedConstants.DEBUG) exception.printStackTrace();
    }
    public static void sendRequestAsync(RdiHttpRequest request, Player player, Consumer<ResultData> doOnSuccess){
        sendRequestAsync(request,doOnSuccess, RdiHttpClient.universalHttpRequestFailureConsumer(player));
    }*/

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
