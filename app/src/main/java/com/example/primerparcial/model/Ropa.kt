package com.example.primerparcial.model

class Ropa(
    id: Int,
    nombre: String,
    marca: String,
    precio: Double,
    stock: Int,
    deporte: Deporte,
    val talla: String,
) : Producto(id, nombre, marca, precio, stock, deporte) {

    override fun detalle(): String = "Talla $talla"
}
