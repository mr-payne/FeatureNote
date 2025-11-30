package com.thussey.noteapp.feature_note.presentation.notes.model

import com.thussey.noteapp.feature_note.domain.model.NoteOrder
import com.thussey.notesdb.model.Note

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder): NotesEvent()
    data class DeleteNote(val note: Note): NotesEvent()
    object RestoreNote: NotesEvent()
    object ToggleOrderSection: NotesEvent()
    object RefreshNotes: NotesEvent()

}