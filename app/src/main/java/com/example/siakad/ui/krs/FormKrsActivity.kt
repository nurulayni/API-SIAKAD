package com.example.siakad.ui.krs
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AutoCompleteTextView
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FormKrsActivity : AppCompatActivity() {
    private lateinit var mahasiswaDropDown: AutoCompleteTextView
    private lateinit var tahunAjaran: AutoCompleteTextView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_krs)
        mahasiswaDropDown = findViewById(R.id.mahasiswa_drop_down)
        dropDownJurusan = findViewById(R.id.jurusan_drop_down)
        mahasiswaDropDown.setOnItemClickListener { adapterView, view, i, l ->
            mahasiswaDropDown.setText(mahasiswaList[i].nama)
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
        tahunAjaran = findViewById(R.id.tahunAjaranDrop)
        tahunAjaran.setOnItemClickListener { adapterView, view, i, l ->
            tahunAjaran.setText(taList[i].tahun.toString())
            idTahunAjaran = taList[i].id
        }
        tahunAjaran.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                getTahunAjaran(p0.toString().toInt())
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

    fun getTahunAjaran(tahun: Int) {
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
                    tahunAjaran.setThreshold(1)
                    tahunAjaran.setAdapter(tahunAjaranDropDownAdapter)
                    tahunAjaranDropDownAdapter.notifyDataSetChanged()
                }
                override fun onFailure(call: Call<TahunModel>, t: Throwable) {
                }
            })
    }
}