package ru.svolf.convertx.data.entity

import kotlinx.serialization.SerialName

/*
 * Created by SVolf on 22.01.2023, 20:52
 * This file is a part of "SimpleSerializer" project
 */
@kotlinx.serialization.Serializable
data class Color(@SerialName("accent") val accent: String, @SerialName("hex") val hex: String)
