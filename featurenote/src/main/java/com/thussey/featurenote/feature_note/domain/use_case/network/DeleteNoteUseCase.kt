package com.thussey.featurenote.feature_note.domain.use_case.network

import com.thussey.core.util.Either
import com.thussey.featurenote.feature_note.domain.repository.NoteNetworkRepository
import com.thussey.featurenote.feature_note.domain.use_case.db.DeleteDBNoteUseCase
import com.thussey.featurenote.feature_note.presentation.util.LOGOUT
import com.thussey.notesdb.model.Note
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val deleteDBNote: DeleteDBNoteUseCase,
    private val repository: NoteNetworkRepository
) {
    suspend operator fun invoke(note: Note): Either<Boolean, DeleteError> {
        val response = repository.deleteNote(note.id)
        return when (response.isSuccessful) {
            true -> {
                deleteDBNote(note)
                Either.Success(true)
            }
            false -> {
                if (response.code() == 401) {
                    Either.Failure(
                        DeleteError(
                            LOGOUT
                        )
                    )
                } else {
                    Either.Failure(
                        DeleteError(
                            response.errorBody()?.toString() ?: "Unknown Error"
                        )
                    )
                }
            }
        }
    }
}

class DeleteError(message: String) : Exception(message)