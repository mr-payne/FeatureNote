package com.thussey.featurenote.feature_note.domain.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
fun String.toInstant(): Instant {
    return Instant.parse(this)
}