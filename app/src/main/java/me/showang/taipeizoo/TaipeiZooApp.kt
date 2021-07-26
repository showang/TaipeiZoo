package me.showang.taipeizoo

import androidx.multidex.MultiDexApplication
import me.showang.taipeizoo.api.ApiFactory
import me.showang.taipeizoo.repo.ZooRepository
import me.showang.taipeizoo.viewmodel.AreaDetailViewModel
import me.showang.taipeizoo.viewmodel.AreaIntroViewModel
import me.showang.taipeizoo.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class TaipeiZooApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(
                module {
                    single { ApiFactory() }
                    single { ZooRepository(get()) }

                    viewModel { SplashViewModel(get()) }
                    viewModel { AreaIntroViewModel(get()) }
                    viewModel { AreaDetailViewModel(get()) }
                }
            ))
        }
    }
}