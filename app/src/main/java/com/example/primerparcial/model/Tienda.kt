package com.example.primerparcial.model

import java.time.LocalDate

/** Singleton: ambas pantallas comparten el mismo inventario y carrito. */
object Tienda {

    val inventario: List<Producto> = listOf(
        Calzado(1, "Zapatillas Running Pro", "Kallpa", 650.0, 8, Deporte.RUNNING, talla = 42),
        Ropa(2, "Camiseta Selección 2026", "Andina", 280.0, 15, Deporte.FUTBOL, talla = "M"),
        Equipamiento(3, "Balón de Fútbol N.º 5", "Illimani", 220.0, 3, Deporte.FUTBOL, pesoKg = 0.43),
        Equipamiento(4, "Raqueta de Tenis Control", "Kallpa", 890.0, 0, Deporte.TENIS, pesoKg = 0.30),
        Ropa(5, "Short de Básquet", "Andina", 190.0, 10, Deporte.BASQUET, talla = "L"),
        Calzado(6, "Zapatillas de Básquet Salto", "Illimani", 720.0, 5, Deporte.BASQUET, talla = 41),
        Equipamiento(7, "Balón de Básquet N.º 7", "Illimani", 250.0, 6, Deporte.BASQUET, pesoKg = 0.62),
        Calzado(8, "Zapatillas de Tenis Arcilla", "Kallpa", 580.0, 4, Deporte.TENIS, talla = 40),
        Ropa(9, "Traje de Baño Competición", "Andina", 160.0, 7, Deporte.NATACION, talla = "S"),
        Equipamiento(10, "Gafas de Natación Pro", "Illimani", 95.0, 12, Deporte.NATACION, pesoKg = 0.05),
    )

    val carrito = Carrito()

    val pedidos: MutableList<Pedido> = mutableListOf()

    /** Con null devuelve todo el inventario. */
    fun productosPorDeporte(deporte: Deporte?): List<Producto> =
        if (deporte == null) inventario else inventario.filter { it.deporte == deporte }

    /** Devuelve el pedido creado, o null si el carrito está vacío o falta stock. */
    fun confirmarCompra(): Pedido? {
        val items = carrito.getItems()
        if (items.isEmpty() || items.any { !it.producto.hayStock(it.cantidad) }) return null

        items.forEach { it.producto.reducirStock(it.cantidad) }

        val pedido = Pedido(
            id = pedidos.size + 1,
            items = items.toList(), // copia: el carrito se vacía a continuación
            total = carrito.total(),
            fecha = LocalDate.now(),
        )
        pedidos.add(pedido)
        carrito.vaciar()
        return pedido
    }
}
