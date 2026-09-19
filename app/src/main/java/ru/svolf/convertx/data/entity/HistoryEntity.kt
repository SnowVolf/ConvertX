package ru.svolf.convertx.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** Storage shape deliberately keeps the legacy table and column names. */
@Entity(tableName = "History")
data class HistoryEntity(
    @PrimaryKey val id: Long,
    val decoder: Int?,
    val input: String?,
    val output: String?,
    @ColumnInfo(name = "spinner_position") val spinnerPosition: Int?
)
