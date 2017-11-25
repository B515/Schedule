package xyz.b515.schedule.api

import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object ZfRetrofit {
    private val server = "http://gdjwgl.bjut.edu.cn"
    private val headers = Headers.Builder()
            .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3014.0 Safari/537.36")
            .add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            .add("Accept-Encoding", "gzip, deflate, sdch")
            .add("Accept-Language", "zh-CN,en-US;q=0.8")
            .build()
    private val httpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor { chain -> chain.proceed(chain.request().newBuilder().headers(headers).build()) }
            .cookieJar(object : CookieJar {
                private val cookieStore = HashMap<String, List<Cookie>>()

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    cookieStore.put(url.host(), cookies)
                }

                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val cookies = cookieStore[url.host()]
                    return cookies ?: ArrayList()
                }
            })
            .build()
    val zfService: ZfService by lazy {
        Retrofit.Builder()
                .baseUrl(server)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(ZfService::class.java)
    }
}
