package com.example.siakad.ui.tahunAjaran

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.siakad.R

class TahunAjaranAdapter (private var tahunajarList: List<Tahun>, private val onItemClickListener: TahunAjaranAdapter.OnItemClickListener) : RecyclerView.Adapter<TahunAjaranAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TahunAjaranAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tahunajar, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tahun: TextView = itemView.findViewById(R.id.tahunajar)
        val status: TextView = itemView.findViewById(R.id.status)
        val semester: TextView = itemView.findViewById(R.id.semester)
    }

    override fun onBindViewHolder(holder: TahunAjaranAdapter.ViewHolder, position: Int) {
        val tahunajar = tahunajarList[position]
        holder.tahun.text = tahunajar.tahun.toString()
        holder.status.text = tahunajar.status
        holder.semester.text = tahunajar.semester
    }

    override fun getItemCount(): Int {
        return tahunajarList.size
    }

    interface OnItemClickListener {
        fun click(tahunajar: Tahun)
    }
}