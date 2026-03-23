package com.example.callingapp.model

/**
 * Represents the different states of a call.
 * Flow: Idle → Calling → Active → Ended
 *       Idle → Ringing → Active → Ended
 *       Any state → Ended → Idle
 */
enum class CallState {
    Idle,       // No active call, dial pad visible
    Calling,    // Outgoing call initiated, waiting for connection
    Ringing,    // Incoming call received, waiting for user action
    Active,     // Call connected, timer running
    Ended       // Call ended, brief display before returning to Idle
}
