package com.thussey.featurenote.feature_note.domain.use_case.db


import com.thussey.featurenote.feature_note.domain.repository.NoteRepository
import com.thussey.notesdb.model.InvalidNoteException
import com.thussey.notesdb.model.Note

class AddDBNoteUseCase(
    private val repository: NoteRepository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("The title of the note can't be empty.")
        }

        if (note.content.isBlank()) {
            throw InvalidNoteException("The content of the note can't be empty.")
        }
        repository.insertNote(note)
    }
}