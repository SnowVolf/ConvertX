package ru.svolf.convertx.utils

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.os.Build
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import ru.svolf.convertx.R

/**
 * This file is part of Toasty.

 * Toasty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Toasty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Toasty.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */

class ToastyUtils {
    companion object {

        fun tint9PatchDrawableFrame(context: Context, @ColorInt tintColor: Int): Drawable {
            val toastDrawable = getDrawable(context, R.drawable.custom_toast_frame) as NinePatchDrawable
            toastDrawable.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
            return toastDrawable
        }

        fun setBackground(view: View, drawable: Drawable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                view.background = drawable
            else
                view.setBackgroundDrawable(drawable)
        }

        fun getDrawable(context: Context, @DrawableRes id: Int): Drawable? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                return context.getDrawable(id)
            else
                return context.resources.getDrawable(id)
        }
    }
}

