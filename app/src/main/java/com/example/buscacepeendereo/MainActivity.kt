package com.example.buscacepeendereo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.buscacepeendereo.MVVM.MainViewModel
import com.example.buscacepeendereo.MVVM.MainViewModelFactory
import com.example.buscacepeendereo.Model.Cep
import com.example.buscacepeendereo.Repository.MainRepository
import com.example.buscacepeendereo.Util.RetrofitInstance
import com.example.buscacepeendereo.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //Spinner config
        val estadoList: ArrayList<String> = arrayListOf(
            "- - -", "AC", "AL", "AM", "AP", "BA", "CE", "DF",
            "ES", "GO", "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ", "RN", "RO",
            "RR", "RS", "SC", "SE", "SP", "TO"
        )

        val spinnerList = ArrayList<String>()

        for (estado in estadoList) {
            spinnerList.add(estado)
        }

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_layout, spinnerList
        )


        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                if (binding.spinner.selectedItem == estadoList[0]) {

                    Toast.makeText(
                        this@MainActivity,
                        binding.spinner.selectedItem.toString()
                                + " selecionado!", Toast.LENGTH_SHORT
                    )

                } else {

                    Toast.makeText(
                        this@MainActivity,
                        binding.spinner.selectedItem.toString()
                                + " selecionado!", Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //ViewModel Config
        val repository = MainRepository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

    }

    override fun onStart() {
        super.onStart()

        Log.e("omStart", "start")

        //button config.
        binding.button.setOnClickListener {

            checkDataInput()

        }
    }

    override fun onPause() {
        super.onPause()

        Log.e("omPause", "Pause")
    }

    override fun onResume() {
        super.onResume()

        Log.e("omResume", "Resume")

        //Network state
        if (!checkInternetConnection(this)) {

            val snackbar = Snackbar.make(
                binding.mainActivity,
                "Verifique sua conexão com a internet!", Snackbar.LENGTH_LONG
            )
            snackbar.setBackgroundTint(Color.RED)
            snackbar.show()

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        startActivity(intent)
        finish()
        System.exit(0)
    }

    private fun checkDataInput() {

        val intent = Intent(this, Resultado::class.java)
        val cepEditText = binding.editTextCEP.text.toString()
        val cepBellow = cepEditText
        val cidade = binding.editTextCidade.text.toString()
        val logradouro = binding.editTextTextLogradouro.text.toString()

        if (!checkInternetConnection(this)) {

            val snackbar = Snackbar.make(
                binding.mainActivity,
                "Verifique sua conexão com a internet!", Snackbar.LENGTH_LONG
            )
            snackbar.setBackgroundTint(Color.RED)
            snackbar.show()

        } else if (cepEditText != "") {

            viewModel.getEndereco(cepEditText)

            viewModel.errorEndereco.observe(this, Observer { error ->


                if(error == "success"){

                    viewModel.endereco.observe(this, Observer {

                        Log.e("EditText", cepEditText)

                    })

                }else{

                    val snackbar = Snackbar.make(
                        binding.mainActivity,
                        "CEP incorreto!", Snackbar.LENGTH_SHORT
                    )

                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()

                }

            })

            intent.putExtra("cepEditText", cepEditText)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

            startActivity(intent)

        } else if (cidade != "" && logradouro != "") {

            buscarCep()

        } else{

            val snackbar = Snackbar.make(
                binding.mainActivity,
                "Preencha todos os campos corretamente!", Snackbar.LENGTH_SHORT
            )
            snackbar.setBackgroundTint(Color.RED)
            snackbar.show()

        }
    }

    private fun checkProgressBar(list: List<Cep?>) {
        if (list.size.compareTo(0) != 0)
            binding.progressBar2.visibility = View.GONE
    }


    private fun checkInternetConnection(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false

            }

        } else {

            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


    fun buscarCep() {

        val intent = Intent(this, Resultado::class.java)
        val uf = binding.spinner.selectedItem.toString()
        val cidade = binding.editTextCidade.text.toString()
        val logradouro = binding.editTextTextLogradouro.text.toString()

        if (cidade != "" && logradouro != "") {

            viewModel.getCep(uf, cidade, logradouro)


            viewModel.errorCep.observe(this, Observer { error ->

                if (error == "success") {

                    viewModel.cep.observe(this, Observer {


                    })

                } else {

                    val snackbar = Snackbar.make(
                        binding.mainActivity,
                        "Endereço incorreto!", Snackbar.LENGTH_SHORT
                    )
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()

                }

                intent.putExtra("estado", uf)
                intent.putExtra("cidade", cidade)
                intent.putExtra("logradouro", logradouro)

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

                startActivity(intent)

            })

        }
    }
}