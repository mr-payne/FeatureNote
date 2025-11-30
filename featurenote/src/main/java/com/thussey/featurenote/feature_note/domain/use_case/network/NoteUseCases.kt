package com.thussey.featurenote.feature_note.domain.use_case.network

data class NoteUseCases(
    val createUpdateUseCase: CreateUpdateNoteUseCase,
    val getNotesUseCase: GetNotesUseCase,
    val getNoteUseCase: GetNoteUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase
)
