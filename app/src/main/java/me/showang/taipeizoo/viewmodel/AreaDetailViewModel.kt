package me.showang.taipeizoo.viewmodel

import androidx.lifecycle.MutableLiveData
import github.showang.mypainter.transtate.core.Transform
import github.showang.mypainter.transtate.core.ViewEvent
import github.showang.mypainter.transtate.core.ViewState
import github.showang.transtate.TranstateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import me.showang.taipeizoo.model.PlantInfo
import me.showang.taipeizoo.repo.ZooRepository

class AreaDetailViewModel(private val repository: ZooRepository) :
    TranstateViewModel<AreaDetailViewModel.State>() {

    override val initState = State.Initializing()
    override var lastState: State = initState

    fun loadPlantInfo(category: String) {
        CoroutineScope(IO).launch {
            repository.loadPlantInfo(category) {
                it?.let { plaints ->
                    startTransform(Event.PlantsLoaded(plaints))
                } ?: startTransform(Event.LoadPlantsFail())
            }
        }
    }

    sealed class State : ViewState() {

        class Initializing : State() {
            override fun transform(
                byEvent: ViewEvent,
                liveData: MutableLiveData<Transform>
            ): ViewState {
                return when (byEvent) {
                    is Event.PlantsLoaded -> PlantInfoLoaded(byEvent.plantInfoList)
                    is Event.LoadPlantsFail -> LoadPlantInfoError()
                    else -> this
                }
            }
        }

        class PlantInfoLoaded(val plantInfoList: List<PlantInfo>) : State() {
            override fun transform(
                byEvent: ViewEvent,
                liveData: MutableLiveData<Transform>
            ): ViewState {
                return this
            }
        }

        class LoadPlantInfoError : State() {
            override fun transform(
                byEvent: ViewEvent,
                liveData: MutableLiveData<Transform>
            ): ViewState {
                return when (byEvent) {
                    is Event.ReloadPlants -> Initializing()
                    else -> this
                }
            }
        }

    }

    sealed class Event : ViewEvent() {

        class PlantsLoaded(val plantInfoList: List<PlantInfo>) : Event()

        class LoadPlantsFail() : Event()

        class ReloadPlants() : Event()
    }

}