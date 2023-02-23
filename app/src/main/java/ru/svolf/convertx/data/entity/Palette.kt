package ru.svolf.convertx.data.entity

import kotlinx.serialization.SerialName

/*
 * Created by SVolf on 22.01.2023, 20:52
 * This file is a part of "SimpleSerializer" project
 */
@kotlinx.serialization.Serializable
data class Palette(@SerialName("name") val name: String, @SerialName("colors") val colors: Array<Color>) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Palette

		if (!colors.contentEquals(other.colors)) return false

		return true
	}

    override fun hashCode(): Int {
        return colors.contentHashCode()
    }
}
