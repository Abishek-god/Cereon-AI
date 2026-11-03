# Cereon AI

Cereon AI is a native Android AI workspace built with Kotlin, Jetpack Compose and Material 3. It is not a WebView application.

## Current implementation

This repository starts as an Android Studio project. The initial foundation includes:

- Kotlin + Jetpack Compose + Material 3
- MVVM presentation state and repository boundary
- Navigation Compose with phone bottom navigation and adaptive navigation rail
- OpenAI-compatible chat streaming using Server-Sent Events (SSE)
- Configurable provider endpoint, model and API credential
- Android Keystore-backed encrypted credential storage
- Room persistence for conversations and messages
- DataStore preferences
- Connection testing and provider model discovery
- Local conversation history

Provider APIs vary. Streaming, model discovery and capability support are treated as provider-dependent; the app does not infer that every model supports vision, tools or structured output.

## Requirements

- Android Studio with a compatible stable Android Gradle Plugin
- JDK 17
- Android SDK (compile/target SDK 35)
- Android Gradle Plugin 8.7.3 / Gradle 8.9 wrapper

Open the repository root in Android Studio and allow Gradle sync. Then run the `app` configuration on an emulator or device.

## Build

```bash
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

On Windows:

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebug
```

Debug APK output: `app/build/outputs/apk/debug/app-debug.apk`.

## API configuration and privacy

Add a provider from **Models**. Enter an HTTPS base URL, model ID and API key. API keys are encrypted with Android Keystore-backed storage. Keys are never committed or printed in logs. Conversations are stored locally in Room.

## Architecture

```
app/              Application, dependency graph, activity
core/             Network, database, security, preferences, shared UI
data/              Provider DTOs and repository implementations
domain/           Models, repository contracts and use cases
navigation/       Navigation graph and destinations
feature/          Chat, history, models, agents, skills, knowledge,
                  voice, create, markets, profile and settings
```

The initial implementation prioritizes a buildable end-to-end chat slice. Agent execution, RAG, voice, creation studio and market data require independent service integrations and are not represented as complete until implemented and tested.

## Security notes

- Never place provider keys in source code, BuildConfig, exported conversations or logs.
- The client connects directly to the configured provider; use trusted endpoints.
- External agent tools require explicit per-action consent before side effects.
- Market data must carry provider timestamps and delay metadata; no synthetic live prices are generated.
