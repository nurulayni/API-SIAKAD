package com.example.siakad.ui.krs

import com.example.siakad.ui.Links
import com.example.siakad.ui.Meta

data class KrsModel(
        val items: List<Krs>,
        val _links: Links,
        val _meta: Meta
)
