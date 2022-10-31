package com.example.buscacepeendereo.Rest

import com.example.buscacepeendereo.Model.Cep
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {

    @GET("ws/{cep}/json/")
    fun getEndereco(@Path("cep") cep : String) : Call<Cep>

    @GET("/ws/{uf}/{cidade}/{logradouro}/json/")
    fun getCep(
        @Path("uf", encoded = true) uf: String,
        @Path("cidade", encoded = true) cidade: String,
        @Path("logradouro", encoded = true) logradouro: String
    ) : Call<List<Cep>>

}