package com.example.siakad.ui.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.siakad.R
import com.example.siakad.databinding.FragmentGalleryBinding
import com.example.siakad.ui.Config
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JurusanAdapter
    private var jurusanList = mutableListOf<Jurusan>()
    private var isLoading = false
    private var totalData = 0;
    private var isLoadMore = false;
    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(context, FormJurusan::class.java)
            intent.putExtra("isNew", 1)
            startActivityForResult(intent, 111)
        }
        jurusanList.clear()
        swipeRefreshLayout = root.findViewById(R.id.refresh)
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            jurusanList.clear()
            fetchData()
        }
        recyclerView = root.findViewById(R.id.listJurusan)
        adapter = JurusanAdapter(jurusanList, object : JurusanAdapter.OnItemClickListener{
            override fun click(jurusan: Jurusan) {
                val intent = Intent(context, FormJurusan::class.java)
                intent.putExtra("id", jurusan.id)
                intent.putExtra("nama_jurusan", jurusan.nama_jurusan)
                intent.putExtra("isNew", 0)
                startActivityForResult(intent, 111)
            }
        })
        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && dy > 0 && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && totalItemCount < totalData) {
                    fetchData()
                }
            }
        })

        swipeRefreshLayout.isRefreshing = true
        fetchData()
        return root
    }

    fun fetchData() {
        isLoading = true
        val fecth = Retrofit.Builder()
            .baseUrl(Config.path)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FetchJurusan::class.java)
        val call = fecth.getJurusanData(page)
        call?.enqueue(object : Callback<JurusanModel> {
            override fun onResponse(call: Call<JurusanModel>, response: Response<JurusanModel>) {
                val metaData = response.body()?.items ?: emptyList()
                totalData = response.body()?._meta?.totalCount!!
                swipeRefreshLayout.isRefreshing = false
                jurusanList.addAll(metaData)
                adapter.notifyItemRangeInserted(adapter.itemCount, jurusanList.size)
                adapter.notifyDataSetChanged()
                page = page + response.body()?._meta?.currentPage!!
                isLoading = false
            }
            override fun onFailure(call: Call<JurusanModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        page = 1
        jurusanList.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111) {
            if (resultCode == Activity.RESULT_OK) {
//                Toast.makeText(context, "Back", Toast.LENGTH_SHORT).show()
                page = 1
                jurusanList.clear()
                fetchData()
            }
        }
    }
}

