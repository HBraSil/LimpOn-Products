package com.example.produtosdelimpeza.core.map.data

import android.text.SpannableString
import com.example.produtosdelimpeza.core.domain.model.Address
import com.example.produtosdelimpeza.core.map.presentation.PlaceSuggestion

object PlaceSuggestionMapper {
    fun PlaceSuggestion.toEntity(): PlaceSuggestionEntity {
        return PlaceSuggestionEntity(
            placeId = placeId,
            primaryText = primaryText.toString(),
            secondaryText = secondaryText.toString(),
            etaMinutes = etaMinutes,
            distance = distance
        )
    }

    fun PlaceSuggestionEntity.toDomain(): PlaceSuggestion {
        return PlaceSuggestion(
            placeId = placeId,
            primaryText = SpannableString(primaryText),
            secondaryText = SpannableString(secondaryText),
            etaMinutes = etaMinutes,
            distance = distance
        )
    }

    fun PlaceSuggestionEntity.toAddress(): Address {
        return Address(
            id = placeId,
            city = primaryText,
            state = secondaryText,
            label = "",
            street = "",
            zip = "",
            distance = distance
        )
    }
}