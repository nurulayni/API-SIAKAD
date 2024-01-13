package com.example.siakad.ui.mahasiswa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.siakad.R
import com.example.siakad.ui.Config
import com.example.siakad.ui.gallery.FetchJurusan
import com.example.siakad.ui.gallery.Jurusan
import com.example.siakad.ui.gallery.JurusanModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class FormMahasiswa : AppCompatActivity() {
    private var jurusanList = mutableListOf<Jurusan>()
    private lateinit var jurusan: AutoCompleteTextView
    private lateinit var adapter: TestingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_mahasiswa)
        jurusan = findViewById(R.id.jurusan)
        jurusan.setOnItemClickListener { adapterView, view, i, l ->
            jurusan.setText(jurusanList[i].nama_jurusan)
        }
        jurusan.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                jurusanList.clear()
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                getJurusan(p0.toString())
            }
        })
    }
    fun getJurusan(nama_jurusan: String) {
        val retRofitJurusan = Retrofit.Builder()
            .baseUrl(Config.path)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FetchJurusan::class.java)
        retRofitJurusan.jurusan(nama_jurusan)
            .enqueue(object : Callback<JurusanModel> {
                override fun onResponse(call: Call<JurusanModel>, response: Response<JurusanModel>) {
                    val listJurusan = response.body()?.items ?: emptyList()
                    jurusanList.clear()
                    jurusanList.addAll(listJurusan)
                    adapter = TestingAdapter(applicationContext, android.R.layout.simple_dropdown_item_1line, jurusanList)
                    jurusan.setThreshold(1)
                    jurusan.setAdapter(adapter)
                    adapter.notifyDataSetChanged()
                }
                override fun onFailure(call: Call<JurusanModel>, t: Throwable) {

                }
            })
    }
}