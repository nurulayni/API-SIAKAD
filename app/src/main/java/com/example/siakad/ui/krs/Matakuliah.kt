package com.example.siakad.ui.krs

import com.example.siakad.ui.matakuliah.Matakuliah

data class Matakuliah(
    val id: Int,
    val krs: Krs,
    val matakuliah: Matakuliah
)
