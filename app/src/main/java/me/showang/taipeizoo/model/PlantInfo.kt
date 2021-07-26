package me.showang.taipeizoo.model

import java.io.Serializable

data class PlantInfo(
    val id: Long,
    val name: String,
    val otherCountryNames: Map<String, String>,
    val briefDesc: String,
    val feature: String,
    val family: String,
    val imageInfoList: List<ImageInfo>,
    val locations: List<String>
) : Serializable