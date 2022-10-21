package com.example.buscacepeendereo.Repository

import com.example.buscacepeendereo.Model.Cep
import com.example.buscacepeendereo.Util.RetrofitInstance
import retrofit2.Call
import retrofit2.Response

class MainRepository {


    fun getEndereco(cep: String) : Call<Cep> {

        return RetrofitInstance.api.getEndereco(cep)

    }

    fun getCep(uf: String, cidade: String, logradouro: String) : Call<List<Cep>>{

        return  RetrofitInstance.api.getCep(uf, cidade, logradouro)

    }

}