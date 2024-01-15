package com.example.siakad.ui.mahasiswa

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiMahasiswa {
    @GET("mahasiswas")
    fun getMahasiswa(@Query("page") page: Int): Call<MahasiswaModel>
    @GET("mahasiswas")
    fun mahasiswa(@Query("nim") nim: String, @Query("id_jurusan") id_jurusan: Int): Call<MahasiswaModel>
    @POST("mahasiswas")
    fun postMahasiswa(@Body dataPost: PostMahasiswa): Call<ResponseBody>
}