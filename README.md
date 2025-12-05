# Evento

A concise description of the Evento Android app — a mobile application for event booking.

## Preview
![Screenshot_20251205_164634.png](../../../Desktop/Screenshot_20251205_164634.png)
![Screenshot_20251205_164628.png](../../../Desktop/Screenshot_20251205_164628.png)
![img.png](img.png)

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

Common test commands:
- Run all JVM unit tests:
  ./gradlew test
- Run debug unit tests:
  ./gradlew testDebugUnitTest
- Run instrumentation (connected) tests on a device/emulator:
  ./gradlew connectedAndroidTest
- Run Android instrumentation tests for a specific variant:
  ./gradlew connectedDebugAndroidTest


