package ru.svolf.convertx.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/*
 * Created by SVolf on 18.02.2023, 13:16
 * This file is a part of "AnonFiles" project
 */
class PrettyPaddingItemDecoration(private val padding: Int) : RecyclerView.ItemDecoration() {

	override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
		super.getItemOffsets(outRect, view, parent, state)

		val verticalDivider = padding / 2

		with(outRect) {
			left = padding
			right = padding
			top = verticalDivider
			bottom = verticalDivider
		}

	}
}