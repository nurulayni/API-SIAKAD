package com.example.siakad.ui.mahasiswa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_mahasiswa)
        val jurusanSpinner: Spinner = findViewById(R.id.listNamaJurusan)

        val fetch = Retrofit.Builder()
            .baseUrl(Config.path)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FetchJurusan::class.java)
        val call = fetch.semuaJurusan()
        call.enqueue(object: Callback<JurusanModel> {
            override fun onResponse(call: Call<JurusanModel>, response: Response<JurusanModel>) {
                val metadata = response.body()?.items ?: emptyList()
                
            }
            override fun onFailure(call: Call<JurusanModel>, t: Throwable) {

            }

        })
    }
}