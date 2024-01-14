package com.example.siakad.ui.matakuliah

import com.example.siakad.ui.gallery.Jurusan
import java.io.Serializable

data class Matakuliah(
    val id: Int,
    val nama: String,
    val jurusan: Jurusan
) : Serializable
