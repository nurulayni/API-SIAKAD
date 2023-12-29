package com.example.siakad.ui.matakuliah

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.siakad.R
import com.example.siakad.ui.mahasiswa.Mahasiswa
import com.example.siakad.ui.mahasiswa.MahasiswaAdapter
import com.example.siakad.ui.matakuliah.Matakuliah
import com.example.siakad.ui.matakuliah.MatakuliahAdapter

class MatakuliahAdapter(private var matakuliahList: List<Matakuliah>, private val onItemClickListener: MatakuliahAdapter.OnItemClickListener) : RecyclerView.Adapter<MatakuliahAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatakuliahAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jurusan, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaMatakuliah: TextView = itemView.findViewById(R.id.matakuliah)
    }

    override fun onBindViewHolder(holder: MatakuliahAdapter.ViewHolder, position: Int) {
        val matakuliah = matakuliahList[position]

    }

    override fun getItemCount(): Int {
        return matakuliahList.size
    }

    interface OnItemClickListener {
        fun click(matakuliah: Matakuliah)
    }
}