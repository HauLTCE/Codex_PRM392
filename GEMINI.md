# Project: Codex

## Project Overview

This project is an Android application named "Codex". It serves as a world-building tool for creative writers, game designers, and hobbyists to create and manage fictional worlds. 

The application allows users to define and catalog various elements of their world, including:

*   **Worlds:** The top-level container for a fictional universe.
*   **Characters:** Detailed profiles for each character.
*   **Locations:** Descriptions of places within the world.
*   **Events:** A timeline of historical events.
*   **Magic/Technology:** Rules and descriptions of unique systems.
*   **Lore:** A general-purpose encyclopedia for miscellaneous information.
*   **Relationships:** A tool for mapping relationships between characters.

The application is built with the following technologies:

*   **Language:** Kotlin
*   **UI:** Jetpack Compose
*   **Build Tool:** Gradle
*   **Dependency Injection:** Hilt (inferred from `build.gradle.kts`)

## Building and Running

The project is a standard Android Gradle project. The following commands can be used to build, run, and test the application from the command line.

*   **Build the project:**
    ```bash
    ./gradlew build
    ```

*   **Install the debug version on a connected device or emulator:**
    ```bash
    ./gradlew installDebug
    ```

*   **Run unit tests:**
    ```bash
    ./gradlew test
    ```

## Development Conventions

*   **Build System:** The project uses Gradle with Kotlin scripts (`.kts`) for defining build logic.
*   **Dependencies:** Application dependencies are managed in the `app/build.gradle.kts` file.
*   **Code Style:** While no specific linter configuration is immediately apparent, the use of Kotlin and Jetpack Compose suggests adherence to modern Android development best practices.
*   **Release:** Proguard rules are configured in `app/proguard-rules.pro` for code obfuscation in release builds.
