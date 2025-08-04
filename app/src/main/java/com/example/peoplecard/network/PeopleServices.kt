package com.example.peoplecard.network

import com.example.peoplecard.model.PeopleResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

val client = OkHttpClient.Builder()
    .build()
val retrofit = Retrofit.Builder()
        .baseUrl("https://randomuser.me/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    interface RetrofitServices {
        @GET("api/")
        suspend fun getPeopleFromAPI(
            @Query("results") results:Int = 1,
            @Query("apiKey") apiKey:String = "0NEL-4D1B-KYRZ-5ZPP"
        ): PeopleResponse
    }

    object PeopleApi{
        val retrofitServices: RetrofitServices by lazy {
            retrofit.create(RetrofitServices::class.java)
        }
    }
