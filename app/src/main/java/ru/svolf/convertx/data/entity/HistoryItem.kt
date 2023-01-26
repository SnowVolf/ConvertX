package ru.svolf.convertx.data.entity

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.svolf.convertx.annotations.Decoder

/*
 * Created by SVolf on 26.01.2023, 13:42
 * This file is a part of "ConvertX" project
 */
@Keep
@Entity(tableName = "History")
class HistoryItem {
    /**
     * id элемента в таблице. В качестве типичного значения будет использоваться
     * <pre>
     *     java.lang.System.currentTimeMillis()
     **/
    @PrimaryKey
    var id: Long? = null

    /**
     * Тип декодера который нужно открыть
     **/
    @Decoder
    var decoder: Int? = null

    /**
     * Поле ввода
     **/
    var input: String? = null

    /**
     * Поле вывода
     **/
    var output: String? = null

    /**
     * Тип декодирующего алгоритма, определяет позицию spinner-a
     **/
    @ColumnInfo(name = "spinner_position")
    var spinnerPosition: Int? = null
}