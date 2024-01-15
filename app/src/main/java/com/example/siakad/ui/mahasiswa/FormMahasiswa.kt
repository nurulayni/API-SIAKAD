package com.example.siakad.ui.mahasiswa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.siakad.R
import com.example.siakad.ui.Config
import com.example.siakad.ui.gallery.FetchJurusan
import com.example.siakad.ui.gallery.Jurusan
import com.example.siakad.ui.gallery.JurusanModel
import com.google.android.material.textfield.TextInputEditText
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class FormMahasiswa : AppCompatActivity() {
    private var jurusanList = mutableListOf<Jurusan>()
    private lateinit var jurusan: AutoCompleteTextView
    private lateinit var textInputNim: TextInputEditText
    private lateinit var textInputNama: TextInputEditText
    private lateinit var adapter: TestingAdapter
    private lateinit var apiMahasiswa: ApiMahasiswa
    private var idJurusan = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_mahasiswa)
        val toolbar = findViewById<Toolbar>(R.id.toolbarFormMahasiswa)
        toolbar.setTitle("Daftar Mahasiswa")
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        jurusan = findViewById(R.id.jurusan)
        textInputNama = findViewById(R.id.nama)
        textInputNim = findViewById(R.id.nim)
        val simpan:Button = findViewById(R.id.simpan)
        simpan.setOnClickListener {
            val isNew = intent.getIntExtra("isNew", 1)
            val retrofit = Retrofit.Builder()
                .baseUrl(Config.path)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            apiMahasiswa = retrofit.create(ApiMahasiswa::class.java)
            val postData = PostMahasiswa(textInputNim.text.toString(), textInputNama.text.toString(), idJurusan)
            var call : Call<ResponseBody>? = null
            if (isNew == 1) {
                call = apiMahasiswa.postMahasiswa(postData)
            }
            else {
                val dataMahasiswa = intent.getSerializableExtra("dataMahasiswa") as Mahasiswa
                call = apiMahasiswa.updateMahasiswa(dataMahasiswa.id, postData)
            }
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val intent = Intent()
                        intent.putExtra("isReload", true)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                    else {
                        val jsonArray = JSONArray(response.errorBody()!!.string())
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = JSONObject(jsonArray.getString(i).toString())
                            if (jsonObject.getString("field") == "nim") {
                                textInputNim.setError(jsonObject.getString("message"))
                            }
                            if (jsonObject.getString("field") == "nama") {
                                textInputNama.setError(jsonObject.getString("message"))
                            }
                            if (jsonObject.getString("field") == "id_jurusan") {
                                val errorJurusan: TextView = findViewById(R.id.errorJurusan)
                                errorJurusan.setText(jsonObject.getString("message"))
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(applicationContext, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }

            })
        }
        jurusan.setOnItemClickListener { adapterView, view, i, l ->
            jurusan.setText(jurusanList[i].nama_jurusan)
        }
        jurusan.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}