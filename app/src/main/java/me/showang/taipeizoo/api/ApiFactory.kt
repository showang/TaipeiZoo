package me.showang.taipeizoo.api

class ApiFactory {

    fun createAreaIntroApi(): AreaIntroApi {
        return AreaIntroApi()
    }

    fun createPlantInfoApi(areaName: String): PlantInfoApi {
        return PlantInfoApi(areaName)
    }

}