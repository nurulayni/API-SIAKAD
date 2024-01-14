package com.example.siakad.ui.matakuliah

import com.example.siakad.ui.mahasiswa.MahasiswaModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiMatakuliah {
    @GET("matakuliah")
    fun getMatakuliah(@Query("page") page: Int): Call<MatakuliahModel>
    @GET("matakuliah")
    fun getListMatakuliah(@Query("id_jurusan") id_jurusan: Int, @Query("nama") nama: String): Call<MatakuliahModel>
    @POST("matakuliahs")
    fun postMatakuliah(@Body dataPost: PostMatakuliah) : Call<ResponseBody>
    @PUT("matakuliah/update")
    fun updateMatakuliah(@Query("id") id: Int, @Body dataPost: PostMatakuliah): Call<ResponseBody>
    @DELETE("matakuliah/delete")
    fun deleteMatakuliah(@Query("id") id: Int): Call<ResponseBody>
}