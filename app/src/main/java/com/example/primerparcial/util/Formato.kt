package com.example.primerparcial.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/** Formato boliviano: punto para miles y coma para decimales. */
private val simbolos = DecimalFormatSymbols(Locale.ROOT).apply {
    groupingSeparator = '.'
    decimalSeparator = ','
}

fun formatearNumero(valor: Double, patron: String): String =
    DecimalFormat(patron, simbolos).format(valor)

/** Ej.: 1210.0 -> "Bs 1.210" */
fun formatoBs(monto: Double): String = "Bs " + formatearNumero(monto, "#,##0.##")

/** Ej.: 1 -> "1 artículo", 3 -> "3 artículos" */
fun textoArticulos(cantidad: Int): String =
    if (cantidad == 1) "1 artículo" else "$cantidad artículos"
