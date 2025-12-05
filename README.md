# Evento

A practice-focused Android application built to deeply understand Clean Architecture, MVVM, UI state handling, and Compose UI design patterns — not a real-world production app.
The backend is fully mocked using an interceptor, allowing real API-like flows

## Preview
<p align="center">
  <img src="https://github.com/user-attachments/assets/8d01e1b6-b54e-4807-a6a4-1c3560a4b1e7" width="300" />
  <img src="https://github.com/user-attachments/assets/cae1ea44-3443-4ac5-a3bf-42d23437fcb0" width="300" />
  <img src="https://github.com/user-attachments/assets/28f64367-5b9d-478e-afc2-9d9f03a5d683" width="300" />
</p>


## Tech stack
Typical technologies used in this project
- Language: Kotlin
- UI: Jetpack Compose 
- Architecture: MVVM with Android Jetpack (ViewModel, StateFlow)
- Networking: Retrofit + OkHttp
- Dependency injection: Hilt 
- Concurrency: Kotlin Coroutines
- Testing: JUnit, MockK for unit tests
- Build: Gradle (Kotlin DSL)

## Development environment
- Android Studio: Android Studio Narwhal 3 Feature Drop | 2025.1.3

## Getting started
1. Clone the repository:
   git clone https://github.com/deepak-prajapatii/Evento.git
2. Open the project in Android Studio (use Android Studio Narwhal 3 Feature Drop | 2025.1.3).
3. Let Gradle sync and download dependencies.
4. Run the app on an emulator or device

## Build & run (CLI)
- Build debug APK:
  ./gradlew assembleDebug
- Install to connected device:
  ./gradlew installDebug

## Testing
Unit tests are organized by module under src/test and src/androidTest.


