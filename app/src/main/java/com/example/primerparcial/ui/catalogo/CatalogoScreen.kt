package com.example.primerparcial.ui.catalogo

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.primerparcial.model.Deporte
import com.example.primerparcial.model.Producto
import com.example.primerparcial.model.Tienda
import com.example.primerparcial.ui.componentes.MiniaturaProducto
import com.example.primerparcial.ui.theme.Borde
import com.example.primerparcial.ui.theme.Fondo
import com.example.primerparcial.ui.theme.Naranja
import com.example.primerparcial.ui.theme.PrimerParcialTheme
import com.example.primerparcial.ui.theme.RojoSinStock
import com.example.primerparcial.ui.theme.TextoPrincipal
import com.example.primerparcial.ui.theme.TextoSecundario
import com.example.primerparcial.util.formatoBs
import com.example.primerparcial.util.textoArticulos

@Composable
fun CatalogoScreen(onVerCarrito: () -> Unit) {
    val context = LocalContext.current
    var deporteSeleccionado by rememberSaveable { mutableStateOf<Deporte?>(null) }

    // Leer los items (lista observable) hace que la barra inferior se actualice al agregar.
    val items = Tienda.carrito.getItems()
    val cantidadArticulos = items.sumOf { it.cantidad }

    Scaffold(
        containerColor = Fondo,
        topBar = { EncabezadoCatalogo() },
        bottomBar = {
            BarraVerCarrito(
                cantidadArticulos = cantidadArticulos,
                total = Tienda.carrito.total(),
                onClick = onVerCarrito,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            FiltrosDeporte(
                seleccionado = deporteSeleccionado,
                onSeleccionar = { deporteSeleccionado = it },
            )
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = Tienda.productosPorDeporte(deporteSeleccionado),
                    key = { it.id },
                ) { producto ->
                    ProductoCard(
                        producto = producto,
                        onAgregar = {
                            if (!Tienda.carrito.agregar(producto)) {
                                Toast.makeText(context, "Sin stock suficiente", Toast.LENGTH_SHORT).show()
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun EncabezadoCatalogo() {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 16.dp),
    ) {
        Text(
            text = "Tienda Deportiva",
            style = MaterialTheme.typography.titleLarge,
            color = TextoPrincipal,
        )
        Text(
            text = "Equípate para tu deporte",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario,
        )
    }
}

@Composable
private fun FiltrosDeporte(seleccionado: Deporte?, onSeleccionar: (Deporte?) -> Unit) {
    // null representa "Todos"
    val opciones: List<Deporte?> = listOf(null) + Deporte.entries

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(opciones) { deporte ->
            val activo = deporte == seleccionado
            FilterChip(
                selected = activo,
                onClick = { onSeleccionar(deporte) },
                label = {
                    Text(
                        text = deporte?.etiqueta ?: "Todos",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 10.dp),
                    )
                },
                shape = CircleShape,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.White,
                    labelColor = TextoPrincipal,
                    selectedContainerColor = TextoPrincipal,
                    selectedLabelColor = Color.White,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = activo,
                    borderColor = Borde,
                    selectedBorderColor = TextoPrincipal,
                ),
            )
        }
    }
}

@Composable
private fun ProductoCard(producto: Producto, onAgregar: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Borde),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MiniaturaProducto(producto = producto, tamano = 72.dp)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextoPrincipal,
                )
                Text(
                    text = "${producto.marca} · ${producto.detalle()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario,
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatoBs(producto.precio),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal,
                    )
                    Spacer(Modifier.width(8.dp))
                    val sinStock = producto.stock == 0
                    Text(
                        text = if (sinStock) "Sin stock" else "${producto.stock} en stock",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (sinStock) RojoSinStock else TextoSecundario,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            val agotado = producto.stock == 0
            Button(
                onClick = onAgregar,
                enabled = !agotado,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TextoPrincipal,
                    contentColor = Color.White,
                    disabledContainerColor = Borde,
                    disabledContentColor = TextoSecundario,
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Text(if (agotado) "Agotado" else "Agregar")
            }
        }
    }
}

@Composable
private fun BarraVerCarrito(cantidadArticulos: Int, total: Double, onClick: () -> Unit) {
    Surface(color = Color.White) {
        Column {
            HorizontalDivider(color = Borde)
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Naranja, contentColor = Color.White),
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.ShoppingCart, contentDescription = null)
                        Spacer(Modifier.width(10.dp))
                        Text("Ver carrito", style = MaterialTheme.typography.titleMedium, maxLines = 1)
                    }
                    Text(
                        text = "${textoArticulos(cantidadArticulos)} · ${formatoBs(total)}",
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun CatalogoScreenPreview() {
    PrimerParcialTheme {
        CatalogoScreen(onVerCarrito = {})
    }
}
