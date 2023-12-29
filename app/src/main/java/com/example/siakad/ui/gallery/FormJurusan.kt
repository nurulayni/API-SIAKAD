package com.example.siakad.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.siakad.R
import com.example.siakad.ui.Config
import com.example.siakad.ui.FormResponse
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FormJurusan : AppCompatActivity() {
    private lateinit var fetchJurusan: FetchJurusan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_jurusan)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setTitle("Form Jurusan");
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val id = intent.getIntExtra("id", 0)
        val nama_jurusan = intent.getStringExtra("nama_jurusan")
        val simpan = findViewById<Button>(R.id.simpan)
        val namaJurusan = findViewById<EditText>(R.id.namaJurusan)
        namaJurusan.setText(nama_jurusan)
        simpan.setOnClickListener {
            val isNew = intent.getIntExtra("isNew", 1)
            val retrofit = Retrofit.Builder()
                .baseUrl(Config.path)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            fetchJurusan = retrofit.create(FetchJurusan::class.java)
            val dataPost = DataPost(namaJurusan.text.toString())
            var call: Call<ResponseBody>? = null
            if (isNew == 1) {
                call = fetchJurusan.postJurusanData(dataPost)
            }
            else {
                call = fetchJurusan.updateJurusan(id, dataPost)
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
                            if (jsonObject.getString("field") == "nama_jurusan") {
                                namaJurusan.setError(jsonObject.getString("message"))
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
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
                    val id = intent.getIntExtra("id", 0)
                    val retrofit = Retrofit.Builder()
                        .baseUrl(Config.path)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    fetchJurusan = retrofit.create(FetchJurusan::class.java)
                    var call: Call<ResponseBody>? = null
                    call = fetchJurusan.deleteJurusan(id)
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

    private fun isValidInput(input: String) : Boolean {
        return input.isNotBlank()
    }
}

