package com.thussey.noteapp.feature_note.domain.model

sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()

}