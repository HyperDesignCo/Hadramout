package com.hyperdesign.myapplication.domain.Entity

import com.hyperdesign.myapplication.data.dto.PagesDTO


data class PagesResponseEntity(
    val pages: PageEntity
)

data class PageEntity(
    val id: Int,
    val title: String,
    val image: String,
    val text: String
)