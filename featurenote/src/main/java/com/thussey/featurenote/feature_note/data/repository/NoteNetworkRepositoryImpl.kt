package com.thussey.featurenote.feature_note.data.repository

import com.thussey.featurenote.feature_note.domain.repository.NoteNetworkRepository
import com.thussey.notesnetwork.data.data_source.NotesApi
import com.thussey.notesnetwork.domain.model.network.createNote.CreateUpdateRequest
import com.thussey.notesnetwork.domain.model.network.createNote.CreateUpdateResponse
import com.thussey.notesnetwork.domain.model.network.getNotes.GetNotesResponse
import com.thussey.notesnetwork.domain.model.network.getNotes.GetNotesResponseItem
import retrofit2.Response
import javax.inject.Inject

class NoteNetworkRepositoryImpl @Inject constructor(
    private val notesApi: NotesApi,
): NoteNetworkRepository {

    override suspend fun createNote(createUpdateRequest: CreateUpdateRequest): Response<CreateUpdateResponse> {
        return notesApi.createNote(createUpdateRequest)
    }

    override suspend fun updateNote(
        noteId: String,
        createUpdateRequest: CreateUpdateRequest
    ): Response<CreateUpdateResponse> {
        return notesApi.updateNote(noteId, createUpdateRequest)
    }

    override suspend fun getNotes(): Response<GetNotesResponse> {
        return notesApi.getNotes()
    }

    override suspend fun deleteNote(noteId: String): Response<Unit> {
        return notesApi.deleteNote(noteId)
    }

    override suspend fun getNoteById(noteId: String): Response<GetNotesResponseItem> {
        return notesApi.getById(noteId)
    }
}