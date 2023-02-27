package ru.svolf.convertx.presentation.screens.about

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.svolf.convertx.R
import ru.svolf.convertx.databinding.ActivityAboutBinding

/**
 * Created by Snow Volf on 21.03.2017, 1:42
 */

class AboutFragment : Fragment() {
    lateinit var binding: ActivityAboutBinding
    lateinit var navController: NavController

    private var preambulaText = R.string.about_preambula

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)
        binding.content.aboutPreambula.text = Html.fromHtml(getString(preambulaText), Html.FROM_HTML_MODE_COMPACT)

        binding.content.aboutChangelist.setOnClickListener { _ -> navController.navigate(R.id.action_aboutFragment_to_changelistFragment) }
        binding.content.aboutGit.setOnClickListener { _ -> showGitDialog() }

        binding.fab4pda.setOnClickListener { _ -> goLink(requireActivity(), "https://4pda.to/forum/index.php?showuser=4324432", "4pda") }
        binding.fabVk.setOnClickListener { _ -> goLink(requireActivity(), "https://t.me/SnowVolf", "Telegram") }
        binding.fabEmail.setOnClickListener { _ -> goLink(requireActivity(), "mailto:svolf15@yandex.ru?subject=ConvertX", "Email") }
    }

    /**
     * Чтобы не создавать один и тот же код много раз
     * 3 строки кода заменяем одной, потом юзаем где хотим
     */
    //контекст берем от Activity, иначе будет падать на некоторых прошивках
    private fun goLink(context: Activity, link: String, info: String) {
        val uri = Uri.parse(link)
        val linkIntent = Intent(Intent.ACTION_VIEW, uri)
        //без этого флага крашится на некоторых устройствах
        linkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        
        context.startActivity(Intent.createChooser(linkIntent, info))
    }

    private fun showGitDialog() {
       MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.explore_git_src)
            .setMessage(R.string.explore_git_disclaimer)
            .setIcon(R.drawable.ic_source_explore)
            .setPositiveButton(R.string.git_hub) { dialog, which ->
                goLink(
                    requireActivity(),
                    "https://github.com/SnowVolf/ConvertX/",
                    "Исходники на GitHub"
                )
            }
            .setNegativeButton(R.string.forpda) { dialog, which ->
                goLink(
                    requireActivity(),
                    "https://goo.gl/bHrymP",
                    "4pda"
                )
            }
            .setNeutralButton(R.string.cancel, null)
            .show()
    }

}
