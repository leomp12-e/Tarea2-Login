package com.example.loginapp

import androidx.room.Entity
import androidx.room.PrimaryKey

// ---------------------------------------------------------
// ENTIDAD: representa una fila de la tabla "videojuegos".
// Room genera automáticamente la tabla a partir de esta clase.
// ---------------------------------------------------------
@Entity(tableName = "videojuegos")
data class Videojuego(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val titulo: String,
    val plataforma: String,
    val genero: String,
    val anyo: Int,
    val nota: Float,
    val completado: Boolean = false
)