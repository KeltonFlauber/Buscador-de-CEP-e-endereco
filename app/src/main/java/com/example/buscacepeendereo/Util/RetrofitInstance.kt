package com.example.buscacepeendereo.Util

import com.example.buscacepeendereo.Rest.RetrofitService
import com.example.buscacepeendereo.Util.Constant.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val retrofit by lazy {

        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api : RetrofitService by lazy {

        retrofit.create(RetrofitService::class.java)

    }

}