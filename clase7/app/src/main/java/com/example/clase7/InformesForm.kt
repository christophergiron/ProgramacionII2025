package com.example.clase7

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.clase7.models.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

@Composable
fun InformeForm(navController: NavController){

    val auth = Firebase.auth
    val context = LocalContext.current
    var informe by remember { mutableStateOf(Informe()) }
    val activity = LocalView.current.context as Activity
    val db = Firebase.firestore

    fun saveInforme() {
        db.collection("informes")
            .add(informe)
            .addOnSuccessListener {
                Toast.makeText(context, "Informe guardado correctamente", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Nuevo Informe", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = informe.curso,
            onValueChange = { informe = informe.copy(curso = it) },
            label = { Text("Curso") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = informe.año,
            onValueChange = { informe = informe.copy(año = it) },
            label = { Text("Año") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = informe.semestre,
            onValueChange = { informe = informe.copy(semestre = it) },
            label = { Text("Semestre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = informe.comentarios,
            onValueChange = { informe = informe.copy(comentarios = it) },
            label = { Text("Comentarios") },
            modifier = Modifier.fillMaxWidth()
        )

//poner aqui los picker de archivos porfis
        OutlinedTextField(
            value = informe.archivo,
            onValueChange = { informe = informe.copy(archivo = it) },
            label = { Text("URL o nombre del archivo") },
            modifier = Modifier.fillMaxWidth()
        )

        LaunchedEffect(Unit) {
            val fechaActual = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                .format(java.util.Date())
            informe = informe.copy(fecha = fechaActual)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { saveInforme() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC9252B),
                contentColor = Color.White
            )
        ) {
            Text("Guardar Informe")
        }

        Spacer(modifier = Modifier.height(8.dp))

        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Volver")
        }
    }
}