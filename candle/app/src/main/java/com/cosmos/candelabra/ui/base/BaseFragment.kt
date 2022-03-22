package com.cosmos.candelabra.ui.base

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            onBackPressed()
        }
    }

    protected open fun onBackPressed() {
        onBackPressedCallback.isEnabled = false
        activity?.onBackPressed()
    }
}
