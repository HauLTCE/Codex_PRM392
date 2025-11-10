### Project Overview

This project is a world-building or storytelling companion application named **Codex**. It allows users to create and manage different "worlds," and within each world, they can add and track characters, locations, and historical events.

### Core Functionality

*   **World Management:** Users can create, view, edit, search, sort, and delete worlds.
*   **Detailed World View:** Each world has a detail screen with separate tabs for its characters, locations, and events.
*   **Character Management:** Within a world, users can add, edit, and delete characters, including their name, backstory, and an optional home location.
*   **Location Management:** Users can manage various locations within a world, each with a name and description.
*   **Event Timeline:** Users can document events with a name, date, and description, creating a timeline for their world.
*   **Data Persistence:** All data is stored locally on the device.
*   **Dynamic Theming:** The application's color theme changes automatically based on the time of day (Morning, Day, Evening, Night).

### Technical Architecture

The project follows a modern **Model-View-ViewModel (MVVM)** architecture, which separates the UI from the business logic and data.

1.  **Data Layer:**
    *   **Room Database:** The project uses the Room Persistence Library as its local database (`AppDatabase`). It defines entities for `World`, `Character`, `Location`, and `Event`.
    *   **DAO (Data Access Objects):** Each entity has a corresponding DAO (`WorldDao`, `CharacterDao`, etc.) that defines the database operations (insert, update, delete, query).
    *   **Repository Pattern:** Repositories (`WorldRepository`, `CharacterRepository`, etc.) abstract the data sources. They handle data operations on a background thread using an `ExecutorService`.
    *   **Specification Pattern:** A custom `Specification` interface is used to build dynamic and reusable database queries in a clean way, particularly for character searching.

2.  **ViewModel Layer:**
    *   ViewModels (`WorldViewModel`, `CharacterViewModel`, etc.) act as a bridge between the UI and the Repositories.
    *   They hold and manage UI-related data using **LiveData**, ensuring the UI is always up-to-date with the database.
    *   They use `Transformations.switchMap` to reactively update lists based on search queries or sorting preferences without needing manual triggers.

3.  **UI Layer:**
    *   **Single-Activity Architecture:** The app uses a single `MainActivity` that hosts various Fragments.
    *   **Fragments:** The UI is built entirely with Fragments for different screens (e.g., `WorldListFragment`, `AddCharacterFragment`, `WorldDetailFragment`).
    *   **View & Data Binding:** It uses `RecyclerView` with `ListAdapter` for efficiently displaying lists of data.
    *   **Navigation:** Fragment transactions are used for navigating between different screens. A `ViewPager2` with a `TabLayout` is used in the world detail screen to manage the different entity tabs.
    *   **Material Design:** The UI is built using components from the Google Material Design library.

4.  **Dependency Injection:**
    *   **Hilt:** The project uses Dagger Hilt for dependency injection. The `DatabaseModule` provides singleton instances of the database, DAOs, and repositories throughout the app, which simplifies dependencies and improves testability.