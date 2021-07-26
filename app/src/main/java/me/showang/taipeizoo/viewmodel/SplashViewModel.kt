package me.showang.taipeizoo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.showang.taipeizoo.repo.ZooRepository

class SplashViewModel(private val repository: ZooRepository) : ViewModel() {

    private val mInitSuccessLiveData: MutableLiveData<Boolean?> = MutableLiveData(null)

    val initSuccessLiveData: LiveData<Boolean?> get() = mInitSuccessLiveData

    fun initBasicData() {
        mInitSuccessLiveData.postValue(null)
        CoroutineScope(IO).launch {
            repository.loadAreaInfo {
                it?.let { mInitSuccessLiveData.postValue(true) }
                    ?: mInitSuccessLiveData.postValue(false)
            }
        }
    }

}