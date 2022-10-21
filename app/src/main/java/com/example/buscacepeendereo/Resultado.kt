package com.example.buscacepeendereo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buscacepeendereo.MVVM.MainViewModel
import com.example.buscacepeendereo.MVVM.MainViewModelFactory
import com.example.buscacepeendereo.Model.Cep
import com.example.buscacepeendereo.RecyclerViewAdapter.RecyclerAdapter
import com.example.buscacepeendereo.Repository.MainRepository
import com.example.buscacepeendereo.databinding.ActivityResultadoBinding
import com.google.android.material.snackbar.Snackbar

class Resultado : AppCompatActivity() {

    lateinit var binding: ActivityResultadoBinding
    lateinit var viewModel: MainViewModel
    private val recyclerAdapter by lazy { RecyclerAdapter() }

    //intent extras values
    val cepEditText by lazy { intent.getStringExtra("cepEditText") }
    val logradouro by lazy { intent.getStringExtra("logradouro") }
    val cidade by lazy { intent.getStringExtra("cidade") }
    val estado by lazy { intent.getStringExtra("estado") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityResultadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerSetUp()

        binding.btnBack.setOnClickListener {

            onBackPressed()

        }
    }

    override fun onStart() {
        super.onStart()

        showResult()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intentResult = Intent(this, MainActivity::class.java)
        intentResult.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intentResult.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intentResult)
    }

    fun recyclerSetUp() {

        binding.recyclerView.adapter = recyclerAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.hasFixedSize()

    }

    private fun showResult() {

        //ViewModel Config
        val repository = MainRepository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        if (cepEditText != null) {

            viewModel.getEndereco(cepEditText!!)

            viewModel.errorEndereco.observe(this, Observer { error ->

                if (error == "success"){

                    viewModel.endereco.observe(this, Observer {

                        checkProgressBar(listOf(it))

                        recyclerAdapter.setData(listOf(it))

                    })

                }else {

                    onBackPressed()

                }

            })


        } else if (estado != null && cidade != null && logradouro != null) {

            viewModel.getCep(estado!!, cidade!!, logradouro!!)

            viewModel.errorCep.observe(this, Observer { error ->

                if (error != "error"){

                    viewModel.cep.observe(this, Observer {

                        checkProgressBar(it)

                        recyclerAdapter.setData(it)

                    })

                }else {

                    onBackPressed()

                }


            })

        } else {

            val snackbar = Snackbar.make(
                binding.resultado,
                "Erro inesperado, tente novamente!", Snackbar.LENGTH_SHORT
            )
            snackbar.setBackgroundTint(Color.RED)
            snackbar.show()
        }
    }

    private fun checkProgressBar(list: List<Cep?>) {
        if (list.size.compareTo(0) != 0)
            binding.progressBar.visibility = View.GONE
    }

}