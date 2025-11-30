package com.thussey.featurenote.feature_note.domain.repository


import com.thussey.notesnetwork.domain.model.network.createNote.CreateUpdateRequest
import com.thussey.notesnetwork.domain.model.network.createNote.CreateUpdateResponse
import com.thussey.notesnetwork.domain.model.network.getNotes.GetNotesResponse
import com.thussey.notesnetwork.domain.model.network.getNotes.GetNotesResponseItem
import retrofit2.Response

interface NoteNetworkRepository {
    suspend fun createNote(createUpdateRequest: CreateUpdateRequest): Response<CreateUpdateResponse>

    suspend fun updateNote(
        noteId: String,
        createUpdateRequest: CreateUpdateRequest
    ): Response<CreateUpdateResponse>
    suspend fun getNotes(): Response<GetNotesResponse>
    suspend fun deleteNote(noteId: String): Response<Unit>
    suspend fun getNoteById(noteId: String): Response<GetNotesResponseItem>
}