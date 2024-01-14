package com.example.siakad.ui.krs

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.siakad.R
import com.example.siakad.ui.Config
import com.example.siakad.ui.gallery.FetchJurusan
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    private lateinit var dataKrs: Krs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_krs)
        val toolbar = findViewById<Toolbar>(R.id.toolbarDaftarKrs)
        toolbar.setTitle("Daftar Matakuliah KRS")
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val intent = getIntent();
        dataKrs = intent.getSerializableExtra("krs") as Krs
        id_krs = dataKrs.id
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
                deleteMatakuliah(matakuliah)
            }
        })
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        swipeRefreshLayout.isRefreshing = true
        fetchData()
        val fab: FloatingActionButton = findViewById(R.id.tambah_matakuliah)
        fab.setOnClickListener {
            val intent = Intent(applicationContext, FormMatakuliah::class.java)
            intent.putExtra("isNew", 1)
            intent.putExtra("dataKrs", dataKrs)
            startActivityForResult(intent, 111)
        }
    }

    fun deleteMatakuliah(matakuliah: Matakuliah) {
        AlertDialog.Builder(this)
            .setTitle("Perhatian!!")
            .setMessage("Yakin hapus data ini?")
            .setPositiveButton("OK") { _, _ ->
                val retrofi = Retrofit.Builder()
                    .baseUrl(Config.path)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                var apiKrs: ApiKrs
                apiKrs = retrofi.create(ApiKrs::class.java)
                var call: Call<ResponseBody>? = null
                call = apiKrs.deleteMatakuliahKrs(matakuliah.id)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        page = 1
                        matakuliahList.clear()
                        fetchData()
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    }
                })
            }
            .setNegativeButton("Cancel") { _, _ ->
            }
            .show()
    }
    fun fetchData() {
        swipeRefreshLayout.isRefreshing = true
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_hapus, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_edit) {
            val intent = Intent(applicationContext, FormKrsActivity::class.java)
            intent.putExtra("isNew", 0)
            intent.putExtra("dataKrs", dataKrs)
            startActivityForResult(intent, 111)
        }
        else if(id == R.id.action_delete) {
            AlertDialog.Builder(this)
                .setTitle("Perhatian!!")
                .setMessage("Yakin hapus data ini?")
                .setPositiveButton("OK") { _, _ ->
                    val id = dataKrs.id
                    val retrofit = Retrofit.Builder()
                        .baseUrl(Config.path)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val apiKrs = retrofit.create(ApiKrs::class.java)
                    var call: Call<ResponseBody>? = null
                    call = apiKrs.deleteKrs(id)
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
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111) {
            if (resultCode == Activity.RESULT_OK) {
                page = 1
                matakuliahList.clear()
                fetchData()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}