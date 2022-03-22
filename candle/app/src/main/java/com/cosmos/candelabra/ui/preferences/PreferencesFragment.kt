package com.cosmos.candelabra.ui.preferences

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.cosmos.candelabra.R
import com.cosmos.candelabra.data.model.preferences.UiPreferences
import com.cosmos.candelabra.databinding.IncludeMainAppbarBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: PreferencesViewModel by viewModels()

    private var nightModePreference: Preference? = null
    private var aboutPreference: Preference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // Theme doesn't reload properly when switching between light/dark mode
        // due to SplashScreen API.
        // Theme has to be set before starting the fragment as a workaround
        activity?.setTheme(R.style.Theme_Candelabra)

        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        initPreferences()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.fitsSystemWindows = true

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        initToolbar()
        bindViewModel()
    }

    private fun initToolbar() {
        view?.findViewById<View>(R.id.app_bar)?.let {
            val toolbarBinding = IncludeMainAppbarBinding.bind(it)
            toolbarBinding.toolbar.title = getString(R.string.preferences_fragment_title)
        }
    }

    private fun bindViewModel() {
        viewModel.nightMode.observe(viewLifecycleOwner) {
            UiPreferences.NightMode.asIndex(it)?.let { index ->
                val nightModeArray = resources.getStringArray(R.array.pref_night_mode_labels)
                nightModePreference?.summary = nightModeArray.getOrNull(index)
            }
        }
    }

    private fun initPreferences() {
        nightModePreference = findPreference<Preference>(
            UiPreferences.PreferencesKeys.NIGHT_MODE.name
        )?.apply {
            setOnPreferenceClickListener {
                viewModel.nightMode.value?.let { mode ->
                    UiPreferences.NightMode.asIndex(mode)?.let { index ->
                        showNightModeDialog(index)
                    }
                }
                true
            }
        }

        aboutPreference = findPreference<Preference>("about")?.apply {
            setOnPreferenceClickListener {
                openAbout()
                true
            }
        }
    }

    private fun showNightModeDialog(checkedItem: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_night_mode_title)
            .setSingleChoiceItems(R.array.pref_night_mode_labels, checkedItem) { dialog, which ->
                UiPreferences.NightMode.asMode(which)?.let { mode ->
                    updateNightMode(mode)
                    dialog.dismiss()
                }
            }
            .show()
    }

    private fun updateNightMode(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        viewModel.setNightMode(mode)
    }

    private fun openAbout() {
        exitTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        findNavController().navigate(R.id.about)
    }
}
