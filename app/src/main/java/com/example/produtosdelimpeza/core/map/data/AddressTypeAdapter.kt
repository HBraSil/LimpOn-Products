package com.example.produtosdelimpeza.core.map.data

import com.example.produtosdelimpeza.core.domain.model.AddressType
import com.google.gson.*
import java.lang.reflect.Type

class AddressTypeAdapter : JsonSerializer<AddressType?>, JsonDeserializer<AddressType?> {
    override fun serialize(src: AddressType?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        if (src == null) return JsonNull.INSTANCE
        val obj = JsonObject()
        when (src) {
            is AddressType.Home -> obj.addProperty("type", "home")
            is AddressType.Work -> obj.addProperty("type", "work")
            is AddressType.Other -> {
                obj.addProperty("type", "other")
                obj.addProperty("customLabel", src.customLabel)
            }
        }
        return obj
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): AddressType? {
        if (json.isJsonNull) return null

        val obj = json.asJsonObject
        val type = obj.get("type")?.asString ?: return null

        return when (type) {
            "home" -> AddressType.Home
            "work" -> AddressType.Work
            "other" -> AddressType.Other(obj.get("customLabel")?.asString ?: "")
            else -> null
        }
    }
}
