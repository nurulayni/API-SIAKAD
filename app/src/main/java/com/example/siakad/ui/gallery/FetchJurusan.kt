package com.example.siakad.ui.gallery

import com.example.siakad.ui.FormResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface FetchJurusan {
    @GET("jurusans")
    fun getJurusanData(@Query("page") page: Int): Call<JurusanModel>
    @POST("jurusans")
    fun postJurusanData(@Body dataPost: DataPost): Call<ResponseBody>
    @PUT("jurusans/update")
    fun updateJurusan(@Query("id") id: Int, @Body dataPost: DataPost): Call<ResponseBody>
    @DELETE("jurusans/delete")
    fun deleteJurusan(@Query("id") id: Int): Call<ResponseBody>
    @GET("jurusans/all")
    fun semuaJurusan(): Call<JurusanModel>
    @GET("jurusans")
    fun jurusan(@Query("nama_jurusan") nama_jurusan: String): Call<JurusanModel>
}