package ru.svolf.convertx.regexdragon

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.view.ActionMode
import android.view.ContextMenu

/**
 * Created by Snow Volf on 29.04.2017, 2:53
 */

class ExtendedWebView : NestedWebView {

    constructor(ctx: Context) : super(ctx) {}

    constructor(ctx: Context, attributeSet: AttributeSet) : super(ctx, attributeSet) {}

    constructor(ctx: Context, attributeSet: AttributeSet, defStyleAtrr: Int) : super(ctx, attributeSet, defStyleAtrr) {}

    @TargetApi(19)
    fun evalJs(js: String) {
        if (Build.VERSION.SDK_INT >= 19)
            evaluateJavascript(js, null)
        else
            loadUrl("javascript:" + js)
    }

    private var actionModeListener: OnStartActionModeListener? = null

    interface OnStartActionModeListener {
        fun OnStart(actionMode: ActionMode, callback: ActionMode.Callback, type: Int)
    }

    fun setActionModeListener(actionModeListener: OnStartActionModeListener) {
        this.actionModeListener = actionModeListener
    }

    override fun startActionMode(callback: ActionMode.Callback): ActionMode {
        return myActionMode(callback, 0)
    }

    override fun startActionMode(callback: ActionMode.Callback, type: Int): ActionMode {
        return myActionMode(callback, type)
    }

    private fun myActionMode(callback: ActionMode.Callback, type: Int): ActionMode {
        val actionMode: ActionMode
        if (Build.VERSION.SDK_INT >= 23) {
            actionMode = super.startActionMode(callback, type)
        } else {
            actionMode = startActionMode(callback)
        }
        if (actionModeListener != null)
            actionModeListener!!.OnStart(actionMode, callback, type)
        return actionMode
    }

    override fun onCreateContextMenu(contextMenu: ContextMenu) {
        super.onCreateContextMenu(contextMenu)
        requestFocusNodeHref(Handler { msg ->
            val result = hitTestResult
            true
        }.obtainMessage())
    }
}
