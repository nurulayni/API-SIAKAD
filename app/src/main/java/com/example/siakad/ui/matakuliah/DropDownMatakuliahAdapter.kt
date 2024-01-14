package com.example.siakad.ui.matakuliah

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.siakad.ui.mahasiswa.Mahasiswa

class DropDownMatakuliahAdapter (context: Context, resource: Int, private val listMatakuliah: List<Matakuliah>) : ArrayAdapter<Matakuliah>(context, resource, listMatakuliah) {
    private val filterListMatakuliah = mutableListOf<Matakuliah>()
    init {
        filterListMatakuliah.addAll(listMatakuliah)
    }
    override fun getCount(): Int {
        return listMatakuliah.size
    }
    override fun getItem(position: Int): Matakuliah? {
        return listMatakuliah[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_dropdown_item_1line, parent, false)
        val nama: TextView = view.findViewById(android.R.id.text1)
        val matakuliah = getItem(position)
        nama.text = matakuliah?.nama
        return view
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val filteredList = mutableListOf<Matakuliah>()
                constraint?.let {
                    val filterPattern = it.toString().toLowerCase().trim()
                    for (matakuliah in listMatakuliah) {
                        if (matakuliah.nama.toLowerCase().contains(filterPattern)) {
                            filteredList.add(matakuliah)
                        }
                    }
                }
                results.values = filteredList
                results.count = filteredList.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                results?.let {
                    filterListMatakuliah.clear()
                    filterListMatakuliah.addAll(it.values as List<Matakuliah>)
                    notifyDataSetChanged()
                }
            }
        }
    }
}