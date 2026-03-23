# 📱 Basic Calling App

A simple Android calling app UI built with **Kotlin** and **Jetpack Compose**, demonstrating call flow state management, UI handling, and MVVM architecture.

## ✨ Features

### 1. Dial Pad Screen
- Number keypad (0–9, *, #) with letter sub-labels
- Real-time input display
- Backspace/delete functionality
- Call button to initiate outgoing call
- "Simulate Incoming Call" button for demo

### 2. Outgoing Call Screen
- Displays dialed number and contact name (if mapped)
- Animated "Calling..." status with pulsing avatar
- End Call button

### 3. Incoming Call Screen
- Displays caller number/name
- Animated ringing effect (expanding rings)
- Accept and Reject buttons

### 4. Active Call Screen
- Call duration timer (MM:SS format)
- Mute toggle (UI only — icon changes)
- Speaker toggle (UI only — icon changes)
- End Call button

## 🏗️ Architecture

- **Pattern**: MVVM (Model–View–ViewModel)
- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Navigation**: Navigation Compose (state-driven)
- **State Management**: StateFlow + collectAsStateWithLifecycle

### Call State Machine
```
Idle → Calling → Active → Ended → Idle
Idle → Ringing → Active → Ended → Idle
```

## 📁 Project Structure

```
com.example.callingapp/
├── model/
│   ├── CallState.kt          # Call state enum
│   └── CallInfo.kt           # Call data class
├── viewmodel/
│   └── CallViewModel.kt      # Central state management
├── navigation/
│   ├── Routes.kt             # Route constants
│   └── NavGraph.kt           # Navigation graph
├── ui/
│   ├── theme/
│   │   ├── Color.kt          # Color palette
│   │   ├── Type.kt           # Typography
│   │   └── Theme.kt          # Material 3 theme
│   └── screens/
│       ├── DialPadScreen.kt
│       ├── OutgoingCallScreen.kt
│       ├── IncomingCallScreen.kt
│       └── ActiveCallScreen.kt
└── MainActivity.kt            # Entry point
```

## 🚀 How to Run

1. Open the project in **Android Studio** (Hedgehog or newer)
2. Wait for Gradle sync to complete
3. Select an emulator or physical device (API 24+)
4. Click **Run ▶️**

## 📋 Technical Details

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compose BOM**: 2024.02.00
- **No backend or VoIP required** — purely UI-based simulation
- **Contact name mapping** included for demo numbers

## 🎯 Bonus Features

- ✅ Material 3 Design with dark/light theme support
- ✅ Contact name mapping for predefined numbers
- ✅ Smooth animations (pulse, ring, fade transitions)
- ✅ Edge-to-edge display support

# Calling-App