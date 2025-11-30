package com.thussey.featurenote.feature_note.domain.use_case.db

data class NoteDBUseCases(
    val getNotes: GetDBNotesUseCase,
    val deleteNote: DeleteDBNoteUseCase,
    val addNote: AddDBNoteUseCase,
    val getNote: GetDBNoteUseCase
)
