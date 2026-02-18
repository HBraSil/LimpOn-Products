package com.example.produtosdelimpeza.core.data.local

import androidx.room.TypeConverter
import com.example.produtosdelimpeza.core.domain.model.BusinessHours
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromList(value: List<String>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }


    @TypeConverter
    fun fromBusinessHoursMap(value: Map<String, BusinessHours>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toBusinessHoursMap(value: String): Map<String, BusinessHours> {
        val type = object : TypeToken<Map<String, BusinessHours>>() {}.type
        return gson.fromJson(value, type)
    }
}