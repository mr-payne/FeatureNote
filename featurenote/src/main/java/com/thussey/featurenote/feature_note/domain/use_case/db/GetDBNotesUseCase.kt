package com.thussey.featurenote.feature_note.domain.use_case.db

import com.thussey.noteapp.feature_note.domain.model.NoteOrder
import com.thussey.noteapp.feature_note.domain.model.OrderType
import com.thussey.featurenote.feature_note.domain.repository.NoteRepository
import com.thussey.notesdb.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDBNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository
            .getNotes()
            .map { notes ->
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
    }

}