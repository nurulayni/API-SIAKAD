package com.example.siakad.ui.mahasiswa

import com.example.siakad.ui.Links
import com.example.siakad.ui.Meta

data class MahasiswaModel(
    val items: List<Mahasiswa>,
    val _links: Links,
    val _meta: Meta
)
