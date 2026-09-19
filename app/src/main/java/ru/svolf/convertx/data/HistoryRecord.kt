package ru.svolf.convertx.data

import ru.svolf.convertx.data.entity.HistoryEntity

data class HistoryRecord(
    val id: Long,
    val decoder: Int,
    val input: String,
    val output: String,
    val spinnerPosition: Int
)

fun HistoryEntity.toRecord() = HistoryRecord(
    id = id,
    decoder = decoder ?: -1,
    input = input.orEmpty(),
    output = output.orEmpty(),
    spinnerPosition = spinnerPosition ?: 0
)
