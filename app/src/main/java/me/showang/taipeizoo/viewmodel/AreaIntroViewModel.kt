package me.showang.taipeizoo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.showang.taipeizoo.model.AreaInfo
import me.showang.taipeizoo.repo.ZooRepository

class AreaIntroViewModel(private val repository: ZooRepository) : ViewModel() {

    private val mAreaInfoListLiveData = MutableLiveData(repository.areaInfoList)

    val areaInfoListLiveData: LiveData<List<AreaInfo>> get() = mAreaInfoListLiveData

    fun fetchData() {
        mAreaInfoListLiveData.postValue(repository.areaInfoList)
    }

}