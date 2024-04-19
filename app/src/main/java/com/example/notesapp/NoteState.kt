package com.example.notesapp

data class NoteState(
    val notes: List<Notes> = emptyList(),
    val title: String = "",
    val note: String = "",
    val created: String? = null,
    val sorted: Sorted = Sorted.CREATED,
    val isAddingNote: Boolean = false
)
