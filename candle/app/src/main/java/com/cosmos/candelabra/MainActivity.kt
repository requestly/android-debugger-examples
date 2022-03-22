package com.cosmos.candelabra

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.cosmos.candelabra.databinding.ActivityMainBinding
import com.cosmos.candelabra.ui.search.SearchFragmentDirections
import com.cosmos.candelabra.util.extension.setStatusBarTheme
import com.cosmos.candelabra.util.extension.themeBoolean
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainBinding

    private val uiViewModel: UiViewModel by viewModels()

    private val currentNavigationFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.fragment_container)
            ?.childFragmentManager
            ?.fragments
            ?.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomAppBar) { _, windowInsets ->
            windowInsets
        }

        bindViewModel()
        binding.fabAdd.setOnClickListener { openSearch() }
    }

    override fun onStart() {
        super.onStart()
        initNavigation()
    }

    private fun bindViewModel() {
        uiViewModel.navigationVisibility.observe(this) {
            showNavigation(it)
        }
    }

    private fun initNavigation() {
        findNavController(R.id.fragment_container).run {
            addOnDestinationChangedListener(this@MainActivity)
            binding.bottomNavigation.setupWithNavController(this)
        }
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.overview -> openOverview()
                R.id.watchlist -> openWatchlist()
                R.id.preferences -> openPreferences()
            }
            true
        }
    }

    private fun openOverview() {
        resetTransitions()
        findNavController(R.id.fragment_container).navigate(R.id.openOverview)
    }

    private fun openWatchlist() {
        resetTransitions()
        findNavController(R.id.fragment_container).navigate(R.id.openWatchlist)
    }

    private fun openPreferences() {
        resetTransitions()
        findNavController(R.id.fragment_container).navigate(R.id.openPreferences)
    }

    private fun openSearch() {
        val motionDuration = resources.getInteger(R.integer.motion_duration_large).toLong()

        currentNavigationFragment?.apply {
            exitTransition = MaterialElevationScale(false).apply {
                duration = motionDuration
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration = motionDuration
            }
        }

        val directions = SearchFragmentDirections.openSearch()
        findNavController(R.id.fragment_container).navigate(directions)
    }

    private fun showNavigation(show: Boolean) {
        if (show) {
            binding.bottomAppBar.apply {
                visibility = View.VISIBLE
                performShow()
            }
            binding.fabAdd.show()
        } else {
            binding.bottomAppBar.performHide()
            binding.bottomAppBar.animate().setListener(object : AnimatorListenerAdapter() {
                var isCanceled = false
                override fun onAnimationEnd(animation: Animator?) {
                    if (isCanceled) return

                    binding.bottomAppBar.visibility = View.GONE
                    binding.fabAdd.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isCanceled = true
                }
            })
        }
    }

    private fun resetTransitions() {
        currentNavigationFragment?.apply {
            exitTransition = MaterialFadeThrough().apply {
                duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            }
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        when (destination.id) {
            R.id.search, R.id.about -> {
                uiViewModel.setNavigationVisibility(false)
            }
            R.id.quote_details -> {
                uiViewModel.setNavigationVisibility(false)
                setStatusBarTheme(false)
            }
            else -> {
                uiViewModel.setNavigationVisibility(true)
                setStatusBarTheme(themeBoolean(android.R.attr.windowLightStatusBar))
            }
        }
    }
}
