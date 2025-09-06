package com.hyperdesign.myapplication.data.mapper.profile.setting

import com.hyperdesign.myapplication.data.dto.PagesDTO
import com.hyperdesign.myapplication.data.dto.PagesResponseDto
import com.hyperdesign.myapplication.domain.Entity.PageEntity
import com.hyperdesign.myapplication.domain.Entity.PagesResponseEntity

fun PagesResponseDto.toDomain(): PagesResponseEntity{
    return PagesResponseEntity(
        pages = pages.tpDomain()
    )
}

fun PagesDTO.tpDomain(): PageEntity{
    return PageEntity(
        id = id,
        text = text,
        title = title,
        image = image
    )
}