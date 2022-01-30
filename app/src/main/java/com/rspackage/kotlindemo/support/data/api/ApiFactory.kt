package com.rspackage.kotlindemo.support.data.api

import android.util.Log
import com.rspackage.kotlindemo.support.DemoApplication
import com.rspackage.kotlindemo.support.data.base_url
import com.rspackage.kotlindemo.support.data.endpoint
import com.rspackage.kotlindemo.support.data.responses.Animal
import com.rspackage.kotlindemo.support.network.ConnectionInterceptor
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers
import java.util.concurrent.TimeUnit


interface ApiFactory {
    companion object {
        const val TAG = "ApiFactory"
        private const val cacheSize = (10 * 1024 * 1024).toLong() //10mb

        operator fun invoke(connectionInterceptor: ConnectionInterceptor): ApiFactory {
            val httpLoggingInterceptor =
                HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Log.d(TAG, message)
                    }
                }).apply {
//                level = if (debug) HttpLoggingInterceptor.Level.BODY
//                else HttpLoggingInterceptor.Level.NONE
                    HttpLoggingInterceptor.Level.BODY
                }

            val cache = Cache(DemoApplication.instance.cacheDir, cacheSize)

            val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor { chain ->
                    val request = chain.request()
                    val maxAge = 60 * 5//seconds
                    val maxStale = 60 * 60 * 24 * 1 // tolerate 1 day stale
                    request.newBuilder()
                        //.cacheControl(CacheControl.Builder().maxAge(maxAge, TimeUnit.SECONDS).build())
                        .header("Cache-Control", "public, max-age=$maxAge")
                        //.header("Cache-Control", "max-stale=$maxStale")
                        .removeHeader("Pragma")
                        .build()

                    chain.proceed(request)
                }
                .addInterceptor(httpLoggingInterceptor)
                //.addInterceptor(cacheInterceptor)
                .addInterceptor(connectionInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ApiFactory::class.java)
        }
    }

    @Headers("Content-Type:application/json")
    @GET(endpoint)
    suspend fun getData(): Response<Animal>

}
