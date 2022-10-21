package com.example.buscacepeendereo.RecyclerViewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.buscacepeendereo.Model.Cep
import com.example.buscacepeendereo.R

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    private var registerList = emptyList<Cep>()

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val cep: TextView = view.findViewById(R.id.cep_result)
        val logradouro: TextView = view.findViewById(R.id.rua_result)
        val bairro: TextView = view.findViewById(R.id.bairro_result)
        val cidade: TextView = view.findViewById(R.id.city_result)
        val estado: TextView = view.findViewById(R.id.estado_result)
        val complemento: TextView = view.findViewById(R.id.complemento_result)

        val complemento_txt: TextView = view.findViewById(R.id.complemento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_layout, parent, false)

        return MyViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.cep.text = registerList[position].cep

        if(registerList[position].logradouro == ""){
            holder.bairro.text = holder.bairro.text
        }else{
            holder.logradouro.text = registerList[position].logradouro
        }

        if(registerList[position].bairro == ""){
            holder.bairro.text = holder.bairro.text
        }else{
            holder.bairro.text = registerList[position].bairro
        }

        holder.cidade.text = registerList[position].localidade
        holder.estado.text = registerList[position].uf

        if (registerList[position].complemento != ""){

            holder.complemento_txt.visibility = View.VISIBLE
            holder.complemento.visibility = View.VISIBLE

            holder.complemento.text = registerList[position].complemento

        }
    }

    override fun getItemCount(): Int {
        return registerList.size
    }

    fun setData(newList: List<Cep>){
        registerList = newList
        notifyDataSetChanged()
    }

}