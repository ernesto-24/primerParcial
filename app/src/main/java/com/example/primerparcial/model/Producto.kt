package com.example.primerparcial.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

abstract class Producto(
    val id: Int,
    val nombre: String,
    val marca: String,
    val precio: Double,
    stock: Int,
    val deporte: Deporte,
) {
    /** Observable por Compose: cuando baja tras una compra, el catálogo se redibuja solo. */
    var stock: Int by mutableIntStateOf(stock)
        private set

    /** Dato propio de cada tipo de producto ("Talla 42", "Talla M", "0,43 kg"). */
    abstract fun detalle(): String

    fun hayStock(cantidad: Int): Boolean = cantidad <= stock

    fun reducirStock(cantidad: Int) {
        require(hayStock(cantidad)) { "Stock insuficiente para $nombre" }
        stock -= cantidad
    }
}
