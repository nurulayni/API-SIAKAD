package com.example.siakad.ui.matakuliah

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.siakad.R
import com.example.siakad.databinding.FragmentMatakuliahBinding
import com.example.siakad.ui.Config
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
 * Use the [MatakuliahFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MatakuliahFragment : Fragment() {

    private var _binding: FragmentMatakuliahBinding? = null
    private val binding get() = _binding!!
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MatakuliahAdapter
    private var matakuliahList = mutableListOf<Matakuliah>()
    private var isLoading = false
    private var totalData = 0;
    private var isLoadMore = false;
    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMatakuliahBinding.inflate(inflater, container, false)
        val root: View = binding.root
        matakuliahList.clear()
        swipeRefreshLayout = root.findViewById(R.id.refresh)
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            matakuliahList.clear()
            fetchData()
        }
        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(context, MatakuliahForm::class.java)
            intent.putExtra("isNew", 1)
            startActivityForResult(intent, 111)
        }
        recyclerView = root.findViewById(R.id.listMatakuliah)
        adapter = MatakuliahAdapter(matakuliahList, object : MatakuliahAdapter.OnItemClickListener{
            override fun click(matakuliah: Matakuliah) {
                val intent = Intent(context, MatakuliahForm::class.java)
                intent.putExtra("isNew", 0)
                intent.putExtra("matakuliah", matakuliah)
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
        swipeRefreshLayout.isRefreshing = true
        isLoading = true
        val fecth = Retrofit.Builder()
            .baseUrl(Config.path)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiMatakuliah::class.java)
        val call = fecth.getMatakuliah(page)
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
                isLoading = false
                t.printStackTrace()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        page = 1
        matakuliahList.clear()
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


}