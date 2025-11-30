package com.thussey.featurenote.feature_note.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thussey.core.util.Either
import com.thussey.noteapp.feature_note.domain.model.NoteOrder
import com.thussey.noteapp.feature_note.domain.model.OrderType
import com.thussey.featurenote.feature_note.domain.use_case.network.GetNotesException
import com.thussey.featurenote.feature_note.domain.use_case.network.NoteUseCases
import com.thussey.featurenote.feature_note.presentation.util.LOGOUT
import com.thussey.noteapp.feature_note.presentation.notes.model.NotesEvent
import com.thussey.noteapp.feature_note.presentation.notes.model.NotesState
import com.thussey.notesdb.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.also

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(NotesState())
    val state: StateFlow<NotesState> = _state

    private val _eventFlow = MutableSharedFlow<NotesUiEvent>()
    val eventFlow: SharedFlow<NotesUiEvent> = _eventFlow

    private var recentlyDeletedNote: Note? = null
    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                // without ::class the comparison will fail b/c it would compare object references
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(event.noteOrder)
            }

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    val either = noteUseCases.deleteNoteUseCase(event.note)
                    when (either) {
                        is Either.Success -> {
                            getNotes(state.value.noteOrder)
                        }

                        is Either.Failure -> {
                            val message = either.error.message
                            if (message == LOGOUT) {
                                _eventFlow.emit(NotesUiEvent.Logout)
                            } else {
                                _eventFlow.emit(
                                    NotesUiEvent.ShowSnackbar(
                                        message ?: "Unknown Exception"
                                    )
                                )
                            }
                        }
                    }
                    recentlyDeletedNote = event.note
                }
            }

            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    recentlyDeletedNote?.also { restore ->
                        val restore = Note(
                            title = restore.title,
                            content = restore.content,
                            color = restore.color,
                            createdAt = 1,
                            id = ""
                        )
                        val either = noteUseCases.createUpdateUseCase(restore)
                        when (either) {
                            is Either.Success -> {
                                getNotes(noteOrder = state.value.noteOrder)
                            }

                            is Either.Failure -> {
                                val message = either.error.message
                                if (message == LOGOUT) {
                                    _eventFlow.emit(NotesUiEvent.Logout)
                                } else {
                                    // show snackbar error
                                    _eventFlow.emit(
                                        NotesUiEvent.ShowSnackbar(
                                            message ?: "Unknown Exception"
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }

            is NotesEvent.RefreshNotes -> {
                getNotes(NoteOrder.Date(OrderType.Descending))
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = viewModelScope.launch {
            val notesResponse: Either<Flow<List<Note>>, GetNotesException> =
                noteUseCases.getNotesUseCase(noteOrder)
            when (notesResponse) {
                is Either.Success -> {
                    // db notes
                    getNotesJob?.cancel()
                    getNotesJob = notesResponse.data
                        .onEach { notes ->
                            _state.value = state.value.copy(
                                notes = notes,
                                noteOrder = noteOrder
                            )
                        }.launchIn(viewModelScope)
                }

                is Either.Failure -> {
                    if (notesResponse.error.message == LOGOUT) {
                        _eventFlow.emit(NotesUiEvent.Logout)
                    } else {
                        _eventFlow.emit(
                            NotesUiEvent.ShowSnackbar(
                                notesResponse.error.message ?: "Unknown Exception"
                            )
                        )
                    }
                }
            }
        }
    }
}

sealed class NotesUiEvent {
    object Logout: NotesUiEvent()
    data class ShowSnackbar(val message: String): NotesUiEvent()
}