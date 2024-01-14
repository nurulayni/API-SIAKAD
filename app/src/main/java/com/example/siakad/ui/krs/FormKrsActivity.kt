package com.example.siakad.ui.krs
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.siakad.R
import com.example.siakad.ui.Config
import com.example.siakad.ui.gallery.DropDownJurusanAdapter
import com.example.siakad.ui.gallery.FetchJurusan
import com.example.siakad.ui.gallery.Jurusan
import com.example.siakad.ui.gallery.JurusanModel
import com.example.siakad.ui.mahasiswa.ApiMahasiswa
import com.example.siakad.ui.mahasiswa.DropDownMahasiswaAdapter
import com.example.siakad.ui.mahasiswa.Mahasiswa
import com.example.siakad.ui.mahasiswa.MahasiswaModel
import com.example.siakad.ui.mahasiswa.TestingAdapter
import com.example.siakad.ui.tahunAjaran.ApiTahunAjar
import com.example.siakad.ui.tahunAjaran.DropDownTahunAjaranAdapter
import com.example.siakad.ui.tahunAjaran.Tahun
import com.example.siakad.ui.tahunAjaran.TahunModel
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FormKrsActivity : AppCompatActivity() {
    private lateinit var mahasiswaDropDown: AutoCompleteTextView
    private lateinit var dropDowntahunAjaran: AutoCompleteTextView
    private lateinit var dropDownJurusan: AutoCompleteTextView
    private var mahasiswaList = mutableListOf<Mahasiswa>()
    private var jurusanList = mutableListOf<Jurusan>()
    private var taList = mutableListOf<Tahun>()
    private lateinit var adapter: DropDownMahasiswaAdapter
    private lateinit var jurusanDropDownAdapter: DropDownJurusanAdapter
    private lateinit var tahunAjaranDropDownAdapter: DropDownTahunAjaranAdapter

    private var idMahasiswa = 0
    private var idJurusan = 0
    private var idTahunAjaran = 0;
    private lateinit var dataKrs: Krs

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_krs)

        val errorMahasiswa:TextView = findViewById(R.id.errorMahasiswa)
        val errorTa:TextView = findViewById(R.id.errorTa)
        mahasiswaDropDown = findViewById(R.id.mahasiswa_drop_down)
        dropDownJurusan = findViewById(R.id.jurusan_drop_down)
        dropDowntahunAjaran = findViewById(R.id.tahun_ajaran_dropdown)
        mahasiswaDropDown.setOnItemClickListener { adapterView, view, i, l ->
            mahasiswaDropDown.setText(mahasiswaList[i].nim +" - "+mahasiswaList[i].nama)
            idMahasiswa = mahasiswaList[i].id
        }
        mahasiswaDropDown.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                getMahasiswa(p0.toString())
            }
        })
        dropDownJurusan.setOnItemClickListener { adapterView, view, i, l ->
            dropDownJurusan.setText(jurusanList[i].nama_jurusan)
            idJurusan = jurusanList[i].id
        }
        dropDownJurusan.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                getJurusan(p0.toString())
            }
        })
        dropDowntahunAjaran.setOnItemClickListener { adapterView, view, i, l ->
            var tulisan = "Ganjil"
            if (taList[i].semester % 2 == 0) {
                tulisan = "Genap"
            }
            dropDowntahunAjaran.setText(taList[i].tahun.toString() +" "+tulisan)
            idTahunAjaran = taList[i].id
        }
        dropDowntahunAjaran.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                getTahunAjaran(p0.toString())
            }
        })
        val isNew = intent.getIntExtra("isNew", 1)
        if (isNew == 0) {
            dataKrs = intent.getSerializableExtra("dataKrs") as Krs
            idMahasiswa = dataKrs.mahasiswa.id
            idJurusan = dataKrs.mahasiswa.jurusan.id
            idTahunAjaran = dataKrs.tahunAjaran.id
            mahasiswaDropDown.setText(dataKrs.mahasiswa.nim+" "+dataKrs.mahasiswa.nama)
            var tulisan = "Ganjil"
            if (dataKrs.tahunAjaran.semester % 2 == 0) {
                tulisan = "Genap"
            }
            dropDowntahunAjaran.setText(dataKrs.tahunAjaran.tahun.toString()+" "+tulisan)
            dropDownJurusan.setText(dataKrs.mahasiswa.jurusan.nama_jurusan)
        }
        val simpan:Button = findViewById(R.id.simpan)
        simpan.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl(Config.path)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val apiKrs = retrofit.create(ApiKrs::class.java)
            val isNew = intent.getIntExtra("isNew", 1)
            val dataKrsPost = KrsPost(idMahasiswa, idTahunAjaran)
            var call: Call<ResponseBody>? = null
            if (isNew == 1) {
                call = apiKrs.postKrs(dataKrsPost)
            }
            else {
                val id = dataKrs.id
                call = apiKrs.updateKrs(id, dataKrsPost)
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
                            if (jsonObject.getString("field") == "id_mahasiswa") {
                                errorMahasiswa.setText(jsonObject.getString("message"))
                            }
                            if (jsonObject.getString("field") == "id_tahun_ajaran") {
                                errorTa.setText(jsonObject.getString("message"))
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
                    jurusanDropDownAdapter = DropDownJurusanAdapter(applicationContext, android.R.layout.simple_dropdown_item_1line, jurusanList)
                    dropDownJurusan.setThreshold(1)
                    dropDownJurusan.setAdapter(jurusanDropDownAdapter)
                    jurusanDropDownAdapter.notifyDataSetChanged()
                }
                override fun onFailure(call: Call<JurusanModel>, t: Throwable) {
                }
            })
    }

    fun getMahasiswa(nama_jurusan: String) {
        val retRofitJurusan = Retrofit.Builder()
            .baseUrl(Config.path)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiMahasiswa::class.java)
        retRofitJurusan.mahasiswa(nama_jurusan, idJurusan)
            .enqueue(object : Callback<MahasiswaModel> {
                override fun onResponse(call: Call<MahasiswaModel>, response: Response<MahasiswaModel>) {
                    val list = response.body()?.items ?: emptyList()
                    mahasiswaList.clear()
                    mahasiswaList.addAll(list)
                    adapter = DropDownMahasiswaAdapter(applicationContext, android.R.layout.simple_dropdown_item_1line, mahasiswaList)
                    mahasiswaDropDown.setThreshold(1)
                    mahasiswaDropDown.setAdapter(adapter)
                    adapter.notifyDataSetChanged()
                }
                override fun onFailure(call: Call<MahasiswaModel>, t: Throwable) {
                }
            })
    }

    fun getTahunAjaran(tahun: String) {
        val retRofitJurusan = Retrofit.Builder()
            .baseUrl(Config.path)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiTahunAjar::class.java)
        retRofitJurusan.tahunAjaran(tahun)
            .enqueue(object : Callback<TahunModel> {
                override fun onResponse(call: Call<TahunModel>, response: Response<TahunModel>) {
                    val list = response.body()?.items ?: emptyList()
                    taList.clear()
                    taList.addAll(list)
                    tahunAjaranDropDownAdapter = DropDownTahunAjaranAdapter(applicationContext, android.R.layout.simple_dropdown_item_1line, taList)
                    dropDowntahunAjaran.setThreshold(1)
                    dropDowntahunAjaran.setAdapter(tahunAjaranDropDownAdapter)
                    tahunAjaranDropDownAdapter.notifyDataSetChanged()
                }
                override fun onFailure(call: Call<TahunModel>, t: Throwable) {
                }
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}