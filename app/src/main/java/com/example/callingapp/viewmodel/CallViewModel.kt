package com.example.callingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.callingapp.model.CallInfo
import com.example.callingapp.model.CallState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel managing all call state and business logic.
 * Handles state transitions: Idle → Calling → Active → Ended
 *                            Idle → Ringing → Active → Ended
 */
class CallViewModel : ViewModel() {

    // --- State Flows ---
    private val _callState = MutableStateFlow(CallState.Idle)
    val callState: StateFlow<CallState> = _callState.asStateFlow()

    private val _callInfo = MutableStateFlow(CallInfo())
    val callInfo: StateFlow<CallInfo> = _callInfo.asStateFlow()

    private val _dialedNumber = MutableStateFlow("")
    val dialedNumber: StateFlow<String> = _dialedNumber.asStateFlow()

    // --- Internal jobs ---
    private var timerJob: Job? = null
    private var simulationJob: Job? = null

    // --- Contact name mapping (bonus feature) ---
    private val contactMap = mapOf(
        "1234567890" to "John Doe",
        "9876543210" to "Jane Smith",
        "5551234567" to "Mom",
        "5559876543" to "Dad",
        "1111111111" to "Office"
    )

    // --- Dial Pad Actions ---

    fun onDigitPress(digit: String) {
        if (_dialedNumber.value.length < 15) {
            _dialedNumber.update { it + digit }
        }
    }

    fun onBackspace() {
        _dialedNumber.update {
            if (it.isNotEmpty()) it.dropLast(1) else it
        }
    }

    fun clearNumber() {
        _dialedNumber.value = ""
    }

    // --- Call Actions ---

    /**
     * Initiates an outgoing call with the dialed number.
     * Transitions: Idle → Calling → (after 3s) → Active
     */
    fun startCall() {
        val number = _dialedNumber.value
        if (number.isBlank()) return

        _callInfo.value = CallInfo(
            number = number,
            contactName = contactMap[number]
        )
        _callState.value = CallState.Calling

        // Simulate call being answered after 3 seconds
        simulationJob = viewModelScope.launch {
            delay(3000)
            if (_callState.value == CallState.Calling) {
                _callState.value = CallState.Active
                startTimer()
            }
        }
    }

    /**
     * Simulates an incoming call from a random contact.
     * Transitions: Idle → Ringing
     */
    fun simulateIncomingCall() {
        if (_callState.value != CallState.Idle) return

        val incomingNumber = contactMap.keys.random()
        _callInfo.value = CallInfo(
            number = incomingNumber,
            contactName = contactMap[incomingNumber]
        )
        _callState.value = CallState.Ringing
    }

    /**
     * Accepts an incoming (ringing) call.
     * Transitions: Ringing → Active
     */
    fun acceptCall() {
        if (_callState.value != CallState.Ringing) return
        _callState.value = CallState.Active
        startTimer()
    }

    /**
     * Rejects an incoming call.
     * Transitions: Ringing → Ended → (after 1.5s) → Idle
     */
    fun rejectCall() {
        if (_callState.value != CallState.Ringing) return
        endCallFlow()
    }

    /**
     * Ends the current call from any active state.
     * Transitions: Calling/Active → Ended → (after 1.5s) → Idle
     */
    fun endCall() {
        endCallFlow()
    }

    // --- Toggle Actions (UI-only) ---

    fun toggleMute() {
        _callInfo.update { it.copy(isMuted = !it.isMuted) }
    }

    fun toggleSpeaker() {
        _callInfo.update { it.copy(isSpeakerOn = !it.isSpeakerOn) }
    }

    // --- Internal helpers ---

    private fun startTimer() {
        timerJob?.cancel()
        _callInfo.update { it.copy(durationSeconds = 0) }
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _callInfo.update { it.copy(durationSeconds = it.durationSeconds + 1) }
            }
        }
    }

    private fun endCallFlow() {
        timerJob?.cancel()
        simulationJob?.cancel()
        _callState.value = CallState.Ended

        viewModelScope.launch {
            delay(1500)
            resetToIdle()
        }
    }

    private fun resetToIdle() {
        _callState.value = CallState.Idle
        _callInfo.value = CallInfo()
        _dialedNumber.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        simulationJob?.cancel()
    }
}
