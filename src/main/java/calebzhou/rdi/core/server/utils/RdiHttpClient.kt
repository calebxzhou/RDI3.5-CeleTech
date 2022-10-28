package calebzhou.rdi.core.server.utils

import calebzhou.rdi.core.server.constant.RdiSharedConstants
import calebzhou.rdi.core.server.logger
import calebzhou.rdi.core.server.model.ResponseData
import calebzhou.rdi.core.server.utils.RdiSerializer.Companion.gson
import com.google.gson.reflect.TypeToken
import it.unimi.dsi.fastutil.Pair
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.reflect.KClass


object RdiHttpClient {
    const val CONNECTION_TIME_OUT = 15
    private val client: OkHttpClient =
        if (RdiSharedConstants.DEBUG)
            unsafeOkHttpClient
        else OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(32, 60, TimeUnit.SECONDS))
            .build()

    fun sendRequestAsyncResponseless(type: String, url: String, vararg params: Pair<String, Any>) {
        ThreadPool.newThread { sendRequest(type, url, *params) }
    }
    fun <T : Any> sendRequest(
        resultClass: KClass<T>?,
        type: String,
        url: String,
        vararg params: Pair<String, Any>
    ): ResponseData<T> {
        if (RdiSharedConstants.DEBUG)
            logger.info("HTTP发送{} {} {}", type, url, params)
        val okreq = Request.Builder()
        val urlBuilder= (RdiSharedConstants.SERVICE_ADDR + url).toHttpUrl().newBuilder()
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
        val response = try {
            client.newCall(okreq.build()).execute()
        } catch (e: IOException) {
            logger.info("请求出现错误：" + e.message + e.cause)
            return ResponseData(-404, "请求超时.${e.message},${e.cause}",null)
        }

        val respStr: String = response.body.use { body -> body!!.string() }
        if (RdiSharedConstants.DEBUG)
            logger.info("HTTP响应 {}", respStr)
        return gson.fromJson(respStr,
            if (resultClass == null)
                ResponseData::class.java
            else TypeToken.getParameterized(
                ResponseData::class.java, resultClass.java
            ).type
        )
    }

    @SafeVarargs
    fun sendRequest(type: String, url: String, vararg params: Pair<String, Any>): ResponseData<*> {
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
    val unsafeOkHttpClient: OkHttpClient
        // Install the all-trusting trust manager
        // Create an ssl socket factory with our all-trusting manager
        get() = try {
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
            builder.hostnameVerifier { hostname, session -> true }
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
}
