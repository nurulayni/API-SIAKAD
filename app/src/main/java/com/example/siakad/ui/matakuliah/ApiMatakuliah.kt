package com.example.siakad.ui.matakuliah

import com.example.siakad.ui.mahasiswa.MahasiswaModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiMatakuliah {
    @GET("matakuliah")
    fun getMatakuliah(@Query("page") page: Int): Call<MatakuliahModel>
}