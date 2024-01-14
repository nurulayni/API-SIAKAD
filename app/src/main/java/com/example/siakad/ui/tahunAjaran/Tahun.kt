package com.example.siakad.ui.tahunAjaran

import java.io.Serializable

data class Tahun(
    val id: Int,
    val tahun: Int,
    val semester: Int,
    val status: Int
) : Serializable
