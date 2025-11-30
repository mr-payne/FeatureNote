package com.thussey.featurenote.feature_note.domain.use_case.network

import com.thussey.core.util.Either
import com.thussey.featurenote.feature_note.domain.mappers.toNote
import com.thussey.noteapp.feature_note.domain.model.NoteOrder
import com.thussey.noteapp.feature_note.domain.model.OrderType
import com.thussey.featurenote.feature_note.domain.repository.NoteNetworkRepository
import com.thussey.featurenote.feature_note.domain.use_case.db.GetDBNotesUseCase
import com.thussey.notesdb.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val getDBNotes: GetDBNotesUseCase,
    private val repository: NoteNetworkRepository
) {
    suspend operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Either<Flow<List<Note>>, GetNotesException> {

        // network call
        val response = repository.getNotes()
        when (response.isSuccessful) {
            true -> {
                // get notes from device db
                 val dbFlow = flowOf(emptyList<Note>()) // getDBNotes(noteOrder)

                // convert network response to List of Notes
                val domainNotes: List<Note>? = response.body()?.let { getNotesResponseItem ->
                    getNotesResponseItem.map{ responseItem ->
                        responseItem.toNote()
                    }
                }
                // convert network List of Notes to a Flow of List of Note
                val domainFlow: Flow<List<Note>> = if (domainNotes != null) {
                    flowOf(domainNotes)
                } else {
                    flowOf(emptyList())
                }

                // combine the notes from the db and the notes form the network
                val combinedFlow: Flow<List<Note>> = dbFlow.combine(domainFlow) { dbList, domainList ->
                   (domainList + dbList)
                        .distinctBy { note ->
                            note.id
                        }
                }

                return Either.Success(
                    // sort the combined flow base on the note order
                    combinedFlow.map { notes ->
                        when (noteOrder.orderType) {
                            is OrderType.Ascending -> {
                                when (noteOrder) {
                                    is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                                    is NoteOrder.Date -> notes.sortedBy { it.createdAt }
                                    is NoteOrder.Color -> notes.sortedBy { it.color }
                                }
                            }
                            is OrderType.Descending -> {
                                when (noteOrder) {
                                    is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                                    is NoteOrder.Date -> notes.sortedByDescending { it.createdAt }
                                    is NoteOrder.Color -> notes.sortedByDescending { it.color }
                                }
                            }
                        }
                    }
                )
            }
            else -> {
                return Either.Failure(GetNotesException("Unable to retrieve notes"))
            }
        }
    }
}

class GetNotesException(message: String) : Exception(message)
