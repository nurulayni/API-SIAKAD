package com.example.siakad.ui.matakuliah

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.example.siakad.R
import com.example.siakad.ui.Config
import com.example.siakad.ui.gallery.DropDownJurusanAdapter
import com.example.siakad.ui.gallery.FetchJurusan
import com.example.siakad.ui.gallery.Jurusan
import com.example.siakad.ui.gallery.JurusanModel
import com.example.siakad.ui.krs.ApiKrs
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MatakuliahForm : AppCompatActivity() {
    private lateinit var apiMatakuliah: ApiMatakuliah
    private lateinit var apiJurusan: FetchJurusan
    private lateinit var autoCompleteJurusan: AutoCompleteTextView
    private var idJurusan = 0
    private lateinit var adapter: DropDownJurusanAdapter
    private var jurusanList = mutableListOf<Jurusan>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matakuliah_form)
        val toolbar = findViewById<Toolbar>(R.id.toolbarFormMatkul)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setTitle("Form Matakuliah");
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val namaMatkkul:TextView = findViewById(R.id.textEditNamaMatkul)
        autoCompleteJurusan = findViewById<AutoCompleteTextView>(R.id.dropDownJurusan)
        val simpan:Button = findViewById(R.id.simpan)
        val isNew = intent.getIntExtra("isNew", 0)
        if (isNew == 0) {
            val data = intent.getSerializableExtra("matakuliah") as Matakuliah
            namaMatkkul.setText(data.nama)
            idJurusan = data.jurusan.id
            autoCompleteJurusan.setText(data.jurusan.nama_jurusan)
        }
        simpan.setOnClickListener {
            val dataPost = PostMatakuliah(namaMatkkul.text.toString(), idJurusan)
            val isNew = intent.getIntExtra("isNew", 1)
            val retrofit = Retrofit.Builder()
                .baseUrl(Config.path)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val apiMatakuliah = retrofit.create(ApiMatakuliah::class.java)
            var call: Call<ResponseBody>? = null
            if (isNew == 1) {
                call = apiMatakuliah.postMatakuliah(dataPost)
            }
            else {
                val data = intent.getSerializableExtra("matakuliah") as Matakuliah
                call = apiMatakuliah.updateMatakuliah(data.id, dataPost)
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
                        Toast.makeText(applicationContext, "ni", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(applicationContext, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }

            })
        }
        autoCompleteJurusan = findViewById(R.id.dropDownJurusan)
        autoCompleteJurusan.setOnItemClickListener { adapterView, view, i, l ->
            autoCompleteJurusan.setText(jurusanList[i].nama_jurusan)
            idJurusan = jurusanList[i].id
        }
        autoCompleteJurusan.addTextChangedListener(object : TextWatcher {
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
                    adapter = DropDownJurusanAdapter(applicationContext, android.R.layout.simple_dropdown_item_1line, jurusanList)
                    autoCompleteJurusan.setThreshold(1)
                    autoCompleteJurusan.setAdapter(adapter)
                    adapter.notifyDataSetChanged()
                }
                override fun onFailure(call: Call<JurusanModel>, t: Throwable) {
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val isNew = intent.getIntExtra("isNew", 1)
        if (isNew == 0) {
            menuInflater.inflate(R.menu.menu_hapus_jurusan, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_delete) {
            AlertDialog.Builder(this)
                .setTitle("Perhatian!!")
                .setMessage("Yakin hapus data ini?")
                .setPositiveButton("OK") { _, _ ->
                    val data = intent.getSerializableExtra("matakuliah") as Matakuliah
                    val retrofit = Retrofit.Builder()
                        .baseUrl(Config.path)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val apiMatakuliah = retrofit.create(ApiMatakuliah::class.java)
                    var call: Call<ResponseBody>? = null
                    call = apiMatakuliah.deleteMatakuliah(data.id)
                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if (response.isSuccessful) {
                                val intent = Intent()
                                intent.putExtra("isReload", true)
                                setResult(RESULT_OK, intent)
                                finish()
                            }
                        }
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(applicationContext, "Terjadi kesalah", Toast.LENGTH_SHORT).show()
                            t.printStackTrace()
                        }

                    })
                }
                .setNegativeButton("Cancel") { _, _ ->
                    // Do something when the user clicks Cancel
                }
                .show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}