package com.example.siakad.ui.tahunAjaran

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.siakad.R
import com.example.siakad.databinding.FragmentTahunAjarBinding
import com.example.siakad.ui.Config
import com.example.siakad.ui.gallery.FormJurusan
import com.example.siakad.ui.matakuliah.ApiMatakuliah
import com.example.siakad.ui.matakuliah.Matakuliah
import com.example.siakad.ui.matakuliah.MatakuliahAdapter
import com.example.siakad.ui.matakuliah.MatakuliahModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TahunAjarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TahunAjarFragment : Fragment() {
    private var _binding: com.example.siakad.databinding.FragmentTahunAjarBinding? = null
    private val binding get() = _binding!!
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TahunAjaranAdapter
    private var tahunAjarList = mutableListOf<Tahun>()
    private var isLoading = false
    private var totalData = 0;
    private var isLoadMore = false;
    private var page = 1



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTahunAjarBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(context, FormTahunAjar::class.java)
            intent.putExtra("isNew", 1)
            startActivityForResult(intent, 111)
        }
        tahunAjarList.clear()
        swipeRefreshLayout = root.findViewById(R.id.refresh)
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            tahunAjarList.clear()
            fetchData()
        }
        recyclerView = root.findViewById(R.id.listTahunAjar)
        adapter = TahunAjaranAdapter(tahunAjarList, object : TahunAjaranAdapter.OnItemClickListener{
            override fun click(tahunajar: Tahun) {
                val intent = Intent(context, FormTahunAjar::class.java)
                intent.putExtra("id", tahunajar.id)
                intent.putExtra("tahun", tahunajar.tahun)
                intent.putExtra("semester", tahunajar.semester)
                intent.putExtra("status", tahunajar.status)
                intent.putExtra("isNew", 0)
                startActivityForResult(intent, 111)
            }
        })
        val layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        swipeRefreshLayout.isRefreshing = true
        fetchData()
        return  root
    }

    fun fetchData() {
        isLoading = true
        val fecth = Retrofit.Builder()
            .baseUrl(Config.path)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiTahunAjar::class.java)
        val call = fecth.getTahunAjar(page)
        call?.enqueue(object : Callback<TahunModel> {
            override fun onResponse(call: Call<TahunModel>, response: Response<TahunModel>) {
                val metaData = response.body()?.items ?: emptyList()
                totalData = response.body()?._meta?.totalCount!!
                swipeRefreshLayout.isRefreshing = false
                tahunAjarList.addAll(metaData)
                adapter.notifyItemRangeInserted(adapter.itemCount, tahunAjarList.size)
                adapter.notifyDataSetChanged()
                page = page + response.body()?._meta?.currentPage!!
                isLoading = false
            }
            override fun onFailure(call: Call<TahunModel>, t: Throwable) {
                isLoading = false
                t.printStackTrace()
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        page = 1
        tahunAjarList.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111) {
            if (resultCode == Activity.RESULT_OK) {
//                Toast.makeText(context, "Back", Toast.LENGTH_SHORT).show()
                page = 1
                tahunAjarList.clear()
                fetchData()
            }
        }
    }

}