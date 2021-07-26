package me.showang.taipeizoo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import me.showang.taipeizoo.databinding.ActivityMainBinding
import me.showang.taipeizoo.utils.setupWithNavController
import me.showang.taipeizoo.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val navigationGraphList = listOf(R.navigation.nav_area_intro, R.navigation.nav_map)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.apply {
            initNavigationComponent()
        }.root)
        viewModel.initBasicData()
    }

    private fun ActivityMainBinding.initNavigationComponent() {
        bottomNavigation.setupWithNavController(
            navigationGraphList,
            supportFragmentManager,
            R.id.fragmentContainer,
            intent
        )
    }

}