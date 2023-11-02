package com.example.csscorechallenge.data.service

import com.example.csscorechallenge.utils.Constants.BASE_URL
import com.example.csscorechallenge.utils.Constants.BEARER_TOKEN
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Base64

object AppServiceFactory {

    private const val API_HEADER_PARAMETER = "Authorization"
    private const val API_HEADER_VALUE_PREFIX = "Bearer "

    fun makeAppService(): AppService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                addHeaderAuthorization(chain)
            }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(AppService::class.java)
    }

    private fun addHeaderAuthorization(chain: Interceptor.Chain): Response {
        return if (chain.request().header(API_HEADER_PARAMETER) == null) {
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .header(
                        API_HEADER_PARAMETER,
                        API_HEADER_VALUE_PREFIX + String(Base64.getDecoder().decode(BEARER_TOKEN))
                    ).build()
            )
        } else {
            chain.proceed(chain.request())
        }
    }

}