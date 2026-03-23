package com.example.callingapp.model

/**
 * Data class holding information about the current call.
 */
data class CallInfo(
    val number: String = "",
    val contactName: String? = null,
    val isMuted: Boolean = false,
    val isSpeakerOn: Boolean = false,
    val durationSeconds: Int = 0
)
