package com.example.primerparcial.ui.carrito

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.primerparcial.model.ItemCarrito
import com.example.primerparcial.model.Pedido
import com.example.primerparcial.model.Tienda
import com.example.primerparcial.ui.componentes.MiniaturaProducto
import com.example.primerparcial.ui.theme.Borde
import com.example.primerparcial.ui.theme.Fondo
import com.example.primerparcial.ui.theme.Naranja
import com.example.primerparcial.ui.theme.PrimerParcialTheme
import com.example.primerparcial.ui.theme.TextoPrincipal
import com.example.primerparcial.ui.theme.TextoSecundario
import com.example.primerparcial.ui.theme.VerdeExito
import com.example.primerparcial.ui.theme.VerdeExitoClaro
import com.example.primerparcial.util.formatoBs
import com.example.primerparcial.util.textoArticulos

@Composable
fun CarritoScreen(onVolver: () -> Unit) {
    val context = LocalContext.current
    val carrito = Tienda.carrito
    val items = carrito.getItems()
    var pedidoConfirmado by remember { mutableStateOf<Pedido?>(null) }

    Scaffold(
        containerColor = Fondo,
        topBar = {
            EncabezadoCarrito(
                cantidadArticulos = items.sumOf { it.cantidad },
                onVolver = onVolver,
            )
        },
        bottomBar = {
            BarraTotal(
                total = carrito.total(),
                habilitado = !carrito.estaVacio(),
                onConfirmar = {
                    val pedido = Tienda.confirmarCompra()
                    if (pedido != null) {
                        pedidoConfirmado = pedido
                    } else {
                        Toast.makeText(context, "No se pudo confirmar la compra", Toast.LENGTH_SHORT).show()
                    }
                },
            )
        },
    ) { innerPadding ->
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Tu carrito está vacío",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextoSecundario,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(items, key = { it.producto.id }) { item ->
                    ItemCarritoCard(
                        item = item,
                        onQuitar = { carrito.quitar(item.producto) },
                        onAgregar = {
                            if (!carrito.agregar(item.producto)) {
                                Toast.makeText(context, "Sin stock suficiente", Toast.LENGTH_SHORT).show()
                            }
                        },
                    )
                }
            }
        }
    }

    pedidoConfirmado?.let { pedido ->
        DialogoPedidoConfirmado(
            pedido = pedido,
            onVolverAlCatalogo = {
                pedidoConfirmado = null
                onVolver()
            },
        )
    }
}

@Composable
private fun EncabezadoCarrito(cantidadArticulos: Int, onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(start = 8.dp, end = 20.dp, top = 12.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onVolver) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Volver al catálogo",
                tint = TextoPrincipal,
                modifier = Modifier.size(32.dp),
            )
        }
        Spacer(Modifier.width(4.dp))
        Text(
            text = "Mi carrito",
            style = MaterialTheme.typography.titleLarge,
            color = TextoPrincipal,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = textoArticulos(cantidadArticulos),
            style = MaterialTheme.typography.bodyLarge,
            color = TextoSecundario,
        )
    }
}

@Composable
private fun ItemCarritoCard(item: ItemCarrito, onQuitar: () -> Unit, onAgregar: () -> Unit) {
    val producto = item.producto
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Borde),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            MiniaturaProducto(producto = producto, tamano = 64.dp)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextoPrincipal,
                )
                Text(
                    text = "${producto.detalle()} · ${formatoBs(producto.precio)} c/u",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario,
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Stepper(cantidad = item.cantidad, onQuitar = onQuitar, onAgregar = onAgregar)
                    Text(
                        text = formatoBs(item.subtotal()),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal,
                    )
                }
            }
        }
    }
}

@Composable
private fun Stepper(cantidad: Int, onQuitar: () -> Unit, onAgregar: () -> Unit) {
    Row(
        modifier = Modifier.border(1.dp, Borde, RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onQuitar) {
            Icon(Icons.Filled.Remove, contentDescription = "Quitar uno", tint = TextoPrincipal)
        }
        Text(
            text = cantidad.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = TextoPrincipal,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(24.dp),
        )
        IconButton(onClick = onAgregar) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar uno", tint = TextoPrincipal)
        }
    }
}

@Composable
private fun BarraTotal(total: Double, habilitado: Boolean, onConfirmar: () -> Unit) {
    Surface(color = Color.White) {
        Column {
            HorizontalDivider(color = Borde)
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextoSecundario,
                    )
                    Text(
                        text = formatoBs(total),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal,
                        maxLines = 1,
                    )
                }
                Button(
                    onClick = onConfirmar,
                    enabled = habilitado,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Naranja,
                        contentColor = Color.White,
                        disabledContainerColor = Borde,
                        disabledContentColor = TextoSecundario,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                ) {
                    Text("Confirmar compra", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
private fun DialogoPedidoConfirmado(pedido: Pedido, onVolverAlCatalogo: () -> Unit) {
    AlertDialog(
        onDismissRequest = onVolverAlCatalogo,
        containerColor = Color.White,
        shape = RoundedCornerShape(28.dp),
        icon = {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(VerdeExitoClaro, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = VerdeExito)
            }
        },
        title = {
            Text(
                text = "Pedido #${pedido.id} confirmado",
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal,
                textAlign = TextAlign.Center,
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Total pagado: ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = TextoPrincipal)) {
                            append(formatoBs(pedido.total))
                        }
                    },
                    color = TextoSecundario,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "El stock se actualizó y tu carrito quedó vacío.",
                    color = TextoSecundario,
                    textAlign = TextAlign.Center,
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onVolverAlCatalogo,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TextoPrincipal, contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            ) {
                Text("Volver al catálogo", style = MaterialTheme.typography.titleMedium)
            }
        },
    )
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun CarritoScreenPreview() {
    PrimerParcialTheme {
        CarritoScreen(onVolver = {})
    }
}
