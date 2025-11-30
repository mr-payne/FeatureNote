package com.thussey.featurenote.feature_note.domain.use_case.network

import com.thussey.core.util.Either
import com.thussey.featurenote.feature_note.domain.mappers.toNote
import com.thussey.featurenote.feature_note.domain.repository.NoteNetworkRepository
import com.thussey.featurenote.feature_note.domain.use_case.db.AddDBNoteUseCase
import com.thussey.featurenote.feature_note.presentation.util.LOGOUT
import com.thussey.notesdb.model.Note
import com.thussey.notesnetwork.domain.model.network.createNote.CreateUpdateRequest
import javax.inject.Inject

class CreateUpdateNoteUseCase @Inject constructor(
    private val addDBNote: AddDBNoteUseCase,
    private val repository: NoteNetworkRepository
) {
    suspend operator fun invoke(note: Note) : Either<Note?, CreateException> {
        val createRequest = CreateUpdateRequest(
            color = note.color,
            content = note.content,
            title = note.title
        )
        val response = if (note.id == "") {
            repository.createNote(createRequest)
        } else {
            repository.updateNote(note.id, createRequest)
        }
        return when(response.isSuccessful) {
            true -> {
                response.body()?.toNote()?.let { newNote ->
                    // todo: resolve conflict
                   // addDBNote(newNote)
                    Either.Success(newNote)
                } ?: Either.Failure(CreateException("Error parsing note"))
            }
            false -> {
                if (response.code() == 401) {
                    Either.Failure(
                        CreateException(LOGOUT)
                    )
                } else {
                    Either.Failure(
                        CreateException(
                            response.errorBody()?.toString() ?: "Unknown Error"
                        )
                    )
                }
            }
        }
    }
}

class CreateException(message: String) : Exception(message)