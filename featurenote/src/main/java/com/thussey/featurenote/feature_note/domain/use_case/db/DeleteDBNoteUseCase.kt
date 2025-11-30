package com.thussey.featurenote.feature_note.domain.use_case.db

import com.thussey.featurenote.feature_note.domain.repository.NoteRepository
import com.thussey.notesdb.model.Note

class DeleteDBNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}