package com.example.primerparcial.model

import com.example.primerparcial.util.formatoBs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TiendaTest {

    private fun producto(id: Int, stock: Int, precio: Double = 100.0) =
        Calzado(id, "Producto $id", "Marca", precio, stock, Deporte.RUNNING, talla = 40)

    @Before
    fun limpiarCarrito() = Tienda.carrito.vaciar()

    @Test
    fun detalle_segunTipo() {
        assertEquals("Talla 42", Calzado(1, "", "", 1.0, 1, Deporte.RUNNING, 42).detalle())
        assertEquals("Talla M", Ropa(1, "", "", 1.0, 1, Deporte.FUTBOL, "M").detalle())
        assertEquals("0,43 kg", Equipamiento(1, "", "", 1.0, 1, Deporte.FUTBOL, 0.43).detalle())
    }

    @Test
    fun agregar_respetaElStock() {
        val carrito = Carrito()
        val p = producto(1, stock = 2)

        assertTrue(carrito.agregar(p))
        assertTrue(carrito.agregar(p))
        assertFalse(carrito.agregar(p))
        assertEquals(1, carrito.getItems().size)
        assertEquals(2, carrito.getItems()[0].cantidad)
    }

    @Test
    fun agregar_sinStockDevuelveFalse() {
        val carrito = Carrito()
        assertFalse(carrito.agregar(producto(1, stock = 0)))
        assertTrue(carrito.estaVacio())
    }

    @Test
    fun quitar_restaYEliminaAlLlegarACero() {
        val carrito = Carrito()
        val p = producto(1, stock = 5)
        carrito.agregar(p)
        carrito.agregar(p)

        carrito.quitar(p)
        assertEquals(1, carrito.getItems()[0].cantidad)
        carrito.quitar(p)
        assertTrue(carrito.estaVacio())
    }

    @Test
    fun total_sumaSubtotales() {
        val carrito = Carrito()
        val a = producto(1, stock = 5, precio = 650.0)
        val b = producto(2, stock = 5, precio = 280.0)
        carrito.agregar(a)
        carrito.agregar(b)
        carrito.agregar(b)

        assertEquals(1210.0, carrito.total(), 0.001)
        assertEquals("Bs 1.210", formatoBs(carrito.total()))
    }

    @Test
    fun productosPorDeporte_nullDevuelveTodo() {
        assertEquals(Tienda.inventario, Tienda.productosPorDeporte(null))
        assertTrue(Tienda.productosPorDeporte(Deporte.TENIS).all { it.deporte == Deporte.TENIS })
    }

    @Test
    fun confirmarCompra_carritoVacioDevuelveNull() {
        assertNull(Tienda.confirmarCompra())
    }

    @Test
    fun confirmarCompra_reduceStockCreaPedidoYVaciaCarrito() {
        val p = Tienda.inventario.first { it.stock >= 2 }
        val stockInicial = p.stock
        val pedidosIniciales = Tienda.pedidos.size
        Tienda.carrito.agregar(p)
        Tienda.carrito.agregar(p)

        val pedido = Tienda.confirmarCompra()

        assertNotNull(pedido)
        assertEquals(pedidosIniciales + 1, pedido!!.id)
        assertEquals(p.precio * 2, pedido.total, 0.001)
        assertEquals(2, pedido.items.single().cantidad) // la copia sobrevive al vaciado
        assertEquals(stockInicial - 2, p.stock)
        assertTrue(Tienda.carrito.estaVacio())
    }
}
