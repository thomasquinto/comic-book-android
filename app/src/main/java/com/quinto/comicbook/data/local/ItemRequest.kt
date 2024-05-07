package com.quinto.comicbook.data.local

import androidx.room.Entity
import com.quinto.comicbook.data.remote.OrderBy
import java.util.Date

@Entity(primaryKeys = ["paramKey", "paramExtras"])
data class ItemRequest(
    val paramKey: String,
    val paramExtras: String,
    val itemIds: List<Int>,
    val created: Date,
) {
    companion object {
        fun generateParamKey(itemType: String?, prefix: String?, id: Int?): String {
            return "$itemType-$prefix-$id"
        }

        fun generateParamExtras(startsWith: String?, orderBy: OrderBy?, offset: Int?, limit: Int?): String {
            return "$startsWith-$orderBy-$offset-$limit"
        }
    }
}