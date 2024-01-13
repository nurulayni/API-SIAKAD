package com.example.siakad.ui.gallery

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.siakad.ui.mahasiswa.Mahasiswa

class DropDownJurusanAdapter(context: Context, resource: Int, private val listJurusan: List<Jurusan>) : ArrayAdapter<Jurusan>(context, resource, listJurusan) {
    private val filteredJurusanList = mutableListOf<Jurusan>()

    init {
        filteredJurusanList.addAll(listJurusan)
    }
    override fun getCount(): Int {
        Log.d("USeless-dev", listJurusan.size.toString())
        return listJurusan.size
    }
    override fun getItem(position: Int): Jurusan? {
        return listJurusan[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_dropdown_item_1line, parent, false)
        val namaJurusanTextView: TextView = view.findViewById(android.R.id.text1)
        val jurusan = getItem(position)
        namaJurusanTextView.text = jurusan?.nama_jurusan
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val filteredList = mutableListOf<Jurusan>()
                constraint?.let {
                    val filterPattern = it.toString().toLowerCase().trim()

                    for (jurusan in listJurusan) {
                        if (jurusan.nama_jurusan.toLowerCase().contains(filterPattern)) {
                            filteredList.add(jurusan)
                        }
                    }
                }
                results.values = filteredList
                results.count = filteredList.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                results?.let {
                    filteredJurusanList.clear()
                    filteredJurusanList.addAll(it.values as List<Jurusan>)
                    notifyDataSetChanged()
                }
            }
        }
    }
}