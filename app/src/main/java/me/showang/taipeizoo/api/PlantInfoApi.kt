package me.showang.taipeizoo.api

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import me.showang.respect.RespectApi
import me.showang.respect.core.HttpMethod
import me.showang.taipeizoo.model.ImageInfo
import me.showang.taipeizoo.model.PageInfo
import me.showang.taipeizoo.model.PlantInfo

class PlantInfoApi(query: String) : RespectApi<PlantInfoApi.ApiResult>() {
    override val httpMethod = HttpMethod.GET
    override val url =
        "https://data.taipei/api/v1/dataset/f18de02f-b6c9-47c0-8cda-50efad621c14?scope=resourceAquire"
    override val urlQueries: Map<String, String> = super.urlQueries.toMutableMap().apply {
        put("q", query)
    }

    override fun parse(bytes: ByteArray): ApiResult {

        return Gson().fromJson(String(bytes), PlantInfoApiResultEntity::class.java).result.run {
            ApiResult(
                PageInfo(offset, plantInfos.size, this.limit),
                plantInfos.map { it.parseToModel() }
            )
        }
    }

    data class ApiResult(
        val pageInfo: PageInfo,
        val plantInfoList: List<PlantInfo>
    )

    private fun PlantInfoEntity.parseToModel(): PlantInfo {
        return PlantInfo(
            id.toLong(),
            FNameCh,
            mutableMapOf<String, String>().apply {
                put("latin", fNameLatin)
                put("en", fNameEn)
            },
            fBrief,
            fFeature,
            fFamily,
            mutableListOf<ImageInfo>().apply {
                if (fPic01URL.isNotEmpty()) add(ImageInfo(fPic01URL.replace("http:","https:"), fPic01ALT))
                if (fPic02URL.isNotEmpty()) add(ImageInfo(fPic02URL.replace("http:","https:"), fPic02ALT))
                if (fPic03URL.isNotEmpty()) add(ImageInfo(fPic03URL.replace("http:","https:"), fPic03ALT))
                if (fPic04URL.isNotEmpty()) add(ImageInfo(fPic04URL.replace("http:","https:"), fPic04ALT))
            },
            fLocation.split("；")
        )
    }

    data class PlantInfoEntity(
        @SerializedName("F_AlsoKnown")
        val fAlsoKnown: String,
        @SerializedName("F_Brief")
        val fBrief: String,
        @SerializedName("F_CID")
        val fCID: String,
        @SerializedName("F_Code")
        val fCode: String,
        @SerializedName("F_Family")
        val fFamily: String,
        @SerializedName("F_Feature")
        val fFeature: String,
        @SerializedName("F_Function＆Application")
        val fFunction: String,
        @SerializedName("F_Genus")
        val fGenus: String,
        @SerializedName("F_Geo")
        val fGeo: String,
        @SerializedName("F_Keywords")
        val fKeywords: String,
        @SerializedName("F_Location")
        val fLocation: String,
        @SerializedName("F_Name_En")
        val fNameEn: String,
        @SerializedName("F_Name_Latin")
        val fNameLatin: String,
        @SerializedName("F_pdf01_ALT")
        val fPdf01ALT: String,
        @SerializedName("F_pdf01_URL")
        val fPdf01URL: String,
        @SerializedName("F_pdf02_ALT")
        val fPdf02ALT: String,
        @SerializedName("F_pdf02_URL")
        val fPdf02URL: String,
        @SerializedName("F_Pic01_ALT")
        val fPic01ALT: String,
        @SerializedName("F_Pic01_URL")
        val fPic01URL: String,
        @SerializedName("F_Pic02_ALT")
        val fPic02ALT: String,
        @SerializedName("F_Pic02_URL")
        val fPic02URL: String,
        @SerializedName("F_Pic03_ALT")
        val fPic03ALT: String,
        @SerializedName("F_Pic03_URL")
        val fPic03URL: String,
        @SerializedName("F_Pic04_ALT")
        val fPic04ALT: String,
        @SerializedName("F_Pic04_URL")
        val fPic04URL: String,
        @SerializedName("F_Summary")
        val fSummary: String,
        @SerializedName("F_Update")
        val fUpdate: String,
        @SerializedName("F_Vedio_URL")
        val fVedioURL: String,
        @SerializedName("F_Voice01_ALT")
        val fVoice01ALT: String,
        @SerializedName("F_Voice01_URL")
        val fVoice01URL: String,
        @SerializedName("F_Voice02_ALT")
        val fVoice02ALT: String,
        @SerializedName("F_Voice02_URL")
        val fVoice02URL: String,
        @SerializedName("F_Voice03_ALT")
        val fVoice03ALT: String,
        @SerializedName("F_Voice03_URL")
        val fVoice03URL: String,
        @SerializedName("_full_count")
        val fullCount: String,
        @SerializedName("_id")
        val id: Int,
        @SerializedName("rank")
        val rank: Double,
        @SerializedName("\uFEFFF_Name_Ch")
        val FNameCh: String
    )

    data class ResultEntity(
        @SerializedName("count")
        val count: Int,
        @SerializedName("limit")
        val limit: Int,
        @SerializedName("offset")
        val offset: Int,
        @SerializedName("results")
        val plantInfos: List<PlantInfoEntity>,
        @SerializedName("sort")
        val sort: String
    )

    data class PlantInfoApiResultEntity(
        @SerializedName("result")
        val result: ResultEntity
    )
}