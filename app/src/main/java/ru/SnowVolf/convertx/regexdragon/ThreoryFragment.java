package ru.SnowVolf.convertx.regexdragon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
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

import com.afollestad.materialdialogs.MaterialDialog;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.other.Extras;

public class ThreoryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private WebView mWebView;

    public ThreoryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ThreoryFragment newInstance(String param1, String param2) {
        ThreoryFragment fragment = new ThreoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_regex_threory, container, false);
        mWebView = (WebView) rootView.findViewById(R.id.webview);

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
                UrlExtentisions.showChoiceDialog(getActivity(), hitTestResult.getExtra());
            }
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        //чтоб не грузить страницу каждый раз
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.loadUrl("https://developer.android.com/reference/java/util/regex/Matcher.html");
        mWebView.setWebChromeClient(new WebChromeClient() {

            public void onReceivedTitle(WebView view, String title) {
                //getActivity().getActionBar().setSubtitle(title);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
                try {
                    getActivity().getActionBar().setTitle(description);
                    getActivity().getActionBar().setSubtitle(failingUrl);
                } catch (Exception ignore){}
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                if (!url.contains("habrahabr.ru") && !url.contains("developer.android")){
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
        menu.add("Очистить кэш")
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER)
                .setOnMenuItemClickListener(menuItem -> {
                    mWebView.clearCache(true);
                    return true;
                });
        menu.add("Ссылка")
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER)
                .setOnMenuItemClickListener(menuItem -> {
                    UrlExtentisions.showChoiceDialog(getActivity(), mWebView.getTitle());
                    return true;
                });

    }

    private void showArticlesList(){
        CharSequence[] items = new CharSequence[]{"Android Developers - Matcher", "Android Developers - Pattern", "Regex intro (HabraHabr)", "Пособие для новичков ч. I (HabraHabr)", "Пособие для новичков ч. II (HabraHabr)"};
        new MaterialDialog.Builder(getContext())
                .title(R.string.articles)
                .items(items)
                .itemsCallback((dialog, view, i, items1) -> {
                    switch (i) {
                        case 0:
                            mWebView.loadUrl("https://developer.android.com/reference/java/util/regex/Matcher.html");
                            break;
                        case 1:
                            mWebView.loadUrl("https://developer.android.com/reference/java/util/regex/Pattern.html");
                            break;
                        case 2:
                            mWebView.loadUrl("https://m.habrahabr.ru/company/piter/blog/300892");
                            break;
                        case 3:
                            mWebView.loadUrl("https://m.habrahabr.ru/post/115825");
                            break;
                        case 4:
                            mWebView.loadUrl("https://m.habrahabr.ru/post/115436");
                            break;
                    }
                })
                .show();
    }
}
