package me.showang.taipeizoo.utils

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class NavControllerHelper {

    private val navigationMutex = Mutex()
    private var isNavigated: Boolean = false

    fun onResume() {
        isNavigated = false
    }

    fun navigate(navController: NavController, @IdRes resId: Int, args: Bundle? = null) {
        val haveNavigated = runBlocking {
            navigationMutex.withLock {
                if (isNavigated) true
                else {
                    isNavigated = true
                    false
                }
            }
        }
        if (!haveNavigated) {
            navController.navigate(resId, args)
        }
    }

}