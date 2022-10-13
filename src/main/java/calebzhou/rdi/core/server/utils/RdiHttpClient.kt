package calebzhou.rdi.core.server.utils

import calebzhou.rdi.core.server.RdiCoreServer
import calebzhou.rdi.core.server.RdiSharedConstants
import com.google.gson.reflect.TypeToken
import it.unimi.dsi.fastutil.Pair
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object RdiHttpClient {
    private const val CONNECTION_TIME_OUT = 10
    private var client: OkHttpClient? = null

    init {
        try {
            client = if (RdiSharedConstants.DEBUG) unsafeOkHttpClient else OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .connectionPool(ConnectionPool(32, 60, TimeUnit.SECONDS))
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val ADDR = if (RdiSharedConstants.DEBUG) "127.0.0.1" else "www.davisoft.cn"
    @JvmStatic
	@SafeVarargs
    fun sendRequestAsyncResponseless(type: String, url: String, vararg params: Pair<String, Any>) {
        ThreadPool.newThread { sendRequest(type, url, *params) }
    }

    @SafeVarargs
    fun <T> sendRequest(
        resultClass: Class<T>,
        type: String,
        url: String,
        vararg params: Pair<String, Any>
    ): ResultData<T> {
        if (RdiSharedConstants.DEBUG) RdiCoreServer.LOGGER.info("HTTP发送{} {} {}", type, url, params)
        val okreq = Request.Builder()
        val urlBuilder: HttpUrl.Builder = ("https://$ADDR:26837$url").toHttpUrlOrNull()!!.newBuilder()
        val bodyFromParams = getFormBodyFromParams(*params)
        when (type) {
            "post" -> okreq.post(bodyFromParams)
            "put" -> okreq.put(bodyFromParams)
            "delete" -> okreq.delete(bodyFromParams)
            else -> {
                okreq.get()
                assembleUrlBuilderFromParams(urlBuilder, *params)
            }
        }
        okreq.url(urlBuilder.build())
        var response: Response? = null
        response = try {
            client!!.newCall(okreq.build()).execute()
        } catch (e: IOException) {
            RdiCoreServer.LOGGER.info("请求出现错误：" + e.message + e.cause)
            return ResultData<Any>(-404, "请求超时", e.message + e.cause)
        }
        var respStr: String? = null
        try {
            response.body.use { body -> respStr = body!!.string() }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (RdiSharedConstants.DEBUG) RdiCoreServer.LOGGER.info("HTTP响应 {}", respStr)
        return RdiSerializer.GSON.fromJson(
            respStr,
            if (resultClass == null) ResultData::class.java else TypeToken.getParameterized(
                ResultData::class.java, resultClass
            ).type
        )
    }

    @JvmStatic
	@SafeVarargs
    fun sendRequest(type: String?, url: String, vararg params: Pair<String, Any>): ResultData<*> {
        return sendRequest<Any>(null, type, url, *params)
    }

    @SafeVarargs
    private fun getFormBodyFromParams(vararg params: Pair<String, Any>): FormBody {
        val builder = FormBody.Builder()
        for (param in params) {
            builder.add(param.left(), param.right().toString())
        }
        return builder.build()
    }

    @SafeVarargs
    private fun assembleUrlBuilderFromParams(urlBuilder: HttpUrl.Builder, vararg params: Pair<String, Any>) {
        for (param in params) {
            urlBuilder.addQueryParameter(param.left(), param.right().toString())
        }
    }

    // Create a trust manager that does not validate certificate chains
    private val unsafeOkHttpClient: OkHttpClient
        // Install the all-trusting trust manager
        // Create an ssl socket factory with our all-trusting manager
        private get() = try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier({ hostname, session -> true })
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
}
