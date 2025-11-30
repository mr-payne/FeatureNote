package com.thussey.noteapp.feature_note.presentation.notes.model

import com.thussey.noteapp.feature_note.domain.model.NoteOrder
import com.thussey.noteapp.feature_note.domain.model.OrderType
import com.thussey.notesdb.model.Note

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
