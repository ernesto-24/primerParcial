package com.example.primerparcial.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** El diseño es solo claro, así que no se usan colores dinámicos ni tema oscuro. */
private val EsquemaTienda = lightColorScheme(
    primary = Naranja,
    onPrimary = Color.White,
    background = Fondo,
    onBackground = TextoPrincipal,
    surface = Superficie,
    onSurface = TextoPrincipal,
    onSurfaceVariant = TextoSecundario,
    outline = Borde,
    outlineVariant = Borde,
    error = RojoSinStock,
)

@Composable
fun PrimerParcialTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaTienda,
        typography = Typography,
        content = content,
    )
}
