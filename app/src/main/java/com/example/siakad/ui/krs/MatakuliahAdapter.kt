package com.example.siakad.ui.krs


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.siakad.R


class MatakuliahAdapter(private var matakuliahList: List<Matakuliah>, private val onItemClickListener: MatakuliahAdapter.OnItemClickListener) : RecyclerView.Adapter<MatakuliahAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatakuliahAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_matakuliah_krs, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        val namaKrs: TextView = itemView.findViewById(R.id.krs)
//        val namaMahasiswa: TextView = itemView.findViewById(R.id.namaMahasiswa)
//        val nim: TextView = itemView.findViewById(R.id.nim)
//        val namaJurusan: TextView = itemView.findViewById(R.id.namaJurusan)
//        val namaTahun: TextView = itemView.findViewById(R.id.namaTahun)
        val matakuliah: TextView = itemView.findViewById(R.id.matakuliah)

    }

    override fun onBindViewHolder(holder: MatakuliahAdapter.ViewHolder, position: Int) {
        val matakuliah = matakuliahList[position]
//        holder.namaMahasiswa.text = krs.mahasiswa.nama
//        holder.nim.text = krs.mahasiswa.nim
//        holder.namaJurusan.text = krs.mahasiswa.jurusan.nama_jurusan
//        holder.namaTahun.text = krs.tahunAjaran.tahun.toString()
        holder.matakuliah.text = matakuliah.matakuliah.nama
        holder.itemView.setOnClickListener {
            onItemClickListener.click(matakuliah)
        }

    }

    override fun getItemCount(): Int {
        return matakuliahList.size
    }

    interface OnItemClickListener {
        fun click(matakuliah: Matakuliah)
    }
}