package com.example.siakad.ui.tahunAjaran

import com.example.siakad.ui.gallery.DataPost
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiTahunAjar {
    @GET("tahun-ajaran")
    fun getTahunAjar(@Query("page") page: Int): Call<TahunModel>
    @POST("tahun-ajarans")
    fun postTahunAjar(@Body dataPost: Tahun): Call<ResponseBody>
    @PUT("tahun-ajaran/update")
    fun updateTahunAjar(@Query("id") id: Int, @Body dataPost: Tahun): Call<ResponseBody>
    @DELETE("tahun-ajaran/delete")
    fun deleteTahunAjar(@Query("id") id: Int): Call<ResponseBody>
}