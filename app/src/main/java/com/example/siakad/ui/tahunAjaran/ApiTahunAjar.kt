package com.example.siakad.ui.tahunAjaran

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiTahunAjar {
    @GET("tahun-ajaran")
    fun getTahunAjar(@Query("page") page: Int): Call<TahunModel>
}