package ru.SnowVolf.convertx.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ru.SnowVolf.convertx.R;

/**
 * Created by Snow Volf on 27.01.2017.
 */

public class Extras {
    public static String TAG = "ConvertX";

    public static void showGitDialog(Activity context){
        new MaterialDialog.Builder(context)
                .title(R.string.explore_git_src)
                .iconRes(R.drawable.ic_source_explore)
                .content(R.string.explore_git_disclaimer)
                .positiveText(R.string.git_hub)
                .negativeText(R.string.forpda)
                .neutralText(android.R.string.cancel)
                //применение goLink
                .onPositive((dialog, which) -> goLink(context, "https://github.com/SnowVolf/ConvertX/", "Исходники на GitHub"))
                .onNegative((dialog, which) -> goLink(context, "https://goo.gl/bHrymP", "Исходники на 4pda"))
                .show();
    }


    public static String getBuildName(Context context) {
        String programBuild = ""; //context.getString(R.string.app_name);
        try {
            String pkg = context.getPackageName();
            PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(pkg, PackageManager.GET_META_DATA);
            programBuild += " v." + pkgInfo.versionName/*+" "+pkgInfo.versionCode*/;
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return programBuild;
    }

    public static void showAssetDialog(Context context, String girlTitle, String assetPath){
        final StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(assetPath), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        new MaterialDialog.Builder(context)
                .title(girlTitle)
                .content(sb)
                .typeface(null, "RobotoMono-Regular.ttf")
                .positiveText(android.R.string.ok)
                .show();
    }

    public static void showAssetBottomSheet(final Activity context, String Title, String sheetAsset){
        TextView title;
        TextView content;
        /**
         * Объявляем наш layout как View
         * Далее получаем текст из StringBuilder
         * Находим в нём наш пустой TextView, и хуярим текст туды
         * На выходе скармливаем это безобразие нашему BottomSheet через setContentView()
         * PROFIT
         */
        View v = context.getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);
        final StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(sheetAsset), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert v != null;
        title = (TextView) v.findViewById(R.id.sheet_title);
        title.setText(Title);
        content = (TextView) v.findViewById(R.id.sheet_content);
        content.setText(sb);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(v);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    public static void showToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Чтобы не создавать один и тот же код много раз
     * 3 строки кода заменяем одной, потом юзаем где хотим
     */
    public static void goLink(Activity context, String link, String info){
        Uri uri = Uri.parse(link);
        Intent linkIntent = new Intent(Intent.ACTION_VIEW, uri);
        //без этого флага крашится на некоторых устройствах
        linkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.w(TAG, "Activity New Task ok");
        context.startActivity(Intent.createChooser(linkIntent, info));
    }
}
