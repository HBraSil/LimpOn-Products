package com.example.produtosdelimpeza.core.map.data

import android.text.SpannableString
import com.example.produtosdelimpeza.core.map.presentation.PlaceSuggestion

object PlaceSuggestionMapper {

    fun PlaceSuggestionEntity.toDomain(): PlaceSuggestion {
        return PlaceSuggestion(
            placeId = placeId,
            primaryText = SpannableString(primaryText),
            secondaryText = SpannableString(secondaryText),
        )
    }
}