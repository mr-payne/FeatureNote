package com.thussey.featurenote.feature_note.domain.use_case.db

import com.thussey.featurenote.feature_note.domain.repository.NoteRepository
import com.thussey.notesdb.model.Note

class GetDBNoteUseCase(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: String): Note? {
        return repository.getNoteById(id)
    }
}