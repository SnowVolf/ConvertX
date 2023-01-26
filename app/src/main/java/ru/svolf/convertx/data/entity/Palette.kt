package ru.svolf.convertx.data.entity

/*
 * Created by SVolf on 22.01.2023, 20:52
 * This file is a part of "SimpleSerializer" project
 */
@kotlinx.serialization.Serializable
data class Palette(val name: String, val colors: Array<Color>) {
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
