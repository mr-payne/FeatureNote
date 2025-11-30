package com.thussey.featurenote.feature_note.domain.use_case.network

import com.thussey.core.util.Either
import com.thussey.featurenote.feature_note.domain.mappers.toNote
import com.thussey.featurenote.feature_note.domain.repository.NoteNetworkRepository
import com.thussey.featurenote.feature_note.domain.use_case.db.GetDBNoteUseCase
import com.thussey.featurenote.feature_note.presentation.util.LOGOUT
import com.thussey.notesdb.model.Note
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val getDBNote: GetDBNoteUseCase,
    private val repository: NoteNetworkRepository
) {
    suspend operator fun invoke(id: String): Either<Note, NoteNotFoundException> {

        // todo: resolve conflicts
       // val noteFromDb = getDBNote(id)

        // If note isn not in db, attempt to return it from network
        // Corrected logic: If note is NOT in db, fetch from network.
        // If note IS in db, decide what to do (e.g., return it, or still try network as a source of truth)
       // if (noteFromDb == null) {
            val response = repository.getNoteById(id)
            return when (response.isSuccessful) {
                true -> {
                    val getNoteResponseItem = response.body()
                    if (getNoteResponseItem == null) {
                        Either.Failure(NoteNotFoundException("Error parsing note from network response"))
                    } else {
                        Either.Success(
                            getNoteResponseItem.toNote()
                        )
                    }
                }

                false -> {
                    if (response.code() == 401) {
                        return Either.Failure(NoteNotFoundException(LOGOUT))
                    }
                    Either.Failure(NoteNotFoundException("Note not found on network"))
                }
            }
//        } else {
//            // If the note was found in the database, return it.
//            // Or, if your logic dictates you should still check the network,
//            // you'd call the network here too, potentially to update the DB.
//            // For this example, we'll assume returning the DB version is sufficient.
//            return Either.Success(noteFromDb)
//        }
    }
}

class NoteNotFoundException(message: String) : Exception(message)

