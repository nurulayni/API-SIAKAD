package com.example.siakad.ui.mahasiswa

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiMahasiswa {
    @GET("mahasiswas")
    fun getMahasiswa(@Query("page") page: Int): Call<MahasiswaModel>

    @GET("mahasiswas")
    fun mahasiswa(@Query("nim") nim: String, @Query("id_jurusan") id_jurusan: Int): Call<MahasiswaModel>
}