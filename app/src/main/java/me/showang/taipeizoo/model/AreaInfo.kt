package me.showang.taipeizoo.model

import java.io.Serializable

data class AreaInfo(
    val id: Long,
    val name: String,
    val description: String,
    val category: String,
    val imageUrl: String,
    val webUrl: String,
    val memo: String?
) : Serializable