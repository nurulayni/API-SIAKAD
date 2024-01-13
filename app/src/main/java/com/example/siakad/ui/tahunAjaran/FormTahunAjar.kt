package com.example.siakad.ui.tahunAjaran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.example.siakad.R
import com.example.siakad.ui.Config
import com.example.siakad.ui.gallery.DataPost
import com.example.siakad.ui.gallery.FetchJurusan
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FormTahunAjar : AppCompatActivity() {
    private lateinit var apiTahunAjar: ApiTahunAjar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_tahun_ajar)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setTitle("Form Tahun Ajar");
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val id = intent.getIntExtra("id", 0)
        val tahun = intent.getStringExtra("tahun")
        val semester = intent.getStringExtra("semester")
        val status = intent.getStringExtra("status")
        val simpan = findViewById<Button>(R.id.simpan)
        val tahunInput = findViewById<EditText>(R.id.tahun)
        val semesterInput = findViewById<EditText>(R.id.semester)
        val statusInput= findViewById<EditText>(R.id.status)
        tahunInput.setText(tahun)
        semesterInput.setText(semester)
        statusInput.setText(status)
        simpan.setOnClickListener {
            val isNew = intent.getIntExtra("isNew", 1)
            val retrofit = Retrofit.Builder()
                .baseUrl(Config.path)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            apiTahunAjar = retrofit.create(ApiTahunAjar::class.java)

            val dataPost = Tahun(0, tahunInput.text.toString().toInt(), semesterInput.text.toString().toInt(), statusInput.text.toString().toInt())
            var call: Call<ResponseBody>? = null
            if (isNew == 1) {
                call = apiTahunAjar.postTahunAjar(dataPost)
            }
            else {
                call = apiTahunAjar.updateTahunAjar(id, dataPost)
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
                            if (jsonObject.getString("field") == "tahun") {
                                tahunInput.setError(jsonObject.getString("message"))
                            }
                            if (jsonObject.getString("field") == "semester") {
                                semesterInput.setError(jsonObject.getString("message"))
                            }
                            if (jsonObject.getString("field") == "status") {
                                statusInput.setError(jsonObject.getString("message"))
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
                    apiTahunAjar = retrofit.create(ApiTahunAjar::class.java)
                    var call: Call<ResponseBody>? = null
                    call = apiTahunAjar.deleteTahunAjar(id)
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
}