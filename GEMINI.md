# Codex Project (Android)

## Project Overview

Codex is an Android application designed as a world-building tool for creative writers, game designers, and hobbyists to create and manage their fictional worlds.The application allows users to define, catalog, and link various elements of their world, including:•Worlds: The top-level container for a fictional universe.•Characters: Detailed profiles for each character.•Locations: Descriptions of places, cities, and regions.•Events: A timeline of historical and significant events.•Magic/Technology Systems: Rules and descriptions for unique world mechanics.•Lore: A general-purpose encyclopedia for miscellaneous information.•Relationships: A tool for mapping complex relationships between characters and entities

It is built using the following technologies:

*   **Language:** Kotlin
*   **UI:** Jetpack Compose
*   **Dependency Injection:** Hilt
*   **Database:** Room
*   **Build Tool:** Gradle

The application's ID is `com.hault.codex`. It targets Android SDK version 36 and has a minimum SDK version of 26.

## Building and Running

### Building the App

To build the application and generate a debug APK, run the following command from the root directory of the project:

```bash
./gradlew assembleDebug
```

The generated APK will be located in `app/build/outputs/apk/debug/`.

### Running the App

To build and install the application on a connected Android device or emulator, use the following command:

```bash
./gradlew installDebug
```

### Running Tests

To run the unit tests for the project, execute:

```bash
./gradlew test
```

To run the instrumented (Android) tests, use:

```bash
./gradlew connectedAndroidTest
```

## Development Conventions

*   **Dependency Management:** Dependencies are managed using Gradle's version catalogs, as seen in the `gradle/libs.versions.toml` file.
*   **UI:** The UI is built declaratively using Jetpack Compose.
*   **Dependency Injection:** Hilt is used for managing dependencies throughout the application.
*   **Database:** Room is used for local data persistence. All database-related code should follow the patterns and best practices for Room.
*   **Testing:** The project includes both unit tests (in `app/src/test`) and instrumented tests (in `app/src/androidTest`). New features should be accompanied by corresponding tests.
