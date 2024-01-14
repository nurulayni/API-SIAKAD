package com.example.siakad.ui.mahasiswa

import com.example.siakad.ui.gallery.Jurusan
import java.io.Serializable

//DataMahasiswa
data class Mahasiswa(val id: Int, val nim: String, val nama: String, val jurusan: Jurusan) :
    Serializable
