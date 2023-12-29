package com.example.siakad.ui.matakuliah

import com.example.siakad.ui.Links
import com.example.siakad.ui.Meta

data class MatakuliahModel(
    val items: List<Matakuliah>,
    val _links: Links,
    val _meta: Meta
)
