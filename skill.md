# Android Development Skill Guidelines & Architecture Blueprint

This document defines the architectural standards and coding principles for the **ExpenseTracker** project. Every new feature or refactor must adhere to these guidelines to ensure the codebase remains scalable, testable, and maintainable.

## 🏛️ Core Architecture: Clean Architecture
The project is divided into three distinct layers. Code dependencies must only point inwards (Data/Presentation -> Domain).

### 1. Domain Layer (The "What")
*   **Location**: `dev.spikeysanju.expensetracker.domain`
*   **Contents**: Pure Kotlin code (no Android dependencies).
    *   **Models**: Data classes representing business entities.
    *   **Repository Interfaces**: Define the contract for data operations.
    *   **Use Cases**: Classes that encapsulate a single business logic task (e.g., `GetFinancialInsightsUseCase`).
*   **Rule**: This is the most stable layer. It should never change when the database or UI changes.

### 2. Data Layer (The "How")
*   **Location**: `dev.spikeysanju.expensetracker.data`
*   **Contents**: Implementation details.
    *   **Local/Remote Sources**: Room DAOs, Retrofit APIs.
    *   **Repository Implementations**: Classes that implement the Domain Repository interfaces.
    *   **Mappers**: Convert Data-level models to Domain-level models.
*   **Rule**: This layer is responsible for data persistence and network logic.

### 3. Presentation Layer (The "UI")
*   **Location**: `dev.spikeysanju.expensetracker.ui` and `dev.spikeysanju.expensetracker.view`
*   **Pattern**: **MVVM/MVI**
    *   **ViewModels**: Expose a single `StateFlow<UiState>` to the UI. Use `viewModelScope` for coroutines.
    *   **Compose Screens**: Purely declarative. They should only "observe" state and "emit" events back to the ViewModel.
*   **Rule**: Logic should be kept out of Composable functions.

---

## 🛠️ Coding Principles (SOLID)

1.  **Single Responsibility (S)**: Every class or function should have one job. (e.g., a UseCase shouldn't handle UI formatting).
2.  **Open/Closed (O)**: Design modules that are open for extension but closed for modification.
3.  **Liskov Substitution (L)**: Subclasses must be substitutable for their base classes.
4.  **Interface Segregation (I)**: Don't force a class to implement methods it doesn't use.
5.  **Dependency Inversion (D)**: Always depend on abstractions (interfaces), not concrete implementations. Use Hilt for injection.

---

## 🚀 Technical Requirements

*   **Dependency Injection**: Use **Hilt**. Define interfaces in `domain` and bind implementations in `di/RepositoryModule`.
*   **Reactive Programming**: Use **Kotlin Flow** for all asynchronous data streams.
*   **State Management**: Views must collect state using `collectAsStateWithLifecycle()` or `collectAsState()`.
*   **UI Components**: Use **Material 3** components. Maintain a consistent theme in `ui.theme`.
*   **Error Handling**: Wrap repository results in a `Result` wrapper or use a dedicated `ViewState` sealed class (Loading, Success, Error).

## 📝 Workflow for New Features
1.  **Define Domain Model** in `domain/model`.
2.  **Define Repository Interface** in `domain/repository`.
3.  **Create Use Case** in `domain/usecase` to handle the logic.
4.  **Implement Repository** in `data/repository`.
5.  **Inject via Hilt** in `di/RepositoryModule`.
6.  **Create ViewModel** and observe the Use Case.
7.  **Build UI** with Jetpack Compose.
