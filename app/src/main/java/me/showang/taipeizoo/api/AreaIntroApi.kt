package me.showang.taipeizoo.api

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import me.showang.respect.RespectApi
import me.showang.respect.core.HttpMethod
import me.showang.taipeizoo.model.AreaInfo

class AreaIntroApi(query: String? = null, limit: Int? = null, offset: Int? = null) :
    RespectApi<AreaIntroApi.ApiResult>() {
    override val httpMethod = HttpMethod.GET
    override val url =
        "https://data.taipei/api/v1/dataset/5a0e5fbb-72f8-41c6-908e-2fb25eff9b8a?scope=resourceAquire"

    override val urlQueries = super.urlQueries.toMutableMap().apply {
        query?.let { put("q", it) }
        limit?.let { put("limit", it.toString()) }
        offset?.let { put("offset", it.toString()) }
    }

    override fun parse(bytes: ByteArray): ApiResult {
        return Gson().fromJson(String(bytes), AreaIntroResultEntity::class.java).run {
            ApiResult(
                result.offset,
                result.results.map {
                    it.parseToModel()
                }
            )
        }
    }

    data class ApiResult(
        val offset: Int,
        val areaInfoList: List<AreaInfo>
    )

    private fun AreaInfoEntity.parseToModel(): AreaInfo {
        return AreaInfo(
            id.toLong(),
            eName,
            eInfo,
            eCategory,
            ePicURL.replace("http", "https"),
            eURL.replace("http", "https"),
            eMemo.apply { println("memo: $this") }.takeIf {
                it.isNotEmpty()
            }
        )
    }

    private data class AreaInfoEntity(
        @SerializedName("E_Category")
        val eCategory: String,
        @SerializedName("E_Geo")
        val eGeo: String,
        @SerializedName("E_Info")
        val eInfo: String,
        @SerializedName("E_Memo")
        val eMemo: String,
        @SerializedName("E_Name")
        val eName: String,
        @SerializedName("E_no")
        val eNo: String,
        @SerializedName("E_Pic_URL")
        val ePicURL: String,
        @SerializedName("E_URL")
        val eURL: String,
        @SerializedName("_id")
        val id: Int
    )

    private data class Result(
        @SerializedName("count")
        val count: Int,
        @SerializedName("limit")
        val limit: Int,
        @SerializedName("offset")
        val offset: Int,
        @SerializedName("results")
        val results: List<AreaInfoEntity>,
        @SerializedName("sort")
        val sort: String
    )

    private data class AreaIntroResultEntity(
        @SerializedName("result")
        val result: Result
    )
}