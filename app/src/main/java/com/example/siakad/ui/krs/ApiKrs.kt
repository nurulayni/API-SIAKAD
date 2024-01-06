package com.example.siakad.ui.krs

import com.example.siakad.ui.mahasiswa.MahasiswaModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiKrs {
    @GET("krs")
    fun getKrs(@Query("page") page: Int): Call<KrsModel>
}