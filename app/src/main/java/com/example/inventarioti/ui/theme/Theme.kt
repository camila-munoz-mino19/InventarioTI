
package com.example.inventarioti.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresInventario = lightColorScheme(
    primary = MoradoBoton,
    onPrimary = Color.White,
    primaryContainer = Lavanda,
    onPrimaryContainer = TextoMorado,

    secondary = MoradoPrincipal,
    onSecondary = Color.White,
    secondaryContainer = LavandaSuave,
    onSecondaryContainer = TextoMorado,

    tertiary = MoradoSuave,
    onTertiary = Color.White,

    background = FondoClaro,
    onBackground = TextoMorado,

    surface = Color.White,
    onSurface = TextoMorado,

    outline = BordeLavanda
)

@Composable
fun InventarioTITheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColoresInventario,
        typography = Typography,
        content = content
    )
}
