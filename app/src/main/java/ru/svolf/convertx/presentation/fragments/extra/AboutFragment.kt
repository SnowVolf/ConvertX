package ru.svolf.convertx.presentation.fragments.extra

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ru.svolf.convertx.R
import ru.svolf.convertx.databinding.ActivityAboutBinding

/**
 * Created by Snow Volf on 21.03.2017, 1:42
 */

class AboutFragment : Fragment() {
    lateinit var binding: ActivityAboutBinding
    lateinit var navController: NavController

    internal var preambulaText = R.string.about_preambula
    internal var aboutText = R.string.about_about

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)
        binding.content.aboutPreambula.text = Html.fromHtml(getString(preambulaText), Html.FROM_HTML_MODE_COMPACT)
        binding.content.aboutContent.text = Html.fromHtml(getString(aboutText), Html.FROM_HTML_MODE_COMPACT)
        /**
         * Чтобы ссылки, которые мы запихали в TextView, были кликабельны.
         * Метод с `Linkify.addLinks(about, Linkify.ALL);` у меня не заработал
         */
        binding.content.aboutContent.movementMethod = LinkMovementMethod.getInstance()
        binding.content.aboutChangelist.setOnClickListener { _ -> navController.navigate(R.id.action_aboutFragment_to_changelistFragment) }
        binding.content.aboutGit.setOnClickListener { _ -> showGitDialog() }

        binding.fab4pda.setOnClickListener { _ -> goLink(requireActivity(), "https://4pda.to/forum/index.php?showuser=4324432", "4pda") }
        binding.fabVk.setOnClickListener { _ -> goLink(requireActivity(), "https://t.me/SnowVolf", "Telegram") }
        binding.fabEmail.setOnClickListener { _ -> goLink(requireActivity(), "mailto:svolf15@yandex.ru?subject=ConvertX", "Email") }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }

    /**
     * Чтобы не создавать один и тот же код много раз
     * 3 строки кода заменяем одной, потом юзаем где хотим
     */
    //контекст берем от Activity, иначе будет падать на некоторых прошивках
    fun goLink(context: Activity, link: String, info: String) {
        val uri = Uri.parse(link)
        val linkIntent = Intent(Intent.ACTION_VIEW, uri)
        //без этого флага крашится на некоторых устройствах
        linkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        
        context.startActivity(Intent.createChooser(linkIntent, info))
    }

    fun showGitDialog() {
        AlertDialog.Builder(requireContext())
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
