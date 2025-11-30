package com.thussey.noteapp.feature_note.presentation.add_edit_note.model

data class NoteTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)
