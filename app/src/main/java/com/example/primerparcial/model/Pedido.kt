package com.example.primerparcial.model

import java.time.LocalDate

class Pedido(
    val id: Int,
    val items: List<ItemCarrito>,
    val total: Double,
    val fecha: LocalDate,
)
