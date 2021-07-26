package me.showang.taipeizoo.repo

import android.util.Log
import me.showang.respect.suspend
import me.showang.taipeizoo.api.ApiFactory
import me.showang.taipeizoo.model.AreaInfo
import me.showang.taipeizoo.model.PlantInfo

class ZooRepository(private val apiFactory: ApiFactory) {

    private val mAreaInfoList = mutableListOf<AreaInfo>()
    private val plantInfoCacheMap = mutableMapOf<String, List<PlantInfo>>()

    val areaInfoList: List<AreaInfo> get() = mAreaInfoList

    suspend fun loadAreaInfo(callback: (List<AreaInfo>?) -> Unit) {
        if (mAreaInfoList.isEmpty()) {
            val result = try {
                apiFactory.createAreaIntroApi().suspend()
            } catch (e: Throwable) {
                null
            }
            result?.let { apiResult ->
                mAreaInfoList.clear()
                mAreaInfoList.addAll(apiResult.areaInfoList)
                callback(mAreaInfoList)
            } ?: run {
                callback(null)
            }
        } else {
            callback(mAreaInfoList)
        }
    }

    suspend fun loadPlantInfo(areaName: String, callback:(List<PlantInfo>?) -> Unit) {
        plantInfoCacheMap[areaName]?.takeIf { it.isNotEmpty() }?.let { plantInfoList ->
            callback(plantInfoList)
        } ?: run {
            try {
                apiFactory.createPlantInfoApi(areaName).suspend()
            } catch (e: Throwable) {
                Log.e(javaClass.simpleName, "loadPlantInfo fail", e)
                null
            }?.let { result ->
                plantInfoCacheMap[areaName] = result.plantInfoList
                callback(result.plantInfoList)
            } ?: callback(null)
        }
    }

}