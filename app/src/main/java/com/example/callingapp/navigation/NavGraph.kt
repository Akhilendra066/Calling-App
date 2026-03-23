package com.example.callingapp.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.callingapp.model.CallState
import com.example.callingapp.ui.screens.*
import com.example.callingapp.viewmodel.CallViewModel

@Composable
fun CallingNavGraph(
    navController: NavHostController,
    viewModel: CallViewModel
) {
    val callState by viewModel.callState.collectAsStateWithLifecycle()
    val callInfo by viewModel.callInfo.collectAsStateWithLifecycle()
    val dialedNumber by viewModel.dialedNumber.collectAsStateWithLifecycle()

    // Observe CallState changes and navigate accordingly
    LaunchedEffect(callState) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        when (callState) {
            CallState.Idle -> {
                if (currentRoute != Routes.DIAL_PAD) {
                    navController.navigate(Routes.DIAL_PAD) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            CallState.Calling -> {
                navController.navigate(Routes.OUTGOING_CALL) {
                    popUpTo(Routes.DIAL_PAD)
                }
            }
            CallState.Ringing -> {
                navController.navigate(Routes.INCOMING_CALL) {
                    popUpTo(Routes.DIAL_PAD)
                }
            }
            CallState.Active -> {
                navController.navigate(Routes.ACTIVE_CALL) {
                    popUpTo(Routes.DIAL_PAD)
                }
            }
            CallState.Ended -> {
                // Stay on current screen briefly, then auto-navigate back to Idle
                // (handled by ViewModel's endCallFlow delay)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.DIAL_PAD,
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        composable(Routes.DIAL_PAD) {
            DialPadScreen(
                dialedNumber = dialedNumber,
                onDigitPress = viewModel::onDigitPress,
                onBackspace = viewModel::onBackspace,
                onCallPress = viewModel::startCall,
                onSimulateIncoming = viewModel::simulateIncomingCall
            )
        }

        composable(Routes.OUTGOING_CALL) {
            OutgoingCallScreen(
                number = callInfo.number,
                contactName = callInfo.contactName,
                onEndCall = viewModel::endCall
            )
        }

        composable(Routes.INCOMING_CALL) {
            IncomingCallScreen(
                number = callInfo.number,
                contactName = callInfo.contactName,
                onAccept = viewModel::acceptCall,
                onReject = viewModel::rejectCall
            )
        }

        composable(Routes.ACTIVE_CALL) {
            ActiveCallScreen(
                number = callInfo.number,
                contactName = callInfo.contactName,
                durationSeconds = callInfo.durationSeconds,
                isMuted = callInfo.isMuted,
                isSpeakerOn = callInfo.isSpeakerOn,
                onToggleMute = viewModel::toggleMute,
                onToggleSpeaker = viewModel::toggleSpeaker,
                onEndCall = viewModel::endCall
            )
        }
    }
}
