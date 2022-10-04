package ru.SnowVolf.convertx.ui.fragments.extra

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.view.menu.MenuBuilder
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.SnowVolf.convertx.R
import ru.SnowVolf.convertx.settings.DefStrings
import ru.SnowVolf.convertx.ui.activity.MainActivity
import ru.SnowVolf.convertx.ui.fragments.base.BaseFragment

/**
 * Created by Snow Volf on 21.03.2017, 1:42
 */

class AboutFragment : BaseFragment() {
    private var about: TextView? = null
    private var preambula: TextView? = null
    private var changelogButton: Button? = null
    private var gitButton: Button? = null
    private var appCaption: TextView? = null
    private var aboutCaption: TextView? = null
    lateinit var fab4pda: FloatingActionButton
    lateinit var fabvk: FloatingActionButton
    lateinit var fabemail: FloatingActionButton

    internal var preambulaText = "Программа пригодится людям работающим с <b>*.apk</b> <br/>" +
            "<b>Функции:</b><br>" +
            "<b>*</b> Кодирование/Декодирование Unicode<br>" +
            "<b>*</b> Кодирование/Декодирование Base64<br>" +
            "<b>*</b> Кодирование/Декодирование HEX<br>" +
            "<b>*</b> Просмотр и копирование палитры цветов в HEX<br>" +
            "<b>*</b> Кодирование <b>Adler32/CRC32/MD5/SHA-1, 224, 384, 512</b><br>" +
            "<b>*</b> <b>Regex Dragon:</b> Проверка регулярных выражений<br>" +
            "<b>*</b> <b>XML</b> unescaper<br>" +
            "<b>*</b> <b>Timestamp</b> converter<br><br><br>"
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
            "Copyright © (2017) <b>Artem Zhiganov</b>" +
            "<br><br><br>"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.activity_about, container, false)
        about = rootView!!.findViewById(R.id.about_content) as TextView
        preambula = rootView!!.findViewById(R.id.about_preambula) as TextView
        changelogButton = rootView!!.findViewById(R.id.about_changelist) as Button
        gitButton = rootView!!.findViewById(R.id.about_git) as Button
        appCaption = rootView!!.findViewById(R.id.about_caption) as TextView
        aboutCaption = rootView!!.findViewById(R.id.about_author) as TextView
        if (Build.VERSION.SDK_INT >= 24) {
            preambula!!.text = Html.fromHtml(preambulaText, 256)
        } else
            preambula!!.text = Html.fromHtml(preambulaText)

        if (Build.VERSION.SDK_INT >= 24) {
            about!!.text = Html.fromHtml(aboutText, 256)
        } else
            about!!.text = Html.fromHtml(aboutText)
        fab4pda = rootView!!.findViewById(R.id.fab_4pda) as FloatingActionButton
        fabvk = rootView!!.findViewById(R.id.fab_vk) as FloatingActionButton
        fabemail = rootView!!.findViewById(R.id.fab_email) as FloatingActionButton
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        TITLE = R.string.about_program
        (activity as MainActivity).setToolbarSubtitle("")
        val lobster = Typeface.createFromAsset(requireActivity().assets, "fonts/3952.ttf")
        appCaption!!.typeface = lobster
        aboutCaption!!.typeface = lobster
        /**
         * Чтобы ссылки, которые мы запихали в TextView, были кликабельны.
         * Метод с `Linkify.addLinks(about, Linkify.ALL);` у меня не заработал
         */
        about!!.movementMethod = LinkMovementMethod.getInstance()
        changelogButton!!.setOnClickListener { _ -> showChangelog() }
        gitButton!!.setOnClickListener { _ -> ExploreSource() }

        fab4pda.setOnClickListener { _ -> goLink(requireActivity(), "https://4pda.ru/forum/index.php?showuser=4324432", "4pda") }
        fabvk.setOnClickListener { _ -> goLink(requireActivity(), "https://vk.com/snowvolf", "vkontakte") }
        fabemail.setOnClickListener { _ -> goLink(requireActivity(), "mailto:svolf15@yandex.ru", "mail") }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        menu.add("").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!!.itemId == android.R.id.home)
            requireActivity().finish()
        return true
    }

    private fun showChangelog() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_container, ChangelistFragment())
            .addToBackStack(null)
            .commit()
    }

    fun ExploreSource() {
        //не юзать getApplicationContext! АААААА!
        showGitDialog()
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
        Log.w(DefStrings.TAG, "Activity New Task ok")
        context.startActivity(Intent.createChooser(linkIntent, info))
    }

    fun showGitDialog() {
        MaterialDialog.Builder(requireContext())
            .title(R.string.explore_git_src)
            .iconRes(R.drawable.ic_source_explore)
            .content(R.string.explore_git_disclaimer)
            .positiveText(R.string.git_hub)
            .negativeText(R.string.forpda)
            .neutralText(R.string.cancel) //применение goLink
            .onPositive(MaterialDialog.SingleButtonCallback { d: MaterialDialog?, w: DialogAction? ->
                goLink(
                    requireActivity(),
                    "https://github.com/SnowVolf/ConvertX/",
                    "Исходники на GitHub"
                )
            })
            .onNegative(MaterialDialog.SingleButtonCallback { d: MaterialDialog?, w: DialogAction? ->
                goLink(
                    requireActivity(),
                    "https://goo.gl/bHrymP",
                    "Исходники на 4pda"
                )
            })
            .show()
    }

}
