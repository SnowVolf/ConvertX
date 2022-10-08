package ru.svolf.convertx.ui.fragments.extra

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

    internal var preambulaText = "Программа пригодится людям работающим с <b>*.apk</b> <br/>" +
            "<b>Функции:</b><br>" +
            "<b>*</b> Кодирование/Декодирование Unicode<br>" +
            "<b>*</b> Кодирование/Декодирование Base64<br>" +
            "<b>*</b> Кодирование/Декодирование HEX<br>" +
            "<b>*</b> Просмотр и копирование палитры цветов в HEX<br>" +
            "<b>*</b> Кодирование <b>Adler32/CRC32/MD5/SHA-1, 224, 384, 512</b><br>" +
            "<b>*</b> Проверка регулярных выражений<br>" +
            "<b>*</b> XML unescaper<br>" +
            "<b>*</b> Timestamp converter<br><br><br>"
    internal var aboutText = "Автор и разработчик: <b><a href=\"http://4pda.ru/forum/index.php?showuser=4324432\">Snow Volf</a></b> (Артем Жиганов)<br>\n" +
            "<b>Контакты: \n</b><a href=\"http://4pda.ru/forum/index.php?act=qms&mid=4324432\">QMS на 4pda</a> <b>|</b> <a href=\"mailto:svolf15@yandex.ru\">svolf15@yandex.ru</a><br>\n" +
            "<b>Особая благодарность</b>:\n <b><a href=\"http://4pda.ru/forum/index.php?showuser=2359960\">keks40</a></b>, <b><a href=\"http://4pda.ru/forum/index.php?showuser=1216287\">Enyby</a></b>, <b><a href=\"http://4pda.ru/forum/index.php?showuser=1457199\">mas-slon</a></b>\n" +
            "<br>" +
            "<b>Использованы разработки:</b><br>" +
            "<b>Android Open Source Project</b> © (2005–2016). Licensed under The Apache License, version 2.0<br>" +
            "<b>Android Support Library</b> © (2005–2016). Licensed under The Apache License, version 2.0<br>" +
            "<b>Gradle Retrolambda Plugin</b> © (2013) Evan Tatarka. Licensed under The Apache License, version 2.0<br>" +
            "<b>Material Dialogs</b> © (2014–2016) Adian Follestad. Licensed under The MIT License<br>" +
            "<b>Material Design Icons</b> © (2014–2016) Google/Templarian/<a href=\"https://materialdesignicons.com\">Material Design Icons Community</a>. Licensed under The Apache License, Version 2.0<br>" +
            "<b>ForPDA 4.0</b> © (2016-2017) ForPDA Team (Evgeniy Nizamiev, Aleksandr Tainyk). Licensed under The Apache License, Version 2.0<br>" +
            "<b>JavaGirl</b> © (2016) Snow Volf (Artem Zhiganov). Licensed under The MIT License<br>" +
            "<b>ACRA</b> © (2013–2016) Kevin Gaudin. Licensed under The Apache License, Version 2.0<br>" +
            "<b>LeakCanary</b> © (2015) Square, Inc. Licensed under The Apache License, Version 2.0<br>" +
            "<b>Material Drawer</b> © (2016) Mike Penz. Licensed under The Apache License, Version 2.0<br><br>" +
            "Copyright © (2017-2023) <b>Artem Zhiganov</b>" +
            "<br><br><br>"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        binding.content.aboutPreambula.text = Html.fromHtml(preambulaText, Html.FROM_HTML_MODE_COMPACT)
        binding.content.aboutContent.text = Html.fromHtml(aboutText, Html.FROM_HTML_MODE_COMPACT)
        /**
         * Чтобы ссылки, которые мы запихали в TextView, были кликабельны.
         * Метод с `Linkify.addLinks(about, Linkify.ALL);` у меня не заработал
         */
        binding.content.aboutContent.movementMethod = LinkMovementMethod.getInstance()
        binding.content.aboutChangelist.setOnClickListener { _ -> navController.navigate(R.id.action_aboutFragment_to_changelistFragment) }
        binding.content.aboutGit.setOnClickListener { _ -> showGitDialog() }

        binding.fab4pda.setOnClickListener { _ -> goLink(requireActivity(), "https://4pda.ru/forum/index.php?showuser=4324432", "4pda") }
        binding.fabVk.setOnClickListener { _ -> goLink(requireActivity(), "https://vk.com/snowvolf", "vkontakte") }
        binding.fabEmail.setOnClickListener { _ -> goLink(requireActivity(), "mailto:svolf15@yandex.ru", "mail") }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            requireActivity().finish()
        return true
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
                    "Исходники на 4pda"
                )
            }
            .setNeutralButton(R.string.cancel, null)
            .show()
    }

}
