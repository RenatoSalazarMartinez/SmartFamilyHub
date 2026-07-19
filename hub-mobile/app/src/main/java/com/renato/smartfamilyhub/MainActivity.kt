package com.renato.smartfamilyhub

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*;
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.renato.smartfamilyhub.ui.theme.SmartFamilyHubTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartFamilyHubTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(paddingContent = innerPadding)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartFamilyHubTheme {
        Greeting("Android")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(paddingContent: PaddingValues) {

    val fechaActual = LocalDate.now()
    val formateador = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale("es", "ES"))
    val fechaTexto = fechaActual.format(formateador).replaceFirstChar { it.uppercase() }

    val miembros = listOf(
        Pair("Verónica", MaterialTheme.colorScheme.primary),
        Pair("Wilder", MaterialTheme.colorScheme.secondary),
        Pair("Enzo", MaterialTheme.colorScheme.tertiary),
        Pair("Renato", MaterialTheme.colorScheme.error)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SmartFamilyHub",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Notificaciones */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones"
                        )
                    }
                    IconButton(onClick = { /* TODO: Perfil */ }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Perfil de usuario"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize() // Ocupa toda la pantalla disponible
                .padding(innerPadding) // Usamos innerPadding para que empiece debajo de la TopAppBar
        ) {

            // Aquí adentro va nuestra tarjeta de bienvenida
            Card(
                modifier = Modifier
                    .fillMaxWidth() // Ocupa todo el ancho
                    .padding(16.dp) // Deja un margen alrededor de la tarjeta
            ) {
                // Columna interna de la tarjeta para poner un texto bajo el otro
                Column(
                    modifier = Modifier.padding(16.dp) // Espacio libre por dentro de la tarjeta
                ) {
                    Text(
                        text = "¡Hola, Familia!",
                        style = MaterialTheme.typography.titleLarge
                    )

                    // El espaciador invisible que aprendimos a usar
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = fechaTexto,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            //Separador antes de la sección de miembros
            Spacer(modifier = Modifier.height(16.dp))

            // Título de la sección
            Text(
                text = "Miembros de la familia",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Fila horizontal con los avatares circulares
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp) // Espacio uniforme entre miembros
            ) {
                miembros.forEach { (nombre, colorDeFondo) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Circulo del Avatar
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(color = colorDeFondo, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = nombre.first().toString(),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.getOnColorFor(colorDeFondo), // Elige texto blanco o negro automáticamente según el contraste
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Texto del Nombre
                        Text(
                            text = nombre,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

        }
    }
}

//Función de extensión de apoyo para garantizar el contraste de la
// inicial según el color del fondo
@Composable
fun ColorScheme.getOnColorFor(color: androidx.compose.ui.graphics.Color): androidx.compose.ui.graphics.Color {
    return when (color) {
        this.primary -> this.onPrimary
        this.secondary -> this.onSecondary
        this.tertiary -> this.onTertiary
        else -> this.onError
    }
}