package com.example.siakad.ui.krs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.siakad.R


class KrsAdapter(private var krsList: List<Krs>, private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<KrsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KrsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_krs, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val namaKrs: TextView = itemView.findViewById(R.id.krs)
        val namaMahasiswa: TextView = itemView.findViewById(R.id.namaMahasiswa)
        val nim: TextView = itemView.findViewById(R.id.nim)
        val namaJurusan: TextView = itemView.findViewById(R.id.namaJurusan)
        val namaTahun: TextView = itemView.findViewById(R.id.namaTahun)

    }

    override fun onBindViewHolder(holder: KrsAdapter.ViewHolder, position: Int) {
        val krs = krsList[position]
        holder.namaMahasiswa.text = krs.mahasiswa.nama
        holder.nim.text = krs.mahasiswa.nim
        holder.namaJurusan.text = krs.mahasiswa.jurusan.nama_jurusan
        holder.namaTahun.text = krs.tahunAjaran.tahun.toString()
        holder.itemView.setOnClickListener {
            onItemClickListener.click(krs)
        }
    }

    override fun getItemCount(): Int {
        return krsList.size
    }

    interface OnItemClickListener {
        fun click(krs: Krs)
    }
}
