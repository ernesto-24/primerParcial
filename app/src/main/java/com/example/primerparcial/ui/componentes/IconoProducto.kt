package com.example.primerparcial.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.Pool
import androidx.compose.material.icons.outlined.SportsBasketball
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material.icons.outlined.SportsTennis
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.primerparcial.model.Calzado
import com.example.primerparcial.model.Deporte
import com.example.primerparcial.model.Producto
import com.example.primerparcial.model.Ropa
import com.example.primerparcial.ui.theme.Naranja
import com.example.primerparcial.ui.theme.NaranjaClaro

/** Miniatura cuadrada con fondo naranja claro y el ícono según el tipo de producto. */
@Composable
fun MiniaturaProducto(producto: Producto, tamano: Dp, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(tamano)
            .background(NaranjaClaro, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = iconoDe(producto),
            contentDescription = null,
            tint = Naranja,
            modifier = Modifier.size(tamano * 0.45f),
        )
    }
}

/** Calzado y Ropa tienen ícono propio; el equipamiento usa el ícono de su deporte. */
private fun iconoDe(producto: Producto): ImageVector = when (producto) {
    is Calzado -> IconoZapatilla
    is Ropa -> IconoCamiseta
    else -> when (producto.deporte) {
        Deporte.FUTBOL -> Icons.Outlined.SportsSoccer
        Deporte.BASQUET -> Icons.Outlined.SportsBasketball
        Deporte.TENIS -> Icons.Outlined.SportsTennis
        Deporte.RUNNING -> Icons.AutoMirrored.Outlined.DirectionsRun
        Deporte.NATACION -> Icons.Outlined.Pool
    }
}

private fun iconoDeTrazo(nombre: String, vararg trazos: String): ImageVector {
    val builder = ImageVector.Builder(
        name = nombre,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    )
    trazos.forEach { trazo ->
        builder.addPath(
            pathData = addPathNodes(trazo),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.8f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        )
    }
    return builder.build()
}

private val IconoZapatilla: ImageVector = iconoDeTrazo(
    "Zapatilla",
    "M3,8 L3,17 L21,17 L21,15 C21,13.3 19.8,12.4 18,12 L13.5,11 L11,8 C10,8.8 8.5,9.2 7,9 C5.5,8.8 4,8.3 3,8 Z",
    "M3,14.5 L21,14.5",
    "M11.2,10.6 L12.4,9.6",
    "M13.2,12.3 L14.4,11.2",
)

private val IconoCamiseta: ImageVector = iconoDeTrazo(
    "Camiseta",
    "M8.5,3.5 L3.5,6 L2.5,10 L6,11 L6,20.5 L18,20.5 L18,11 L21.5,10 L20.5,6 L15.5,3.5 " +
        "C15,5 13.7,6 12,6 C10.3,6 9,5 8.5,3.5 Z",
)
