package com.example.siakad.ui.mahasiswa

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.siakad.R
import com.example.siakad.databinding.FragmentMahasiswaBinding
import com.example.siakad.ui.Config
import com.example.siakad.ui.gallery.FetchJurusan
import com.example.siakad.ui.gallery.FormJurusan
import com.example.siakad.ui.gallery.Jurusan
import com.example.siakad.ui.gallery.JurusanAdapter
import com.example.siakad.ui.gallery.JurusanModel
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
 * Use the [MahasiswaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MahasiswaFragment : Fragment() {

    private var _binding: FragmentMahasiswaBinding? = null
    private val binding get() = _binding!!
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MahasiswaAdapter
    private var mahasiswaList = mutableListOf<Mahasiswa>()
    private var isLoading = false
    private var totalData = 0;
    private var isLoadMore = false;
    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMahasiswaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mahasiswaList.clear()
        swipeRefreshLayout = root.findViewById(R.id.refresh)
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            mahasiswaList.clear()
            fetchData()
        }
        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(context, FormMahasiswa::class.java)
            startActivityForResult(intent, 121)
        }
        recyclerView = root.findViewById(R.id.listMahasiswa)
        adapter = MahasiswaAdapter(mahasiswaList, object : MahasiswaAdapter.OnItemClickListener{
            override fun click(mahasiswa: Mahasiswa) {

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
            .create(ApiMahasiswa::class.java)
        val call = fecth.getMahasiswa(page)
        call?.enqueue(object : Callback<MahasiswaModel> {
            override fun onResponse(call: Call<MahasiswaModel>, response: Response<MahasiswaModel>) {
                val metaData = response.body()?.items ?: emptyList()
                totalData = response.body()?._meta?.totalCount!!
                swipeRefreshLayout.isRefreshing = false
                mahasiswaList.addAll(metaData)
                adapter.notifyItemRangeInserted(adapter.itemCount, mahasiswaList.size)
                adapter.notifyDataSetChanged()
                page = page + response.body()?._meta?.currentPage!!
                isLoading = false
            }
            override fun onFailure(call: Call<MahasiswaModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        page = 1
        mahasiswaList.clear()
    }
}