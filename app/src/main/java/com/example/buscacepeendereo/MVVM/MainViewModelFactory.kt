package com.example.buscacepeendereo.MVVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buscacepeendereo.Repository.MainRepository

class MainViewModelFactory(private val repository: MainRepository):
ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }

}