package com.example.siakad.ui.mahasiswa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.siakad.R

class  MahasiswaAdapter(private var mahasiswaList: List<Mahasiswa>, private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<MahasiswaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahasiswaAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mahasiswa, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaMahasiswa: TextView = itemView.findViewById(R.id.namaMahasiswa)
        val nim: TextView = itemView.findViewById(R.id.nim)
        val namaJurusan: TextView = itemView.findViewById(R.id.namaJurusan)
    }

    override fun onBindViewHolder(holder: MahasiswaAdapter.ViewHolder, position: Int) {
        val mahasiswa = mahasiswaList[position]
        holder.namaMahasiswa.text = mahasiswa.nama
        holder.nim.text = mahasiswa.nim
        holder.namaJurusan.text = mahasiswa.jurusan.nama_jurusan
    }

    override fun getItemCount(): Int {
        return mahasiswaList.size
    }

    interface OnItemClickListener {
        fun click(mahasiswa: Mahasiswa)
    }
}