package com.example.siakad.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.siakad.R

class JurusanAdapter(private var jurusanList: List<Jurusan>, private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<JurusanAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaJurusan: TextView = itemView.findViewById(R.id.namaJurusan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JurusanAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jurusan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: JurusanAdapter.ViewHolder, position: Int) {
        val jurusan = jurusanList[position]
        holder.namaJurusan.text = jurusan.nama_jurusan
        holder.itemView.setOnClickListener{
            onItemClickListener.click(jurusan)
        }
    }

    override fun getItemCount(): Int {
        return jurusanList.size
    }

    interface OnItemClickListener {
        fun click(jurusan: Jurusan)
    }

}