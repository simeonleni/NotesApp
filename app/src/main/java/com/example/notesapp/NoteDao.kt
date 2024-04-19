package com.example.notesapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNote(note: Notes)

    @Delete
    suspend fun deleteNote(note: Int)

    @Query("SELECT * FROM notes_table ORDER BY created ASC")
    fun getNotes(): Flow<List<Notes>>

    @Query("SELECT * FROM notes_table ORDER BY title ASC")
    fun getNotesSortedByTitle(): Flow<List<Notes>>

    @Query("SELECT * FROM notes_table ORDER BY note ASC")
    fun getNotesSortedByNote(): Flow<List<Notes>>
}
