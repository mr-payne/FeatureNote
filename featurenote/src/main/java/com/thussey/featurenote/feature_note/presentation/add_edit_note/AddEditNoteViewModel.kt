package com.thussey.featurenote.feature_note.presentation.add_edit_note

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thussey.core.util.Either
import com.thussey.featurenote.feature_note.domain.model.NoteColors
import com.thussey.featurenote.feature_note.domain.use_case.network.NoteUseCases
import com.thussey.featurenote.feature_note.presentation.util.LOGOUT
import com.thussey.noteapp.feature_note.presentation.add_edit_note.model.AddEditNoteEvent
import com.thussey.noteapp.feature_note.presentation.add_edit_note.model.NoteTextFieldState
import com.thussey.notesdb.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.random
import kotlin.let
import kotlin.text.isBlank

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = MutableStateFlow(
        NoteTextFieldState(
            hint = "Enter title..."
        )
    )
    val noteTitle: StateFlow<NoteTextFieldState> = _noteTitle

    private val _noteContent = MutableStateFlow(
        NoteTextFieldState(
            hint = "Enter some content..."
        )
    )
    val noteContent: StateFlow<NoteTextFieldState> = _noteContent

    private val _noteColor: MutableStateFlow<Int> =
        MutableStateFlow(NoteColors.colors.random().toArgb())
    val noteColor: StateFlow<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow

    private var currentNoteId: String = ""

    init {
        savedStateHandle.get<String>("noteId")?.let { noteId ->
            if (noteId != "") {
                viewModelScope.launch {
                    val either = noteUseCases.getNoteUseCase(noteId)
                    when (either) {
                        is Either.Success -> {
                            val note = either.data
                            currentNoteId = note.id
                            _noteTitle.value = noteTitle.value.copy(
                                text = note.title,
                                isHintVisible = false
                            )
                            _noteContent.value = noteContent.value.copy(
                                text = note.content,
                                isHintVisible = false
                            )
                            _noteColor.value = note.color
                        }
                        is Either.Failure -> {
                            if (either.error.message == LOGOUT) {

                            } else {
                                // todo: show snackbar
                            }
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = _noteContent.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _noteContent.value.text.isBlank()
                )
            }

            AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    val note = Note(
                        title = noteTitle.value.text,
                        content = noteContent.value.text,
                        createdAt = System.currentTimeMillis(),
                        color = noteColor.value,
                        id = currentNoteId
                    )
                    val either = noteUseCases.createUpdateUseCase(note)
                    when (either) {
                        is Either.Success -> {
                            _eventFlow.emit(
                                UiEvent.SaveNote
                            )
                        }

                        is Either.Failure -> {
                            if (either.error.message == LOGOUT) {
                                _eventFlow.emit(
                                    UiEvent.Logout
                                )
                            } else {
                                _eventFlow.emit(
                                    UiEvent.ShowSnackbar(
                                        message = either.error.message ?: "Couldn't save note"
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    sealed class UiEvent() {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveNote: UiEvent()

        object Logout: UiEvent()
    }

}