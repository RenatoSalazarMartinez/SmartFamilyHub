package com.renato.smartfamilyhub

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
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
import androidx.compose.runtime.*

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



data class TareaFamiliar(
    val id: Int,
    val titulo: String,
    val detalle: String,
    val estaCompletada: Boolean = false
)
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(paddingContent: PaddingValues) {

    // ESTADO: Recuerda quién está seleccionado
    //var miembroSeleccionado by remember { mutableStateOf("Verónica") }

    // USUARIO LOGUEADO: Dueño de la sesión (nunca cambia al tocar avatares)
    val usuarioLogueado by remember { mutableStateOf("Verónica") }

    // MIEMBRO VISUALIZADO: Filtro superior para ver las tareas de otros
    var miembroVisualizado by remember { mutableStateOf("Verónica") }

    var mostrarDialogo by remember { mutableStateOf(false) }

    // Estados para los campos del formulario de tareas
    var nuevoTitulo by remember { mutableStateOf("") }
    var nuevaDescripcion by remember { mutableStateOf("") }
    var prioridadSeleccionada by remember { mutableStateOf("Media") } // Valor por defecto

    // DATOS DE PRUEBA: Tareas asociadas a cada miembro
    val tareasPorMiembro = remember {
        mutableStateMapOf(
            "Verónica" to mutableStateListOf(
                TareaFamiliar(1, "Preparar el desayuno", "Prioridad Alta ☀️", estaCompletada = true),
                TareaFamiliar(2, "Regar las plantas", "Por la tarde 💧"),
                TareaFamiliar(3, "Barrer y trapear la sala", "Fin de semana")
            ),
            "Wilder" to mutableStateListOf(
                TareaFamiliar(3, "Barrer y trapear la sala", "Fin de semana")
            ),
            "Enzo" to mutableStateListOf(
                TareaFamiliar(4, "Pasear al perro", "20 min de caminata 🐾")
            ),
            "Renato" to mutableStateListOf(
                TareaFamiliar(5, "Lavar los platos", "Antes de las 3:00 PM 🍳"),
                TareaFamiliar(6, "Sacar la basura", "Por la noche 🗑️")
            )
        )
    }

    // Buscamos en el mapa usando el nombre guardado en el estado
    val tareasDelMiembro = tareasPorMiembro[miembroVisualizado] ?: remember { mutableStateListOf() }

    // Lógica de la fecha actual
    val fechaActual = LocalDate.now()
    val formateador = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale("es", "ES"))
    val fechaTexto = fechaActual.format(formateador).replaceFirstChar { it.uppercase() }

    // Configuración de los miembros y sus colores
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
                        Icon(imageVector = Default.Notifications, contentDescription = "Notificaciones")
                    }
                    IconButton(onClick = { /* TODO: Perfil */ }) {
                        Icon(imageVector = Default.AccountCircle, contentDescription = "Perfil de usuario")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogo = true }, // Abre el formulario
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Default.Add, //
                    contentDescription = "Añadir tarea"
                )
            }
        }
    ) { innerPadding ->

        // Aquí agregamos el scroll vertical para que toda la pantalla sea deslizable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            // Tarjeta de bienvenida
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "¡Hola, Familia!", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = fechaTexto, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Miembros de la familia",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fila horizontal con los avatares circulares interactivos
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                miembros.forEach { (nombre, colorDeFondo) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val esElActivo = nombre == miembroVisualizado

                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(color = colorDeFondo, shape = CircleShape)
                                .clickable { miembroVisualizado = nombre } // 👆 Cambia el estado
                                .border(
                                    width = if (esElActivo) 3.dp else 0.dp, // 🖼️ Borde dinámico
                                    color = if (esElActivo) MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.Transparent,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = nombre.first().toString(),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.getOnColorFor(colorDeFondo),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = nombre,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (esElActivo) FontWeight.Bold else FontWeight.SemiBold,
                            color = if (esElActivo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Título dinámico de la sección de tareas
            Text(
                text = "Tareas de $miembroVisualizado",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Lista de tareas del miembro seleccionado
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                if (tareasDelMiembro.isEmpty()) {
                    Text(
                        text = "No hay tareas pendientes 🎉",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    tareasDelMiembro.forEach { tarea ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = tarea.estaCompletada,
                                    onCheckedChange = { estaMarcada ->
                                        // Buscamos el índice exacto de la tarea actual en la lista
                                        val indice = tareasDelMiembro.indexOf(tarea)

                                        if (indice != -1) {
                                            // Reemplazamos la tarea por una copia con el nuevo estado del checkbox
                                            tareasDelMiembro[indice] = tarea.copy(estaCompletada = estaMarcada)
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = tarea.titulo,
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = tarea.detalle,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = {
                Text(text = "Nueva tarea", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = nuevoTitulo,
                        onValueChange = { nuevoTitulo = it },
                        label = { Text("Título de la tarea") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = nuevaDescripcion,
                        onValueChange = { nuevaDescripcion = it },
                        label = { Text("Descripción (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    // ⚡ Sección de Prioridad con Chips
                    Text(
                        text = "Prioridad",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = prioridadSeleccionada == "Alta",
                            onClick = { prioridadSeleccionada = "Alta" },
                            label = { Text("Alta") }
                        )
                        FilterChip(
                            selected = prioridadSeleccionada == "Media",
                            onClick = { prioridadSeleccionada = "Media" },
                            label = { Text("Media") }
                        )
                        FilterChip(
                            selected = prioridadSeleccionada == "Baja",
                            onClick = { prioridadSeleccionada = "Baja" },
                            label = { Text("Baja") }
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (nuevoTitulo.isNotBlank()) {
                        val nuevaTarea = TareaFamiliar(
                            id = (0..1000).random(),
                            titulo = nuevoTitulo,
                            detalle = "$nuevaDescripcion • Prioridad $prioridadSeleccionada",
                            estaCompletada = false
                        )

                        // Se asigna exclusivamente al usuario logueado
                        tareasPorMiembro[usuarioLogueado]?.add(nuevaTarea)

                        nuevoTitulo = ""
                        nuevaDescripcion = ""
                        prioridadSeleccionada = "Media"
                        mostrarDialogo = false
                    }
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
        )
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