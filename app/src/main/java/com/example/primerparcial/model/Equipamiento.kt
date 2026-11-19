package com.example.primerparcial.model

import com.example.primerparcial.util.formatearNumero

class Equipamiento(
    id: Int,
    nombre: String,
    marca: String,
    precio: Double,
    stock: Int,
    deporte: Deporte,
    val pesoKg: Double,
) : Producto(id, nombre, marca, precio, stock, deporte) {

    override fun detalle(): String = formatearNumero(pesoKg, "0.00") + " kg"
}
