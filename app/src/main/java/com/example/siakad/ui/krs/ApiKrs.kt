package com.example.siakad.ui.krs

import com.example.siakad.ui.mahasiswa.MahasiswaModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiKrs {
    @GET("krs")
    fun getKrs(@Query("page") page: Int): Call<KrsModel>

    @GET("detail-krs")
    fun getMatakuliahKrs(@Query("id_krs") id_krs: Int): Call<MatakuliahModel>

    fun deleteMatakuliahKrs(@Query("id") id: Int): Call<ResponseBody>
}