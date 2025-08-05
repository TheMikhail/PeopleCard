package com.example.peoplecard.network

import com.example.peoplecard.model.PeopleResponse
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


var certPinner = CertificatePinner.Builder()
    .add(
        "appmattus.com",
        "sha256/4hw5tz+scE+TW+mlai5YipDfFWn1dqvfLG+nU7tq1V8="
    )
    .build()
val client = OkHttpClient.Builder()
    .certificatePinner(certPinner)
    .build()
val retrofit = Retrofit.Builder()
    .baseUrl("https://randomuser.me/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .build()

interface RetrofitServices {
    @GET("api/")
    suspend fun getPeopleFromAPI(
        @Query("results") results: Int = 1,
        @Query("apiKey") apiKey: String = "0NEL-4D1B-KYRZ-5ZPP"
    ): PeopleResponse
}

object PeopleApi {
    val retrofitServices: RetrofitServices by lazy {
        retrofit.create(RetrofitServices::class.java)
    }
}
