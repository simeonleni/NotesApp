package com.example.notesapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModel(
    private val dao: NoteDao
) : ViewModel(), ViewModelProvider.Factory {
    private val _sorted = MutableStateFlow(Sorted.CREATED)
    private val _state = MutableStateFlow(NoteState())
    private val _notes = _sorted.flatMapConcat { sorted ->
        when (sorted) {
            Sorted.CREATED -> dao.getNotes()
            Sorted.TITLE -> dao.getNotesSortedByTitle()
            Sorted.NOTE -> dao.getNotesSortedByNote()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _sorted, _notes) { state, sorted, notes ->
        state.copy(
            notes = notes,
            sorted = sorted
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.noteId)
                }
            }
            NoteEvent.SaveNote -> {
                val title = state.value.title
                val note = state.value.note

                if (title.isBlank() || note.isBlank()) {
                    return
                }
                val created = LocalDateTime.now().toString()
                val newNote = Notes(
                    title = title,
                    note = note,
                    created = created
                )
                viewModelScope.launch {
                    dao.upsertNote(newNote)
                }
                _state.update {
                    it.copy(
                        isAddingNote = false,
                        title = "",
                        note = "",
                        created = ""
                    )
                }
            }
            NoteEvent.HideDialog -> {
                _state.update { it.copy(isAddingNote = false) }
            }
            is NoteEvent.SetCreated -> {
                _state.update { it.copy(created = event.created.toString()) }
            }
            is NoteEvent.SetNote -> {
                _state.update { it.copy(note = event.note) }
            }
            is NoteEvent.SetTitle -> {
                _state.update { it.copy(title = event.title) }
            }
            NoteEvent.ShowDialog -> {
                _state.update { it.copy(isAddingNote = true) }
            }
            is NoteEvent.SortNotes -> {
                _sorted.value = event.sorted
            }
            else -> {}
        }
    }
}
