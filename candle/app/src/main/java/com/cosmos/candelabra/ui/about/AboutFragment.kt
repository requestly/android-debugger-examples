package com.cosmos.candelabra.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cosmos.candelabra.BuildConfig
import com.cosmos.candelabra.R
import com.cosmos.candelabra.data.model.CreditItem
import com.cosmos.candelabra.databinding.FragmentAboutBinding
import com.cosmos.candelabra.ui.base.BaseFragment
import com.cosmos.candelabra.util.extension.openExternalLink
import com.cosmos.candelabra.util.extension.themeColor
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

@ExperimentalStdlibApi
class AboutFragment : BaseFragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    private lateinit var creditAdapter: CreditAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { root, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            root.updatePadding(left = insets.left, top = insets.top, right = insets.right)

            windowInsets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.listAbout) { list, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            list.updatePadding(bottom = insets.bottom)

            windowInsets
        }

        initAppBar()
        initAboutSection()
        initRecyclerView()
        initCredits()
    }

    private fun initCredits(coroutinesContext: CoroutineContext = Dispatchers.Default) {
        lifecycleScope.launch {
            val items = withContext(coroutinesContext) {
                buildList {
                    add(CreditItem.Section(R.string.about_section_credits))
                    addAll(CREDITS.sortedBy { it.title })
                    add(CreditItem.Section(R.string.about_section_libraries))
                    addAll(LIBRARIES.sortedBy { it.title })
                    add(CreditItem.Section(R.string.about_section_contributors))
                    addAll(CONTRIBUTORS.sortedBy { it.name })
                }
            }
            creditAdapter.submitList(items)
        }
    }

    private fun initRecyclerView() {
        creditAdapter = CreditAdapter {
            if (it is CreditItem.Credit) {
                showCreditDialog(it)
            } else if (it is CreditItem.Contributor) {
                openExternalLink(it.link)
            }
        }

        binding.listAbout.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = creditAdapter
        }
    }

    private fun initAboutSection() {
        binding.appVersion.text = BuildConfig.VERSION_NAME
    }

    private fun initAppBar() {
        binding.includeAppbar.toolbar.run {
            title = getString(R.string.about_fragment_title)
            setNavigationIcon(R.drawable.ic_arrow_back)
            activity?.themeColor(R.attr.colorOnBackground)?.let { setNavigationIconTint(it) }
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun showCreditDialog(credit: CreditItem.Credit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(credit.title)
            .setMessage(credit.description)
            .setPositiveButton(R.string.dialog_credit_show_website) { _, _ ->
                openExternalLink(credit.link)
            }
            .setNegativeButton(R.string.dialog_credit_show_license) { _, _ ->
                openExternalLink(credit.licenseLink)
            }
            .setCancelable(true)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val CREDITS: List<CreditItem.Credit> by lazy {
            listOf(
                CreditItem.Credit(
                    "Feather",
                    "Cole Bemis",
                    "Simply beautiful open source icons",
                    "https://feathericons.com/",
                    CreditItem.Credit.LicenseType.MIT,
                    "https://github.com/feathericons/feather/blob/master/LICENSE"
                )
            )
        }

        private val LIBRARIES: List<CreditItem.Credit> by lazy {
            listOf(
                CreditItem.Credit(
                    "Dagger Hilt",
                    "Google",
                    "Hilt provides a standard way to incorporate Dagger dependency injection into an Android application.",
                    "https://github.com/google/dagger",
                    CreditItem.Credit.LicenseType.APACHE_V2,
                    "https://github.com/google/dagger/blob/master/LICENSE.txt"
                ),
                CreditItem.Credit(
                    "Material Components for Android",
                    "Google",
                    "Modular and customizable Material Design UI components for Android",
                    "https://github.com/material-components/material-components-android",
                    CreditItem.Credit.LicenseType.APACHE_V2,
                    "https://github.com/material-components/material-components-android/blob/master/LICENSE"
                ),
                CreditItem.Credit(
                    "Retrofit",
                    "Square",
                    "A type-safe HTTP client for Android and the JVM",
                    "https://github.com/square/retrofit",
                    CreditItem.Credit.LicenseType.APACHE_V2,
                    "https://github.com/square/retrofit/blob/master/LICENSE.txt"
                ),
                CreditItem.Credit(
                    "Moshi",
                    "Square",
                    "A modern JSON library for Kotlin and Java.",
                    "https://github.com/square/moshi",
                    CreditItem.Credit.LicenseType.APACHE_V2,
                    "https://github.com/square/moshi/blob/master/LICENSE.txt"
                ),
                CreditItem.Credit(
                    "Coil",
                    "Coil Contributors",
                    "Image loading for Android backed by Kotlin Coroutines.",
                    "https://github.com/coil-kt/coil",
                    CreditItem.Credit.LicenseType.APACHE_V2,
                    "https://github.com/coil-kt/coil/blob/master/LICENSE.txt"
                ),
                CreditItem.Credit(
                    "MPAndroidChart",
                    "Philipp Jahoda",
                    "A powerful & easy to use chart library for Android",
                    "https://github.com/PhilJay/MPAndroidChart",
                    CreditItem.Credit.LicenseType.APACHE_V2,
                    "https://github.com/PhilJay/MPAndroidChart/blob/master/LICENSE"
                ),
                CreditItem.Credit(
                    "Ticker",
                    "Robinhood Markets, Inc.",
                    "An Android text view with scrolling text change animation",
                    "https://github.com/robinhood/ticker",
                    CreditItem.Credit.LicenseType.APACHE_V2,
                    "https://github.com/robinhood/ticker/blob/master/LICENSE.txt"
                ),
            )
        }

        private val CONTRIBUTORS: List<CreditItem.Contributor> by lazy {
            listOf(
                CreditItem.Contributor(
                    "Diego Sanguinetti",
                    "@sguinetti",
                    R.string.contributor_sguinetti_description,
                    "https://gitlab.com/sguinetti"
                )
            )
        }
    }
}
