package com.example.siakad.ui.tahunAjaran

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.siakad.ui.mahasiswa.Mahasiswa

class DropDownTahunAjaranAdapter(context: Context, resource: Int, private val tahunAjaranList: List<Tahun>) : ArrayAdapter<Tahun>(context, resource, tahunAjaranList) {
    private val filterTahunList = mutableListOf<Tahun>()
    init {
        filterTahunList.addAll(tahunAjaranList)
    }
    override fun getCount(): Int {
        return filterTahunList.size
    }
    override fun getItem(position: Int): Tahun? {
        return tahunAjaranList[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_dropdown_item_1line, parent, false)
        val nama: TextView = view.findViewById(android.R.id.text1)
        val tahunAjaran = getItem(position)
        var tulisan = ""
        if (tahunAjaran != null) {
            if (tahunAjaran.semester % 2 == 0) {
                tulisan = "Genap"
            }
            else {
                tulisan = "Ganjil"
            }
        }
        nama.text = tahunAjaran?.tahun.toString() +" "+ tulisan
        return view
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val filteredList = mutableListOf<Tahun>()
                constraint?.let {
                    val filterPattern = it.toString().toLowerCase().trim()
                    for (tahun in tahunAjaranList) {
                        if (tahun.tahun.toString().toLowerCase().contains(filterPattern)) {
                            filteredList.add(tahun)
                        }
                    }
                }
                results.values = filteredList
                results.count = filteredList.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                results?.let {
                    filterTahunList.clear()
                    filterTahunList.addAll(it.values as List<Tahun>)
                    notifyDataSetChanged()
                }
            }
        }
    }
}