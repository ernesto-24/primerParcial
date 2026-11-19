package com.example.primerparcial.model

import androidx.compose.runtime.mutableStateListOf

class Carrito {
    /**
     * Lista observable por Compose. Los items son inmutables: al cambiar una cantidad
     * se reemplaza el item por una copia para que la UI detecte el cambio.
     */
    private val items = mutableStateListOf<ItemCarrito>()

    fun getItems(): List<ItemCarrito> = items

    /** Suma 1 unidad. Devuelve false si ya se alcanzó el stock del producto. */
    fun agregar(producto: Producto): Boolean {
        val indice = items.indexOfFirst { it.producto.id == producto.id }
        val cantidadActual = if (indice >= 0) items[indice].cantidad else 0
        if (!producto.hayStock(cantidadActual + 1)) return false

        if (indice >= 0) {
            items[indice] = items[indice].copy(cantidad = cantidadActual + 1)
        } else {
            items.add(ItemCarrito(producto, 1))
        }
        return true
    }

    /** Resta 1 unidad; si llega a 0, elimina el item. */
    fun quitar(producto: Producto) {
        val indice = items.indexOfFirst { it.producto.id == producto.id }
        if (indice < 0) return

        val item = items[indice]
        if (item.cantidad > 1) {
            items[indice] = item.copy(cantidad = item.cantidad - 1)
        } else {
            items.removeAt(indice)
        }
    }

    fun total(): Double = items.sumOf { it.subtotal() }

    fun estaVacio(): Boolean = items.isEmpty()

    fun vaciar() = items.clear()
}
