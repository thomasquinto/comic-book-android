package com.quinto.comicbook.data.local

import androidx.room.Entity
import com.quinto.comicbook.data.remote.OrderBy
import java.util.Date

@Entity(primaryKeys = ["paramKey", "offsetParam", "limitParam"])
data class ItemRequest(
    val paramKey: String,
    val offsetParam: Int,
    val limitParam: Int,
    val itemIds: List<Int>,
    val created: Date,
) {
    companion object {
        fun generateParamKey(itemType: String?, prefix: String?, id: Int?, startsWith: String?, orderBy: OrderBy?): String {
            return "$itemType-$prefix-$id-$startsWith-$orderBy"
        }
    }
}