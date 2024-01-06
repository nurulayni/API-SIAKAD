package com.example.siakad.ui.krs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.siakad.R
import com.example.siakad.ui.Config
import com.example.siakad.ui.gallery.FetchJurusan
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DaftarKrs : AppCompatActivity() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MatakuliahAdapter
    private var matakuliahList = mutableListOf<Matakuliah>()
    private var isLoading = false
    private var totalData = 0;
    private var isLoadMore = false;
    private var page = 1
    private var id_krs = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_krs)
        val intent = getIntent();
        id_krs = intent.getIntExtra("id_krs", 0)
        matakuliahList.clear()
        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.refresh)
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            matakuliahList.clear()
            fetchData()
        }
        recyclerView = findViewById<RecyclerView>(R.id.listMatakuliahKrs)
        adapter = MatakuliahAdapter(matakuliahList, object : MatakuliahAdapter.OnItemClickListener{
            override fun click(matakuliah: Matakuliah) {
                AlertDialog.Builder(applicationContext)
                    .setTitle("Perhatian!!")
                    .setMessage("Yakin hapus "+matakuliah.matakuliah.nama+" ?")
                    .setPositiveButton("Ok") {_, _ ->
                        deleteMatakuliah(matakuliah)
                    }
                    .setNegativeButton("Cancel") {_, _ ->

                    }.show()
            }
        })
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        swipeRefreshLayout.isRefreshing = true
        fetchData()
    }
    fun deleteMatakuliah(matakuliah: Matakuliah) {
        val retrofi = Retrofit.Builder()
            .baseUrl(Config.path)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var apiKrs: ApiKrs
        apiKrs = retrofi.create(ApiKrs::class.java)
        var call: Call<ResponseBody>? = null
        call = apiKrs.deleteMatakuliahKrs(matakuliah.matakuliah.id)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                fetchData()
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }
    fun fetchData() {
        isLoading = true
        val fecth = Retrofit.Builder()
            .baseUrl(Config.path)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiKrs::class.java)
        val call = fecth.getMatakuliahKrs(id_krs)
        call?.enqueue(object : Callback<MatakuliahModel> {
            override fun onResponse(call: Call<MatakuliahModel>, response: Response<MatakuliahModel>) {
                val metaData = response.body()?.items ?: emptyList()
                totalData = response.body()?._meta?.totalCount!!
                swipeRefreshLayout.isRefreshing = false
                matakuliahList.addAll(metaData)
                adapter.notifyItemRangeInserted(adapter.itemCount, matakuliahList.size)
                adapter.notifyDataSetChanged()
                page = page + response.body()?._meta?.currentPage!!
                isLoading = false
            }
            override fun onFailure(call: Call<MatakuliahModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}