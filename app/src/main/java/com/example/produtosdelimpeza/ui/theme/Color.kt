package com.example.produtosdelimpeza.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

//Light theme colors
val LightBluishGray = Color(0xFFE1E8E7)
val LightWhite = Color(0xFF000000)
val LightBlue = Color(0xFF35808D)
val LightDarkBlue = Color(0xFF021C3B)
val LightSmoothBluish = Color(0xFF12445B)
val LightGreenCircle = Color(0xFF1E942A)
val LightBrown = Color(0xFF683904)


//Dark theme colors


// General colors
val RedCircle = Color(0xFFB00020)
val BluishGreen = Color(0xFF00693C)

val GradientSignupMainText = Brush.radialGradient(
    colors = listOf(
        Color(0xFF106C9D), // Azul claro no centro
        Color(0xFF6FAFA0), // Verde claro intermediário
        Color(0x6F109F93)  // Verde escuro nas bordas
    ),
    center = Offset.Zero, // Pode ajustar depois
    radius = 800f
)