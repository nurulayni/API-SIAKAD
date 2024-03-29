package com.example.siakad.ui.krs

import com.example.siakad.ui.mahasiswa.Mahasiswa
import com.example.siakad.ui.tahunAjaran.Tahun
import java.io.Serializable

data class Krs(
    val id: Int,
    val mahasiswa: Mahasiswa,
    val tahunAjaran: Tahun
) : Serializable
