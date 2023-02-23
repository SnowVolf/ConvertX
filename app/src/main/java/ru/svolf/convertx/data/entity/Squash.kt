package ru.svolf.convertx.data.entity

import kotlinx.serialization.SerialName

/*
 * Created by SVolf on 22.01.2023, 20:58
 * This file is a part of "SimpleSerializer" project
 */
@kotlinx.serialization.Serializable
data class Squash(@SerialName("palettes") val palettes: List<Palette>)
