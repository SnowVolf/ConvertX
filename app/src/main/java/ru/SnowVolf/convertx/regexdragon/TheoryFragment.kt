package ru.SnowVolf.convertx.regexdragon

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebView.HitTestResult
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import ru.SnowVolf.convertx.R
import ru.SnowVolf.convertx.regexdragon.UrlExtensions.showChoiceDialog
import ru.SnowVolf.convertx.settings.DefStrings.TAG
import ru.SnowVolf.convertx.ui.Toasty
import ru.SnowVolf.convertx.utils.RuntimePermission.verifyStoragePermissions
import java.util.*

class TheoryFragment : Fragment() {
    private var mWebView: WebView? = null
    val webViews: Queue<ExtendedWebView> = LinkedList()
    private val webViewCleaner = Timer()
    private lateinit var rootView: View
    private var mSwipeRefresh: SwipeRefreshLayout? = null
    private val swipeColor = intArrayOf(R.color.blue500, R.color.green500, R.color.red500)

    init {
        webViewCleaner.schedule(MemoryLeakFixTask(), 0, 60000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_regex_threory, container, false)
        mWebView = rootView.findViewById<View>(R.id.webview) as WebView
        mSwipeRefresh = rootView.findViewById<View>(R.id.swipe_refresh) as SwipeRefreshLayout
        return rootView
    }

    override fun onCreateContextMenu(menu: ContextMenu, view: View, menuInfo: ContextMenuInfo?) {
        val hitTestResult = mWebView!!.hitTestResult
        when (hitTestResult.type) {
            HitTestResult.UNKNOWN_TYPE, HitTestResult.EDIT_TEXT_TYPE -> {}
            else -> {
                showChoiceDialog(requireActivity(), hitTestResult.extra!!)
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val GirlTask = GirlTask()
        GirlTask.execute()
        mWebView!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                mSwipeRefresh!!.isRefreshing = true
                if (progress >= 85) {
                    mSwipeRefresh!!.isRefreshing = false
                }
            }
        }
        mSwipeRefresh!!.setOnRefreshListener { mWebView!!.reload() }
        mSwipeRefresh!!.setColorSchemeColors(*swipeColor)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        var menu: Menu? = menu
        if (menu != null) {
            menu.clear()
        } else menu = MenuBuilder(context)
        menu.add(R.string.articles)
            .setIcon(R.drawable.ic_menu_regex_articles)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                showArticlesList()
                true
            }
        menu.add(R.string.refresh)
            .setIcon(R.drawable.ic_menu_regex_refresh)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                mWebView!!.reload()
                true
            }
        menu.add(R.string.clear_cache)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                if (Build.VERSION.SDK_INT >= 23) //marshmallow
                    try {
                        verifyStoragePermissions(requireActivity())
                    } catch (se: SecurityException) {
                        Toasty.warning(requireContext(), se.message!!, Toast.LENGTH_LONG, true).show()
                    }
                mWebView!!.clearCache(true)
                true
            }
        menu.add(R.string.link)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                showChoiceDialog(requireActivity(), mWebView!!.title!!)
                true
            }
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Clean WebView onDestroy")
        webViewCleaner.cancel()
        webViewCleaner.purge()
        webViews.clear()
    }

    private fun showArticlesList() {
        val items = arrayOf<CharSequence>(
            getString(R.string.st1),
            getString(R.string.st2),
            getString(R.string.st3),
            getString(R.string.st4),
            getString(R.string.st5),
            getString(R.string.st6),
            getString(R.string.st7)
        )
        val urls = arrayOf(
            "https://developer.android.com/reference/java/util/regex/Matcher.html",
            "https://developer.android.com/reference/java/util/regex/Pattern.html",
            "https://m.habrahabr.ru/company/piter/blog/300892",
            "https://m.habrahabr.ru/post/115825",
            "https://m.habrahabr.ru/post/115436",
            "http://ibm.com/developerworks/ru/library/l-regexp_1",
            "http://ibm.com/developerworks/ru/library/l-regexp_2"
        )
        MaterialDialog.Builder(context!!)
            .title(R.string.articles)
            .items(*items)
            .itemsCallback { dialog: MaterialDialog?, view: View?, i: Int, items1: CharSequence? ->
                for (j in 0..6) {
                    if (j == i) {
                        mWebView!!.loadUrl(urls[j])
                    }
                }
            }
            .show()
    }

    private inner class GirlTask : AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            mWebView!!.settings.setSupportZoom(true)
            mWebView!!.settings.builtInZoomControls = true
            mWebView!!.settings.displayZoomControls = false
            mWebView!!.settings.userAgentString = Build.MANUFACTURER + " " + Build.MODEL + "/Android " + Build.VERSION.RELEASE
            Log.i(TAG, "UserAgent : " + Build.MANUFACTURER + " " + Build.MODEL + "/Android " + Build.VERSION.RELEASE)
            //чтоб не грузить страницу каждый раз
            mWebView!!.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            mWebView!!.loadUrl("https://developer.android.com/reference/java/util/regex/Matcher.html")
        }

        override fun doInBackground(vararg params: Void?): Void? {
            Log.i(TAG, "doInBackground Success")
            return null
        }

        override fun onPostExecute(result: Void?) {
            Log.i(TAG, "onPostExecute")
            super.onPostExecute(result)
            mWebView!!.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    if (!url.contains("habrahabr.ru") && !url.contains("developer.android") && !url.contains("ibm.com/developerworks")) {
                        Log.i(TAG, "Redirect to external link : $url")
                        MaterialDialog.Builder(context!!)
                            .content("Кажется, вы хотите перейти по внешней ссылке, открыть\n$url\nв браузере?")
                            .positiveText("Ok")
                            .negativeText("Загрузить")
                            .neutralText("Отмена")
                            .onPositive { dialog: MaterialDialog?, which: DialogAction? ->
                                goLink(
                                    requireActivity(),
                                    url,
                                    "Открыть ссылку в браузере"
                                )
                            }
                            .onNegative { dialog: MaterialDialog?, which: DialogAction? -> mWebView!!.loadUrl(url) }
                            .show()
                        return true
                    }
                    return false
                }
            }
        }

    }

    fun goLink(context: Activity, link: String, info: String) {
        val uri = Uri.parse(link)
        val linkIntent = Intent(Intent.ACTION_VIEW, uri)
        //без этого флага крашится на некоторых устройствах
        linkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        Log.w(TAG, "Activity New Task ok")
        context.startActivity(Intent.createChooser(linkIntent, info))
    }

    private inner class MemoryLeakFixTask : TimerTask() {
        override fun run() {
            Log.d(TAG, "Try remove WebView : $this")
            if (webViews.size > 0) {
                Log.d(TAG, "Remove WebView : " + webViews.element().tag)
                webViews.remove()
            }
        }
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): TheoryFragment {
            val fragment = TheoryFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}