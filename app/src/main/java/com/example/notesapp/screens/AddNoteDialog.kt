package com.example.notesapp.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notesapp.NoteEvent
import com.example.notesapp.NoteState


@ExperimentalMaterial3Api
@Composable
fun AddNoteDialog(
    state: NoteState,
    onEvent: (NoteEvent)-> Unit,
    modifier: Modifier = Modifier){
    AlertDialog(onDismissRequest = {
        onEvent(NoteEvent.HideDialog)
    }) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = state.title,
                onValueChange = {
                    onEvent(NoteEvent.SetTitle(it))
                },
                placeholder = {
                    Text(text = "Note Title")
                }
            )
            TextField(
                value = state.note,
                onValueChange = {
                    onEvent(NoteEvent.SetNote(it))
                },
                placeholder = {
                    Text(text = "Take Note")
                }
            )
            Box(modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
                ){
                Button(onClick = {onEvent(NoteEvent.SaveNote)}) {
                    Text(text = "Save Note")
                }
            }
        }
    }
}