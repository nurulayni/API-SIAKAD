package com.example.siakad.ui.mahasiswa

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiMahasiswa {
    @GET("mahasiswa")
    fun getMahasiswa(@Query("page") page: Int): Call<MahasiswaModel>
}