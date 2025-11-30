package com.thussey.featurenote.feature_note.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object NotesScreenRoute: NavKey
@Serializable
data class AddEditNoteScreenRoute(val noteId: String = "", val noteColor: Int = -1): NavKey