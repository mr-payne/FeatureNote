package com.thussey.featurenote.feature_note.domain.mappers

import com.thussey.notesdb.model.Note
import com.thussey.notesnetwork.domain.model.network.getNotes.GetNotesResponseItem

fun GetNotesResponseItem.toNote(): Note {
    return Note(
        title = this.title,
        content = this.content,
        createdAt = this.createdAt.toInstant().toEpochMilli(),
        color = this.color,
        id = this.id
    )
}