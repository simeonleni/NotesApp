package com.example.notesapp


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notes(
    val title: String,
    val note: String,
    val created: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
