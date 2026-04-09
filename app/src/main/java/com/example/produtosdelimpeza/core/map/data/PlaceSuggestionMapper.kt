package com.example.produtosdelimpeza.core.map.data

import android.text.SpannableString
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
}