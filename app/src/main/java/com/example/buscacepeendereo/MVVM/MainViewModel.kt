package com.example.buscacepeendereo.MVVM

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buscacepeendereo.Model.Cep
import com.example.buscacepeendereo.Repository.MainRepository
import com.example.buscacepeendereo.Resultado
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback
import kotlin.math.log

class MainViewModel(private val repository: MainRepository): ViewModel() {

    val endereco : MutableLiveData<Cep> = MutableLiveData()

    val errorEndereco : MutableLiveData<String> = MutableLiveData()

    val cep : MutableLiveData<List<Cep>> = MutableLiveData()

    val errorCep : MutableLiveData<String> = MutableLiveData()

    fun getEndereco(cep: String) {

        repository.getEndereco(cep).enqueue(object : Callback<Cep>{
            override fun onResponse(call: Call<Cep>, response: Response<Cep>) {

                if (response.isSuccessful){

                    errorEndereco.value = "success"
                    endereco.postValue(response.body())
                    Log.e("RESPONSE", "success")

                }else {

                    Log.e("ERROR", "Failure1")
                    errorEndereco.value = "error"

                }
            }

            override fun onFailure(call: Call<Cep>, t: Throwable) {
                errorEndereco.postValue(t.message)
                Log.e("ERROR", "Failure")
            }

        })
    }

    fun getCep(uf: String, cidade: String, logradouro: String){

        val response1 = repository.getCep(uf, cidade, logradouro)
        response1.enqueue(object : Callback<List<Cep>>{
            override fun onResponse(call: Call<List<Cep>>, response: Response<List<Cep>>) {

                if (response.isSuccessful && response.body() != null){
                    errorCep.value = "success"
                    cep.postValue(response.body())
                }else {
                    Log.e("ERROR", "isNotSuccessful")
                    errorCep.value = "error"
                }
            }

            override fun onFailure(call: Call<List<Cep>>, t: Throwable) {
                Log.e("ERROR", "onFailure")
                errorCep.postValue(t.message)
            }

        })
    }
}