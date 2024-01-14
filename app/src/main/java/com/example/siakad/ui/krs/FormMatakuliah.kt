package com.example.siakad.ui.krs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.siakad.R
import com.example.siakad.ui.Config
import com.example.siakad.ui.gallery.DropDownJurusanAdapter
import com.example.siakad.ui.gallery.FetchJurusan
import com.example.siakad.ui.gallery.JurusanModel
import com.example.siakad.ui.matakuliah.ApiMatakuliah
import com.example.siakad.ui.matakuliah.DropDownMatakuliahAdapter
import com.example.siakad.ui.matakuliah.Matakuliah
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FormMatakuliah : AppCompatActivity() {
    private lateinit var adapter: DropDownMatakuliahAdapter
    private lateinit var dropDownMatakuliah: AutoCompleteTextView
    private var matakuliahList = mutableListOf<Matakuliah>()
    private var idMatakuliah = 0;
    private lateinit var dataKrs: Krs
    private lateinit var dataMatakuliah: Matakuliah
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_matakuliah)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_form_matakuliah)
        toolbar.setTitle("Form Matakuliah KRS")
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        dropDownMatakuliah = findViewById(R.id.matakuliah)
        val isNew = intent.getIntExtra("isNew", 1)
        dataKrs = intent.getSerializableExtra("dataKrs") as Krs
        if (isNew != 1) {
            dataMatakuliah = intent.getSerializableExtra("dataMatakuliah") as Matakuliah
            idMatakuliah = dataMatakuliah.id
        }
        dataKrs = intent.getSerializableExtra("dataKrs") as Krs
        dropDownMatakuliah.setOnItemClickListener { adapterView, view, i, l ->
            dropDownMatakuliah.setText(matakuliahList[i].nama)
            idMatakuliah = matakuliahList[i].id
        }
        dropDownMatakuliah.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                getMatakuliah(p0.toString())
            }
        })
        val simpam:Button = findViewById(R.id.simpan)
        simpam.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl(Config.path)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val apiMatakuliahKrs = retrofit.create(ApiKrs::class.java)
            var call: Call<ResponseBody>? = null
            val data = DataMkPost(dataKrs.id, idMatakuliah)
            if (isNew == 1) {
                call = apiMatakuliahKrs.simpanMatakuliah(data)
            }
            else {
                call = apiMatakuliahKrs.updateMatakuliah(dataKrs.id, data)
            }
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
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
                            if (jsonObject.getString("field") == "id_matakuliah") {
                                val errorMatkul:TextView = findViewById(R.id.errorMatkul)
                                errorMatkul.setText(jsonObject.getString("message"))
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(applicationContext, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    fun getMatakuliah(nama_matakuliah: String) {
        val retRofitJurusan = Retrofit.Builder()
            .baseUrl(Config.path)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiMatakuliah::class.java)
        retRofitJurusan.getListMatakuliah(dataKrs.mahasiswa.jurusan.id, nama_matakuliah)
            .enqueue(object : Callback<com.example.siakad.ui.matakuliah.MatakuliahModel> {
                override fun onResponse(call: Call<com.example.siakad.ui.matakuliah.MatakuliahModel>, response: Response<com.example.siakad.ui.matakuliah.MatakuliahModel>) {
                    val listJurusan = response.body()?.items ?: emptyList()
                    matakuliahList.clear()
                    matakuliahList.addAll(listJurusan)
                    adapter = DropDownMatakuliahAdapter(applicationContext, android.R.layout.simple_dropdown_item_1line, matakuliahList)
                    dropDownMatakuliah.setThreshold(1)
                    dropDownMatakuliah.setAdapter(adapter)
                    adapter.notifyDataSetChanged()
                }
                override fun onFailure(call: Call<com.example.siakad.ui.matakuliah.MatakuliahModel>, t: Throwable) {
                }
            })
    }
}