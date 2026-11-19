package com.example.primerparcial.model

data class ItemCarrito(
    val producto: Producto,
    val cantidad: Int,
) {
    fun subtotal(): Double = producto.precio * cantidad
}
