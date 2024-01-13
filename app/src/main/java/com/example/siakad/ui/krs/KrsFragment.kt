package com.example.siakad.ui.krs

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
import com.example.siakad.databinding.FragmentKrsBinding
import com.example.siakad.databinding.FragmentMahasiswaBinding
import com.example.siakad.ui.Config
import com.example.siakad.ui.gallery.FormJurusan
import com.example.siakad.ui.mahasiswa.ApiMahasiswa
import com.example.siakad.ui.mahasiswa.Mahasiswa
import com.example.siakad.ui.mahasiswa.MahasiswaAdapter
import com.example.siakad.ui.mahasiswa.MahasiswaModel
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
 * Use the [KrsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KrsFragment : Fragment() {
    private var _binding: FragmentKrsBinding? = null
    private val binding get() = _binding!!
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: KrsAdapter
    private var krsList = mutableListOf<Krs>()
    private var isLoading = false
    private var totalData = 0;
    private var isLoadMore = false;
    private var page = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentKrsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(context, FormKrsActivity::class.java)
            intent.putExtra("isNew", 1)
            startActivityForResult(intent, 111)
        }
        krsList.clear()
        swipeRefreshLayout = root.findViewById(R.id.refresh)
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            krsList.clear()
            fetchData()
        }
        recyclerView = root.findViewById(R.id.listKrs)
        adapter = KrsAdapter(krsList, object : KrsAdapter.OnItemClickListener{
            override fun click(krs: Krs) {
                val  intent= Intent(context,DaftarKrs::class.java)
                intent.putExtra("id_krs", krs.id)
                startActivity(intent)
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
            .create(ApiKrs::class.java)
        val call = fecth.getKrs(page)
        call?.enqueue(object : Callback<KrsModel> {
            override fun onResponse(call: Call<KrsModel>, response: Response<KrsModel>) {
                val metaData = response.body()?.items ?: emptyList()
                totalData = response.body()?._meta?.totalCount!!
                swipeRefreshLayout.isRefreshing = false
                krsList.addAll(metaData)
                adapter.notifyItemRangeInserted(adapter.itemCount, krsList.size)
                adapter.notifyDataSetChanged()
                page = page + response.body()?._meta?.currentPage!!
                isLoading = false
            }
            override fun onFailure(call: Call<KrsModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

}