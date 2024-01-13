package com.example.siakad.ui.mahasiswa

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView

class DropDownMahasiswaAdapter(context: Context, resource: Int, private val listMahasiswa: List<Mahasiswa>) : ArrayAdapter<Mahasiswa>(context, resource, listMahasiswa) {
    private val filterMahasiswaList = mutableListOf<Mahasiswa>()
    init {
        filterMahasiswaList.addAll(listMahasiswa)
    }
    override fun getCount(): Int {
        return listMahasiswa.size
    }
    override fun getItem(position: Int): Mahasiswa? {
        return listMahasiswa[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_dropdown_item_1line, parent, false)
        val nama: TextView = view.findViewById(android.R.id.text1)
        val mahasiswa = getItem(position)
        nama.text = mahasiswa?.nama
        return view
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val filteredList = mutableListOf<Mahasiswa>()
                constraint?.let {
                    val filterPattern = it.toString().toLowerCase().trim()
                    for (mahasiswa in listMahasiswa) {
                        if (mahasiswa.nama.toLowerCase().contains(filterPattern)) {
                            filteredList.add(mahasiswa)
                        }
                    }
                }
                results.values = filteredList
                results.count = filteredList.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                results?.let {
                    filterMahasiswaList.clear()
                    filterMahasiswaList.addAll(it.values as List<Mahasiswa>)
                    notifyDataSetChanged()
                }
            }
        }
    }
}