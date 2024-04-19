package com.example.notesapp

import com.example.notesapp.Sorted
import java.time.LocalDateTime

sealed interface NoteEvent {
    object SaveNote : NoteEvent
    object ShowDialog : NoteEvent
    object HideDialog : NoteEvent

    data class SetTitle(val title: String) : NoteEvent
    data class SetNote(val note: String) : NoteEvent
    data class SetCreated(val created: LocalDateTime) : NoteEvent
    data class SortNotes(val sorted: Sorted) : NoteEvent // Assuming Sorted is an enum or similar
    data class DeleteNote(val noteId: Int) : NoteEvent // Changed deleteNote: Notes to noteId: Long
}
