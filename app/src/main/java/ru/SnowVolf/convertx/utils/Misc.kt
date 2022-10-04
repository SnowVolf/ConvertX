package ru.SnowVolf.convertx.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.SnowVolf.convertx.R
import ru.SnowVolf.convertx.settings.DefStrings.TAG
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class Misc {
    fun showGitDialog(context: Activity) {
        MaterialDialog.Builder(context)
            .title(R.string.explore_git_src)
            .iconRes(R.drawable.ic_source_explore)
            .content(R.string.explore_git_disclaimer)
            .positiveText(R.string.git_hub)
            .negativeText(R.string.forpda)
            .neutralText(R.string.cancel) //применение goLink
            .onPositive(SingleButtonCallback { d: MaterialDialog?, w: DialogAction? ->
                goLink(
                    context,
                    "https://github.com/SnowVolf/ConvertX/",
                    "Исходники на GitHub"
                )
            })
            .onNegative(SingleButtonCallback { d: MaterialDialog?, w: DialogAction? ->
                goLink(
                    context,
                    "https://goo.gl/bHrymP",
                    "Исходники на 4pda"
                )
            })
            .show()
    }


    fun getBuildName(context: Context): String {
        var programBuild = "" //context.getString(R.string.app_name);
        try {
            val pkg = context.packageName
            val pkgInfo = context.packageManager.getPackageInfo(pkg, PackageManager.GET_META_DATA)
            programBuild += " v." + pkgInfo.versionName
        } catch (e1: PackageManager.NameNotFoundException) {
            e1.printStackTrace()
        }
        return programBuild
    }

    fun showAssetDialog(context: Context, girlTitle: String?, assetPath: String?) {
        val sb = StringBuilder()
        try {
            val br = BufferedReader(InputStreamReader(context.assets.open(assetPath!!), "UTF-8"))
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        MaterialDialog.Builder(context)
            .title(girlTitle!!)
            .content(sb)
            .typeface(null, "RobotoMono-Regular.ttf")
            .positiveText(R.string.ok)
            .show()
    }

    fun showAssetBottomSheet(context: Activity, Title: String?, sheetAsset: String?) {
        val title: TextView
        val content: TextView

        /**
         * Объявляем наш layout как View
         * Далее получаем текст из StringBuilder
         * Находим в нём наш пустой TextView, и хуярим текст туды
         * На выходе скармливаем это безобразие нашему BottomSheet через setContentView()
         * PROFIT
         */
        val v: View = context.layoutInflater.inflate(R.layout.dialog_bottom_sheet, null)
        val sb = StringBuilder()
        try {
            val br = BufferedReader(InputStreamReader(context.assets.open(sheetAsset!!), "UTF-8"))
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        assert(v != null)
        title = v.findViewById<View>(R.id.sheet_title) as TextView
        title.text = Title
        content = v.findViewById<View>(R.id.sheet_content) as TextView
        content.text = sb
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(v)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }


    fun showToast(context: Context?, text: String?) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
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
        Log.w(TAG, "Activity New Task ok")
        context.startActivity(Intent.createChooser(linkIntent, info))
    }
}