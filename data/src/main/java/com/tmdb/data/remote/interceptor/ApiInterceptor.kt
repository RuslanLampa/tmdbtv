package com.tmdb.data.remote.interceptor

import com.tmdb.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody

class ApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val apiKey: String = BuildConfig.API_KEY

        val request = original.newBuilder()
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("accept", "application/json")
            .url(originalHttpUrl)
            .build()

        return try {
            chain.proceed(request)
        } catch (e: Exception) {
            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(999)
                .message("Something went wrong")
                .body(ResponseBody.create(null, "Something went wrong"))
                .build()
        }
    }
}