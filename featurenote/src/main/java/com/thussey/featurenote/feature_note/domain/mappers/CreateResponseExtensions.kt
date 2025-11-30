package com.thussey.featurenote.feature_note.domain.mappers

import com.thussey.notesdb.model.Note
import com.thussey.notesnetwork.domain.model.network.createNote.CreateUpdateResponse
import java.time.Instant
import java.time.format.DateTimeParseException

fun CreateUpdateResponse.toNote(): Note {
    val parsedCreatedAtInstant: Instant = try {
        // Assuming the createdAt string is in ISO 8601 format (e.g., "2023-10-27T14:30:00Z")
        Instant.parse(this.createdAt) // Use java.time.Instant.parse
    } catch (e: DateTimeParseException) {
        // Handle the case where the date string is not in the expected format.
        // Option 1: Throw a custom exception
        // throw IllegalArgumentException("Invalid date format for createdAt: ${this.createdAt}", e)
        // Option 2: Use a default value (e.g., current time or a fixed epoch time)
        // System.err.println("Warning: Could not parse createdAt string '${this.createdAt}'. Using current time as fallback.")
        // Instant.now()
        // Option 3: If Note.createdAt can be nullable, return null (would require Note.createdAt to be Instant?)
        // For now, let's re-throw if parsing fails, as it indicates a data contract issue.
        // Depending on your app's error handling strategy, you might choose another option.
        throw e // Re-throw the exception or handle it more gracefully
    }

    // Convert the Instant to Long (epoch milliseconds)
    val createdAtAsLong: Long = parsedCreatedAtInstant.toEpochMilli()

    return Note(
        id = this.id,
        title = this.title,
        content = this.content,
        createdAt = createdAtAsLong, // Pass the Long value
        color = this.color
    )
}