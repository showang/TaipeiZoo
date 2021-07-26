package me.showang.taipeizoo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import me.showang.taipeizoo.databinding.ActivitySplashBinding
import me.showang.taipeizoo.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()

    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.apply {
            initErrorRetryView()
        }.root)
        viewModel.initBasicData()
    }

    private fun ActivitySplashBinding.initErrorRetryView() {
        retryButton.setOnClickListener {
            viewModel.initBasicData()
            updateErrorRetryView(false)
        }
        viewModel.initSuccessLiveData.observe({ lifecycle }) { isSuccess ->
            isSuccess?.let {
                if (isSuccess) {
                    Intent(this@SplashActivity, MainActivity::class.java).apply {
                        putExtras(intent)
                    }.run(::startActivity)
                    finish()
                } else {
                    updateErrorRetryView(true)
                }
            }
        }
    }

    private fun ActivitySplashBinding.updateErrorRetryView(isVisible: Boolean) {
        progress.isVisible = !isVisible
        errorText.isVisible = isVisible
        retryButton.isVisible = isVisible
    }
}