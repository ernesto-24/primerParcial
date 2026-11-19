package com.example.primerparcial

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.primerparcial.ui.carrito.CarritoScreen
import com.example.primerparcial.ui.catalogo.CatalogoScreen
import com.example.primerparcial.ui.theme.PrimerParcialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // El diseño es claro: íconos oscuros en las barras del sistema aunque el teléfono esté en modo oscuro.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        )
        setContent {
            PrimerParcialTheme {
                TiendaApp()
            }
        }
    }
}

private object Rutas {
    const val CATALOGO = "catalogo"
    const val CARRITO = "carrito"
}

@Composable
fun TiendaApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.CATALOGO) {
        composable(Rutas.CATALOGO) {
            CatalogoScreen(onVerCarrito = { navController.navigate(Rutas.CARRITO) })
        }
        composable(Rutas.CARRITO) {
            // Volver hasta el catálogo (y no más allá) aunque se toque dos veces seguidas.
            CarritoScreen(onVolver = { navController.popBackStack(Rutas.CATALOGO, inclusive = false) })
        }
    }
}
