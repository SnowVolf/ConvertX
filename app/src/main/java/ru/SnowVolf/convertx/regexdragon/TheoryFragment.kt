package ru.SnowVolf.convertx.regexdragon;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.other.Extras;
import ru.SnowVolf.convertx.settings.DefStrings;
import ru.SnowVolf.convertx.ui.Toasty;
import ru.SnowVolf.convertx.utils.RuntimePermission;

public class TheoryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private WebView mWebView;
    private Queue<ExtendedWebView> webViews = new LinkedList<>();
    private Timer webViewCleaner = new Timer();
    private View rootView;
    private SwipeRefreshLayout mSwipeRefresh;
    private int[] swipeColor = {R.color.blue500, R.color.green500, R.color.red500};

    public TheoryFragment() {
        webViewCleaner.schedule(new MemoryLeakFixTask(), 0, 60000);
    }

    // TODO: Rename and change types and number of parameters
    public static TheoryFragment newInstance(String param1, String param2) {
        TheoryFragment fragment = new TheoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_regex_threory, container, false);
        mWebView = (WebView) rootView.findViewById(R.id.webview);
        mSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        final WebView.HitTestResult hitTestResult = mWebView.getHitTestResult();
        switch (hitTestResult.getType()) {
            case WebView.HitTestResult.UNKNOWN_TYPE:
            case WebView.HitTestResult.EDIT_TEXT_TYPE:
                break;
            default: {
                UrlExtensions.INSTANCE.showChoiceDialog(getActivity(), hitTestResult.getExtra());
            }
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GirlTask GirlTask = new GirlTask();
        GirlTask.execute();
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress){
                mSwipeRefresh.setRefreshing(true);
                if (progress >= 85){
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        });
        mSwipeRefresh.setOnRefreshListener(() -> mWebView.reload());
        mSwipeRefresh.setColorSchemeColors(swipeColor);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null) {
            menu.clear();
        } else
            menu = new MenuBuilder(getContext());
        menu.add(R.string.articles)
                .setIcon(R.drawable.ic_menu_regex_articles)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setOnMenuItemClickListener(menuItem -> {
                   showArticlesList();
                    return true;
                });
        menu.add(R.string.refresh)
                .setIcon(R.drawable.ic_menu_regex_refresh)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setOnMenuItemClickListener(menuItem -> {
                    mWebView.reload();
                    return true;
                });
        menu.add(R.string.clear_cache)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER)
                .setOnMenuItemClickListener(menuItem -> {
                    if (Build.VERSION.SDK_INT >= 23)//marshmallow
                        try {
                            RuntimePermission.INSTANCE.verifyStoragePermissions(getActivity());
                        } catch (SecurityException se) {
                            Toasty.warning(getContext(), se.getMessage(), Toast.LENGTH_LONG, true).show();
                        }
                    mWebView.clearCache(true);
                    return true;
                });
        menu.add(R.string.link)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER)
                .setOnMenuItemClickListener(menuItem -> {
                    UrlExtensions.INSTANCE.showChoiceDialog(getActivity(), mWebView.getTitle());
                    return true;
                });

    }

    @Override
    public void onDestroyView(){
        Log.d(DefStrings.INSTANCE.getTAG(), "onDestroyView");
        super.onDestroyView();
        rootView = null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(DefStrings.INSTANCE.getTAG(), "Clean WebView onDestroy");
        webViewCleaner.cancel();
        webViewCleaner.purge();
        webViews.clear();
    }

    private void showArticlesList(){
        CharSequence[] items = new CharSequence[]{getString(R.string.st1), getString(R.string.st2), getString(R.string.st3), getString(R.string.st4), getString(R.string.st5), getString(R.string.st6), getString(R.string.st7)};
        String[] urls = {
                "https://developer.android.com/reference/java/util/regex/Matcher.html",
                "https://developer.android.com/reference/java/util/regex/Pattern.html",
                "https://m.habrahabr.ru/company/piter/blog/300892",
                "https://m.habrahabr.ru/post/115825",
                "https://m.habrahabr.ru/post/115436",
                "http://ibm.com/developerworks/ru/library/l-regexp_1",
                "http://ibm.com/developerworks/ru/library/l-regexp_2"
        };
        new MaterialDialog.Builder(getContext())
                .title(R.string.articles)
                .items(items)
                .itemsCallback((dialog, view, i, items1) -> {
                    for (int j = 0; j < 7; j++) {
                        if (j == i){
                            mWebView.loadUrl(urls[j]);
                        }
                    }
                })
                .show();
    }

    private class GirlTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mWebView.getSettings().setAppCacheEnabled(true);
            mWebView.getSettings().setSupportZoom(true);
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setDisplayZoomControls(false);

            mWebView.getSettings().setUserAgentString(Build.MANUFACTURER + " " + Build.MODEL + "/Android " + Build.VERSION.RELEASE);
            Log.i(DefStrings.INSTANCE.getTAG(), "UserAgent : " + Build.MANUFACTURER + " " + Build.MODEL + "/Android " + Build.VERSION.RELEASE);
            //чтоб не грузить страницу каждый раз
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            mWebView.loadUrl("https://developer.android.com/reference/java/util/regex/Matcher.html");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(DefStrings.INSTANCE.getTAG(), "doInBackground Success");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(DefStrings.INSTANCE.getTAG(), "onPostExecute");
            super.onPostExecute(result);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url){
                    if (!url.contains("habrahabr.ru") && !url.contains("developer.android") && !url.contains("ibm.com/developerworks")){
                        Log.i(DefStrings.INSTANCE.getTAG(), "Redirect to external link : " + url);
                        new MaterialDialog.Builder(getContext())
                                .content("Кажется, вы хотите перейти по внешней ссылке, открыть\n" + url + "\nв браузере?")
                                .positiveText("Ok")
                                .negativeText("Загрузить")
                                .neutralText("Отмена")
                                .onPositive((dialog, which) -> Extras.goLink(getActivity(), url, "Открыть ссылку в браузере"))
                                .onNegative((dialog, which) -> mWebView.loadUrl(url))
                                .show();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    public Queue<ExtendedWebView> getWebViews(){
        return webViews;
    }

    private class MemoryLeakFixTask extends TimerTask{
        public void run(){
            Log.d(DefStrings.INSTANCE.getTAG(), "Try remove WebView : " + this);
            if (webViews.size() > 0){
                Log.d(DefStrings.INSTANCE.getTAG(), "Remove WebView : " + webViews.element().getTag());
                webViews.remove();
            }
        }
    }

}
