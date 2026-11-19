package com.example.primerparcial.model

class Calzado(
    id: Int,
    nombre: String,
    marca: String,
    precio: Double,
    stock: Int,
    deporte: Deporte,
    val talla: Int,
) : Producto(id, nombre, marca, precio, stock, deporte) {

    override fun detalle(): String = "Talla $talla"
}
