package com.thussey.featurenote.di

import com.thussey.featurenote.feature_note.data.data_source.NotesApi
import com.thussey.featurenote.feature_note.data.repository.NoteNetworkRepositoryImpl
import com.thussey.featurenote.feature_note.data.repository.NoteRepositoryImpl
import com.thussey.featurenote.feature_note.domain.repository.NoteNetworkRepository
import com.thussey.featurenote.feature_note.domain.repository.NoteRepository
import com.thussey.featurenote.feature_note.domain.use_case.db.AddDBNoteUseCase
import com.thussey.featurenote.feature_note.domain.use_case.db.DeleteDBNoteUseCase
import com.thussey.featurenote.feature_note.domain.use_case.db.GetDBNoteUseCase
import com.thussey.featurenote.feature_note.domain.use_case.db.GetDBNotesUseCase
import com.thussey.featurenote.feature_note.domain.use_case.db.NoteDBUseCases
import com.thussey.featurenote.feature_note.domain.use_case.network.CreateUpdateNoteUseCase
import com.thussey.featurenote.feature_note.domain.use_case.network.DeleteNoteUseCase
import com.thussey.featurenote.feature_note.domain.use_case.network.GetNoteUseCase
import com.thussey.featurenote.feature_note.domain.use_case.network.GetNotesUseCase
import com.thussey.featurenote.feature_note.domain.use_case.network.NoteUseCases
import com.thussey.notesdb.data_source.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object FeatureNoteModule {

    @Provides
    @Singleton // Or another scope if needed
    fun provideNotesApi(
        @Named("NotesApiBaseUrl") baseUrl: String,
        okHttpClient: OkHttpClient
    ): NotesApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteNetworkRepository(
        notesApi: NotesApi,
    ): NoteNetworkRepository {
        return NoteNetworkRepositoryImpl(
            notesApi
        )
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteDBUseCases {
        return NoteDBUseCases(
            getNotes = GetDBNotesUseCase(repository),
            deleteNote = DeleteDBNoteUseCase(repository),
            addNote = AddDBNoteUseCase(repository),
            getNote = GetDBNoteUseCase(repository)
        )
    }

    @Singleton
    @Provides
    fun provideNoteNetworkUseCases(
        noteDBUseCases: NoteDBUseCases,
        networkRepository: NoteNetworkRepository,
    ) : NoteUseCases {
        return NoteUseCases(
            createUpdateUseCase = CreateUpdateNoteUseCase(
                addDBNote = noteDBUseCases.addNote,
                repository = networkRepository
            ),
            getNotesUseCase = GetNotesUseCase(
                getDBNotes = noteDBUseCases.getNotes,
                repository = networkRepository
            ),
            deleteNoteUseCase = DeleteNoteUseCase(
                deleteDBNote = noteDBUseCases.deleteNote,
                repository = networkRepository
            ),
            getNoteUseCase = GetNoteUseCase(
                getDBNote = noteDBUseCases.getNote,
                repository = networkRepository
            )
        )
    }
}