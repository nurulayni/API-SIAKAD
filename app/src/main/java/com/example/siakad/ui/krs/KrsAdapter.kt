package com.example.siakad.ui.krs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.siakad.R


class KrsAdapter(private var krsList: List<Krs>, private val onItemClickListener: KrsAdapter.OnItemClickListener) : RecyclerView.Adapter<KrsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KrsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jurusan, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaKrs: TextView = itemView.findViewById(R.id.krs)
    }

    override fun onBindViewHolder(holder: KrsAdapter.ViewHolder, position: Int) {
        val krs = krsList[position]

    }

    override fun getItemCount(): Int {
        return krsList.size
    }

    interface OnItemClickListener {
        fun click(krs: Krs)
    }
}