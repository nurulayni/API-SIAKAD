package com.example.siakad.ui.krs

import com.example.siakad.ui.mahasiswa.MahasiswaModel
import com.example.siakad.ui.tahunAjaran.Tahun
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiKrs {
    @GET("krs")
    fun getKrs(@Query("page") page: Int): Call<KrsModel>
    @POST("krs")
    fun postKrs(@Body dataPost: KrsPost): Call<ResponseBody>
    @PUT("krs/update")
    fun updateKrs(@Query("id") id: Int, @Body dataPost: KrsPost): Call<ResponseBody>
    @GET("detail-krs")
    fun getMatakuliahKrs(@Query("id_krs") id_krs: Int): Call<MatakuliahModel>

    fun deleteMatakuliahKrs(@Query("id") id: Int): Call<ResponseBody>
}