package com.thussey.featurenote.feature_note.data.data_source

import com.thussey.notesnetwork.domain.model.network.createNote.CreateUpdateRequest
import com.thussey.notesnetwork.domain.model.network.createNote.CreateUpdateResponse
import com.thussey.notesnetwork.domain.model.network.getNotes.GetNotesResponse
import com.thussey.notesnetwork.domain.model.network.getNotes.GetNotesResponseItem
import com.thussey.notesnetwork.domain.model.network.login.LoginRequest
import com.thussey.notesnetwork.domain.model.network.login.LoginResponse
import com.thussey.notesnetwork.domain.model.network.refresh.RefreshRequest
import com.thussey.notesnetwork.domain.model.network.refresh.RefreshResponse
import com.thussey.notesnetwork.domain.model.network.register.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Retrofit interface
interface NotesApi {
    @PUT("notes/{id}")
    suspend fun updateNote(
        @Path("id") id: String,
        @Body createUpdateRequest: CreateUpdateRequest
    ): Response<CreateUpdateResponse>

    @POST("notes")
    suspend fun createNote(@Body createUpdateRequest: CreateUpdateRequest): Response<CreateUpdateResponse>

    @GET("notes")
    suspend fun getNotes(): Response<GetNotesResponse>

    @DELETE("notes/{id}")
    suspend fun deleteNote(@Path("id") id: String): Response<Unit>

    @GET("notes/{id}")
    suspend fun getById(@Path("id") id: String): Response<GetNotesResponseItem>
}