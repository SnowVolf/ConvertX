package ru.svolf.convertx.utils

import android.content.res.Resources.getSystem

/*
 * Created by SVolf on 18.02.2023, 13:51
 * This file is a part of "AnonFiles" project
 */
val Int.dpi: Int get() = (this / getSystem().displayMetrics.density).toInt()