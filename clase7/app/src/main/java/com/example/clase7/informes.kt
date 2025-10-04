package com.example.clase7

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.clase7.USERS_COLLECTION
import com.example.clase7.models.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

const val INFORMES_COLLECTION = "informes"

suspend fun getInformes(db: FirebaseFirestore): List<Informe> {
    val snapshot = db.collection(INFORMES_COLLECTION)
        .get()
        .await()

    return snapshot.documents.mapNotNull { doc ->
        doc.toObject<Informe>()?.copy(id = doc.id)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformesScreen(navController: NavController) {
    val context = LocalContext.current
    val db = Firebase.firestore
    var informes by remember { mutableStateOf(emptyList<Informe>()) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        informes = getInformes(db)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Informes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Salir"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(8.dp))
            Text("Listado de Informes")

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn {
                    for (informe in informes) {
                        item(informe.id) {
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(context.getString(R.string.screen_Informes))
                                    },
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Curso: ${informe.curso}")
                                    Text("Año: ${informe.año}")
                                    Text("Semestre: ${informe.semestre}")
                                    Text("Fecha: ${informe.fecha}")
                                    Text("Comentarios: ${informe.comentarios}")
                                    if (informe.archivo.isNotEmpty()) {
                                        Text("Archivo adjunto: ${informe.archivo}", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                IconButton(onClick = {
                    navController.navigate(context.getString(R.string.screen_informes_form))
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Nuevo Informe",
                        modifier = Modifier.size(50.dp),
                    )
                }
            }
        }
    }
}