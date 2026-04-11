# CLAUDE.md — Skillima Mentors Android

This file provides guidance to Claude Code subagents working on the Skillima Mentors Android app (Kotlin + Jetpack Compose).

---

## 1. Commands

Run all commands from the project root: `Skillima Mentors Android/`

```bash
# Build debug APK
./gradlew assembleDebug

# Install debug APK on connected device
./gradlew installDebug

# Run all unit tests
./gradlew test

# Run all instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Run lint
./gradlew lint

# Build a specific module (e.g., screens:auth)
./gradlew :screens:auth:assembleDebug

# Run unit tests for a single module
./gradlew :screens:auth:test
./gradlew :data:auth:test
./gradlew :core:utils:test

# Clean build
./gradlew clean
```

---

## 2. Module Architecture

```
Skillima Mentors Android/
├── app/                        # Application entry point, DI wiring, MainActivity, navigation host
├── build-logic/                # Custom Gradle convention plugins (not an Android module)
├── screens/
│   ├── auth/                   # Login, Signup screens
│   ├── onboarding/             # Onboarding flow
│   └── guild/                  # Guild screen
├── data/
│   ├── auth/                   # AuthRepository, AuthRepositoryImpl, Supabase auth calls
│   ├── local/                  # Room database, DAOs, local repositories
│   ├── user/                   # User data repository
│   └── guild/                  # Guild data repository
├── core/
│   ├── ui/                     # Compose design system: components, theme, colors, typography
│   ├── utils/                  # Response sealed class, validators, NetworkError, InternetMonitor
│   ├── models/                 # Shared DTOs: GuildDto, GuildSkillDto, SkillDto, UserData
│   ├── navigation/             # Navigator class, Destinations interface, NavigationRoutes
│   ├── supabase/               # Supabase client Koin module, SupabaseError, SupabaseConstants
│   └── datastore/              # Jetpack DataStore helpers and Koin module
```

### Where to add new code

| What you're building | Which module |
|---|---|
| New feature screen (UI + ViewModel) | `screens/<feature>/` |
| Data fetching / repository | `data/<feature>/` |
| Shared UI components | `core/ui/` |
| Shared utilities / validators | `core/utils/` |
| Shared data models / DTOs | `core/models/` |
| Navigation destinations | `core/navigation/` (add to `NavigationRoutes.kt`) |
| Supabase client config | `core/supabase/` |

### Convention plugins and when to apply each

Defined in `build-logic/convention/src/main/kotlin/`:

| Plugin alias | Convention class | When to apply |
|---|---|---|
| `skillima.android` | `AndroidApplicationConventionPlugin` | App module only (`app/`) |
| `skillima.android.compose` | `AndroidApplicationComposeConventionPlugin` | App module with Compose |
| `skillima.android.library` | `AndroidLibraryConventionPlugin` | Any Android library module (applies `com.android.library` + Kotlin) |
| `skillima.android.library.compose` | `AndroidLibraryComposeConventionPlugin` | Library module that uses Compose (applies Compose compiler plugin) |
| `skillima.android.feature` | `AndroidFeatureConventionPlugin` | Feature screen library (adds lifecycle-runtime-compose, viewmodel-compose) |
| `skillima.android.room` | `SkillimaAndroidRoomConventionPlugin` | Any module using Room |
| `skillima.android.koin` | `AndroidKoinConventionPlugin` | Any module using Koin DI (adds koin-compose, koin-android, koin-compose-viewmodel, koin-navigation3) |
| `skillima.firebase` | `AndroidApplicationFirebaseConventionPlugin` | App module for Firebase integration |

---

## 3. Creating a New Feature Module

### Step-by-step example: adding `screens/sessions`

**Step 1: Create the directory structure**
```
screens/sessions/
└── src/main/java/skillima/screens/sessions/
    ├── SessionsViewModel.kt
    ├── sessionsPresentationModule.kt
    ├── routes/
    │   └── SessionsRoute.kt
    ├── screens/
    │   └── SessionsScreen.kt
    └── state/
        ├── SessionsUiState.kt
        └── SessionsEvents.kt
```

**Step 2: Write `screens/sessions/build.gradle.kts`**
```kotlin
plugins {
    alias(libs.plugins.skillima.android.library.compose)
    alias(libs.plugins.skillima.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.skillima.koin)
}

android {
    namespace = "skillima.mentors.sessions"
    compileSdk {
        version = release(36)
    }
    defaultConfig {
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(projects.core.navigation)
    implementation(projects.core.ui)
    implementation(projects.core.utils)
    implementation(projects.core.models)
    implementation(projects.data.sessions)   // or whichever data module
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
```

**Step 3: Register in `settings.gradle.kts`**
```kotlin
include(":screens:sessions")
```

**Step 4: Add the Destinations to `core/navigation/src/main/java/skillima/mentors/navigation/NavigationRoutes.kt`**
```kotlin
data object SessionsScreen : Destinations
```

**Step 5: Register the Koin module in `app/src/main/java/skillima/mentors/DI.kt`**
```kotlin
import skillima.screens.sessions.sessionsPresentationModule

val appModules = module {
    includes(
        // ... existing modules ...
        sessionsPresentationModule,
    )
    // ...
}
```

No changes needed to `SkillimaApp.kt` — it already calls `GlobalContext.startKoin { modules(appModules) }`.

**Step 6: Add route to `app/src/main/java/skillima/mentors/navigation/Screen.kt`** (if using Screen sealed class for app-level routing; otherwise use `NavigationRoutes.kt` destinations directly).

**Step 7: Wire into `SkillimaNavHost.kt`** — add a `NavDisplay` entry for the new destination.

---

## 4. Dependency Injection (Koin)

### Initialization

`SkillimaApp.kt` starts Koin once:
```kotlin
class SkillimaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            modules(appModules)
            androidContext(this@SkillimaApp)
        }
    }
}
```

`appModules` is the single aggregated module defined in `app/src/main/java/skillima/mentors/DI.kt`. Every feature/data module's Koin module must be included in `appModules` via `includes(...)`.

### Declaring a Koin module in a feature/data module

Each module has a `di/` package with a `val xyzModule = module { ... }` declaration.

```kotlin
// screens/sessions/src/main/java/skillima/screens/sessions/sessionsPresentationModule.kt
package skillima.screens.sessions

import org.koin.dsl.module

val sessionsPresentationModule = module {
    single { SessionsViewModel(get()) }
}
```

### Declaring a Repository

```kotlin
// data/sessions/src/main/java/skillima/data/sessions/di/SessionsModule.kt
val sessionsDataModule = module {
    single<SessionsRepository> {
        SessionsRepositoryImpl(get()) // get() resolves SupabaseClient
    }
}
```

### Declaring a ViewModel

```kotlin
// Use single{} if the ViewModel is activity-retained (shared across screens), viewModel{} if per-screen
single { SessionsViewModel(get()) }

// Per-screen ViewModel:
viewModel { SessionsViewModel(get()) }
```

Note: The auth module uses `single { AuthViewmodel(get(), get()) }` because the ViewModel is shared across Login and Signup screens.

### Injecting in a Composable (Route)

```kotlin
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin

@Composable
fun SessionsRoute(
    viewModel: SessionsViewModel = koinViewModel(),
    navigator: Navigator = getKoin().get()
) {
    // ...
}
```

### Injecting in a non-Composable (Repository / class)

```kotlin
class SessionsRepositoryImpl(
    private val supabaseClient: SupabaseClient  // injected via constructor by Koin
) : SessionsRepository
```

---

## 5. ViewModel + Flow Pattern

```kotlin
class SessionsViewModel(
    private val sessionsRepository: SessionsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SessionsUiState>(SessionsUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun loadSessions() {
        viewModelScope.launch {
            sessionsRepository.getSessions().collect { response ->
                when (response) {
                    is Response.Loading -> _uiState.value = SessionsUiState.Loading
                    is Response.Error   -> _uiState.value = SessionsUiState.Error(response.exception.error)
                    is Response.Success -> _uiState.value = SessionsUiState.Success(response.data)
                }
            }
        }
    }
}

sealed interface SessionsUiState {
    data object Idle    : SessionsUiState
    data object Loading : SessionsUiState
    data class  Error(val message: Int) : SessionsUiState   // @StringRes Int from SupabaseError
    data class  Success(val data: List<SessionDto>) : SessionsUiState
}
```

### Collecting StateFlow in Compose

```kotlin
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SessionsRoute(viewModel: SessionsViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SessionsScreen(uiState = uiState)
}
```

---

## 6. Supabase Kotlin SDK Patterns

### Getting the Supabase client

The `SupabaseClient` is a Koin `single` declared in `core/supabase/src/main/java/skillima/mentors/supabase/di/CoreModule.kt`. Inject it via constructor parameter — Koin resolves it with `get()`.

```kotlin
class SessionsRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : SessionsRepository
```

### Auth: sign in

```kotlin
supabaseClient.auth.signInWith(Email) {
    email = userData.email
    password = userData.password
}
val session = supabaseClient.auth.currentUserOrNull()
```

### Auth: sign up with metadata

```kotlin
supabaseClient.auth.signUpWith(Email) {
    email = userData.email
    password = userData.password
    data = buildJsonObject {
        put("name", userData.name)
        put("role", "mentor")
    }
}
```

### Select query

```kotlin
val rows = supabaseClient.from(SupabaseConstants.GUILD_TABLE)
    .select()
    .decodeList<GuildDto>()
```

### Insert

```kotlin
supabaseClient.from(SupabaseConstants.USER_TABLE)
    .insert(userDto)
```

### RPC call

```kotlin
supabaseClient.postgrest.rpc("function_name", buildJsonObject {
    put("param_key", paramValue)
})
```

### Error handling pattern

Always wrap Supabase calls in `try/catch`, emit `Response.Loading` first, then `Response.Success` or `Response.Error`. All errors must map to a `SupabaseError` (which is `@StringRes Int`):

```kotlin
flow {
    emit(Response.Loading)
    try {
        val result = supabaseClient.from("table").select().decodeList<MyDto>()
        emit(Response.Success(result))
    } catch (e: AuthRestException) {
        emit(Response.Error(mapAuthError(e.error)))
    } catch (e: Exception) {
        emit(Response.Error(AuthError.Unknown))
    }
}.flowOn(Dispatchers.IO)
```

---

## 7. Response / Result Pattern

Defined in `core/utils/src/main/java/skillima/mentors/utils/Response.kt`:

```kotlin
sealed interface Response<out T> {
    data object Loading : Response<Nothing>
    data class Error(val exception: SupabaseError) : Response<Nothing>
    data class Success<T>(val data: T) : Response<T>
}
```

`SupabaseError` (in `core/supabase/`) is an interface with a single `@StringRes val error: Int`. Each data module defines its own error enum implementing this interface (e.g. `AuthError` in `data/auth/`).

### Wrapping a Supabase call

```kotlin
fun getSessions(): Flow<Response<List<SessionDto>>> = flow {
    emit(Response.Loading)
    try {
        val sessions = supabaseClient.from(SupabaseConstants.SESSIONS_TABLE)
            .select()
            .decodeList<SessionDto>()
        emit(Response.Success(sessions))
    } catch (e: Exception) {
        emit(Response.Error(SessionError.Unknown))
    }
}.flowOn(Dispatchers.IO)
```

### Handling Response in ViewModel

```kotlin
viewModelScope.launch {
    repository.getSessions().collect { response ->
        _uiState.value = when (response) {
            is Response.Loading -> SessionsUiState.Loading
            is Response.Error   -> SessionsUiState.Error(response.exception.error)
            is Response.Success -> SessionsUiState.Success(response.data)
        }
    }
}
```

---

## 8. Navigation

### How routes work

Navigation is driven by a custom `Navigator` class (`core/navigation/`) that owns a `mutableStateListOf<Destinations>` back stack. Destinations are plain `data object` / `data class` types defined in `core/navigation/src/main/java/skillima/mentors/navigation/NavigationRoutes.kt`:

```kotlin
interface Destinations
interface RequiresLogin   // implement this on destinations that need auth

data object LoginScreen  : Destinations
data object GuildScreen  : Destinations
// etc.
```

`app/src/main/java/skillima/mentors/navigation/Screen.kt` contains a `sealed class Screen` with `@Serializable` variants used for **Navigation 3 type-safe backstack serialization** — keep all entries `@Serializable`.

### Navigating forward

```kotlin
navigator.goTo(GuildScreen)          // pushes onto back stack; checks RequiresLogin
navigator.replaceTop(SignupScreen)   // replaces current top (no duplicate check needed)
```

### Navigating back

```kotlin
navigator.goBack()   // removes last entry if stack size > 1
```

### Clearing the back stack (post-login)

```kotlin
navigator.backStack.clear()
navigator.goTo(GuildScreen)
```

### Receiving a destination in a composable

The `Navigator` is injected via `getKoin().get()` inside Route composables:

```kotlin
@Composable
fun SessionsRoute(
    navigator: Navigator = getKoin().get()
) {
    SessionsScreen(
        onBack = { navigator.goBack() },
        onNavigateToDetail = { navigator.goTo(SessionDetailScreen) }
    )
}
```

### How SkillimaNavHost wires destinations

`SkillimaNavHost.kt` (in `app/`) observes `navigator.backStack` and renders the correct Route composable for each `Destinations` entry. When adding a new screen, add a `when` branch for your new `Destinations` object there.

---

## 9. Adding a New Screen (end-to-end)

Every new screen touches **all** of these files:

| # | File | Change |
|---|---|---|
| 1 | `core/navigation/src/main/java/skillima/mentors/navigation/NavigationRoutes.kt` | Add `data object MyScreen : Destinations` |
| 2 | `app/src/main/java/skillima/mentors/navigation/Screen.kt` | Add `@Serializable data object MyScreen : Screen()` if using serialized backstack |
| 3 | `settings.gradle.kts` | `include(":screens:myfeature")` |
| 4 | `screens/myfeature/build.gradle.kts` | New file — use `skillima.android.library.compose` + `skillima.android.library` + `skillima.koin` plugins |
| 5 | `screens/myfeature/src/.../MyViewModel.kt` | ViewModel with StateFlow UiState |
| 6 | `screens/myfeature/src/.../state/MyUiState.kt` | Sealed interface for UI states |
| 7 | `screens/myfeature/src/.../screens/MyScreen.kt` | Stateless `@Composable` |
| 8 | `screens/myfeature/src/.../routes/MyRoute.kt` | Stateful Route composable: injects ViewModel + Navigator |
| 9 | `screens/myfeature/src/.../myPresentationModule.kt` | `val myPresentationModule = module { single { MyViewModel(get()) } }` |
| 10 | `app/src/main/java/skillima/mentors/DI.kt` | Add `myPresentationModule` to `includes(...)` in `appModules` |
| 11 | `app/src/main/java/skillima/mentors/navigation/SkillimaNavHost.kt` | Add `MyScreen -> MyRoute()` in the destinations `when` block |

If the screen needs its own data repository, also create:
- `data/myfeature/build.gradle.kts`
- `data/myfeature/src/.../repository/MyRepository.kt` + `MyRepositoryImpl.kt`
- `data/myfeature/src/.../di/MyDataModule.kt`
- Add `myDataModule` to `DI.kt` includes
- `include(":data:myfeature")` in `settings.gradle.kts`

---

## 10. Design System

### Theme location

`core/ui/src/main/java/skillma/core/ui/theme/`
- `Theme.kt` — `SkillimaMentorsTheme` composable (dark-only, wraps `MaterialTheme`)
- `Color.kt` — all color tokens
- `Type.kt` — `AppTypography` with `displayFontFamily`

Always wrap previews and screens in `SkillimaMentorsTheme { }`.

### Color tokens (from `Color.kt`)

Primary palette: `Violet50` through `Violet900` — primary is `Violet500 = Color(0xFF6312B2)`

Supporting palettes: `Blue50–900`, `Orange50–900`, `Green50–900`

Neutral scale:
- Light end: `NeutralWhite1` (pure white) through `NeutralWhite10`
- Dark end: `NeutralBlack1` (white) through `NeutralBlack13` (near-black `0xFF020202`)
- Use `NeutralBlack9 = Color(0xFF464646)` for secondary icon tints (as seen in `LoginScreen`)
- Use `NeutralBlack13` for app background in dark theme; `NeutralBlack11` for surface/cards

### Theme color scheme mappings (dark theme only)

```
primary            = Violet500
background         = NeutralBlack13   // very dark, near black
surface            = NeutralBlack11   // cards, sheets
surfaceVariant     = NeutralBlack9    // used for secondary text color
error              = Orange700
secondary          = Blue300
tertiary           = Green300
```

### Typography

`AppTypography` uses `displayFontFamily` (custom fonts from `core/ui/res/font/`). Use standard Material3 roles:
- `MaterialTheme.typography.headlineMedium` — screen titles
- `MaterialTheme.typography.bodyLarge` — button labels
- `MaterialTheme.typography.bodyMedium` — secondary/hint text

### Custom UI components (from `core/ui`)

Import from `skillma.core.ui.design.*`:
- `SkillimaButton` — takes `state: ButtonState`, `colors: ButtonColor`, `enabled: Boolean`
- `SkillimaTextField` — takes `color: TextFieldColors`, `value`, `hintValue`, `onValueChange`, `navigationIcon`
- `SkillimaPasswordTextField` — same as above with built-in visibility toggle
- `SkillimaLogo` — animated logo, takes `gridSize` and `cubeCount`

Helper types: `ButtonColor.Primary / .Secondary`, `ButtonState.Idle / .Loading / .Success`, `TextFieldColors.Primary`

### Compose preview

```kotlin
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
private fun MyScreenPreview() {
    SkillimaMentorsTheme {
        MyScreen(/* preview params */)
    }
}
```

---

## 11. Current Status & Changelog

### Status: Core Flow Complete — HomeScreen Missing

### Navigation Logic (from `Navigator.kt`)

```
loggedIn       → GuildScreen        (post-login home is the Guild screen)
firstTime      → OnboardingScreen   (first launch intro pager)
else           → SignupScreen        (returning user, not logged in)
```

### Implemented (as of 2026-04-10)

| Feature | Status | Notes |
|---|---|---|
| Core infrastructure | ✅ Done | `core/ui`, `core/models`, `core/navigation`, `core/supabase`, `core/datastore`, `core/utils` |
| Design system | ✅ Done | Violet-themed (`SkillimaMentorsTheme`); `SkillimaButton`, `SkillimaTextField`, `SkillimaLogo` |
| Auth — Login screen | ✅ Done | `screens/auth` — email/password |
| Auth — Signup screen | ✅ Done | `screens/auth` — email/password |
| Auth data repo | ✅ Done | `data/auth` — `AuthRepository` + `AuthRepositoryImpl`, error mapping |
| Onboarding intro | ✅ Done | `screens/onboarding` — `WelcomeRoute` (logo splash) + `OnBoardingRoute` (3-page pager) |
| Guild selection | ✅ Done | `screens/guild` — `GuildSelectionScreen` with search, skill grid, save to DB |
| Guild data repo | ✅ Done | `data/guild` — `GuildRepository`, RPC-based guild/skill fetch |
| Local DB (Room) | ✅ Done | `data/local` — UserEntity, UserSkillEntity, DAOs, migrations |
| User data repo | ✅ Done | `data/user` — `UserRepository` |
| Koin Navigation3 wiring | ✅ Done | All screens wired via DI modules using `activityRetainedScope { navigation<Destination> { ... } }` |
| MainViewModel | ✅ Done | Reads DataStore, controls splash screen visibility |
| Navigator | ✅ Done | Reactive back-stack; loggedIn→GuildScreen, firstTime→Onboarding, else→Signup |

### Not Yet Implemented

| Feature | Priority | Notes |
|---|---|---|
| HomeScreen | Next | Defined in `NavigationRoutes.kt` but no implementation; currently Navigator routes loggedIn users to GuildScreen instead |
| ForgetScreen | Next | Defined in `NavigationRoutes.kt` but no implementation |
| Dashboard (post-guild) | Future | Feed, projects, sessions — parity with Website dashboard |

### Known Issues

| Issue | File | Description |
|---|---|---|
| Dead code | `app/src/main/java/skillima/mentors/navigation/SkillimaNavHost.kt` | Entire file is commented out — this was the old Navigation approach. The new approach uses Koin `getEntryProvider()`. Can be deleted. |
| Nav routing inconsistency | `core/navigation/Navigator.kt` | `determineDestination` routes `loggedIn → GuildScreen` instead of `HomeScreen`. This is intentional for now (GuildScreen = home), but differs from Students Android which routes `loggedIn → HomeScreen`. |

---

## 12. Subagent Development Workflow

When developing new features or functions for this project, use the **subagent-driven-development** skill:

1. **Plan first** — Use `superpowers:writing-plans` or `superpowers:brainstorming` to create a plan
2. **Implement** — Dispatch an implementer subagent with full task context
3. **Spec review** — Dispatch a spec-compliance reviewer before moving on
4. **Quality review** — Dispatch a code-quality reviewer after spec passes
5. **Finish** — Use `superpowers:finishing-a-development-branch` when all tasks are done

Each implementer subagent should use **superpowers:test-driven-development** for all implementation work.

### Context to always provide subagents
- Full content of this CLAUDE.md
- The task spec in full
- Relevant existing files (don't make them search)
- The relevant module's `build.gradle.kts` and existing DI module file

---

## 12. Known Gotchas

### Supabase credentials are hardcoded in SupabaseConstants (not BuildConfig)
`core/supabase/src/main/java/skillima/mentors/supabase/SupabseConstants.kt` contains the URL and key as `const val`. These are the live project credentials. Do not create a second client or duplicate credentials.

**Note the filename typo**: `SupabseConstants.kt` (missing 'a') — do not rename it without updating all imports.

### minSdk is 28 for app; screens/auth uses minSdk 24
The `app/` module enforces minSdk 28. Feature modules may declare a lower minSdk in their own `build.gradle.kts` but the effective minSdk is the app's. Do not use APIs below API 28 without checks.

### Koin module must be in both the local module file AND DI.kt
If you declare `val myModule = module { ... }` in a feature module but forget to add it to `includes(...)` in `app/src/main/java/skillima/mentors/DI.kt`, Koin will throw `NoBeanDefFoundException` at runtime.

### Navigation 3 back stack uses `Destinations` interface, not `@Serializable` data classes
The `Navigator` uses `mutableStateListOf<Destinations>`. All destinations must implement `Destinations` from `core/navigation/NavigationRoutes.kt`. The `Screen.kt` sealed class in `app/navigation/` uses `@Serializable` and is a separate concept — both exist in the project. New destinations go into `NavigationRoutes.kt`.

### SupabaseError.error is a @StringRes Int
`SupabaseError` requires `val error: Int` annotated `@StringRes`. All custom error enums must implement this interface. Error strings live in `strings.xml`, not hardcoded. The ViewModel passes `response.exception.error` (a resource ID) to the UI, which calls `context.getString(errorResId)`.

### DI was migrated from Hilt to Koin
There are no Hilt annotations in this project. Do not add `@HiltViewModel`, `@Inject`, or any Hilt dependencies. Use Koin's `single { }`, `viewModel { }`, and `get()` exclusively.

### Koin Navigation 3 integration uses activityRetainedScope
The auth module registers routes using `activityRetainedScope { navigation<LoginScreen> { LoginRoute() } }` — this ties the scope to the Activity lifecycle. Use this pattern for screen-level ViewModel scoping.

### compileSdk syntax in feature modules
Feature module `build.gradle.kts` files use the DSL form `compileSdk { version = release(36) }`, not the simple `compileSdk = 36` form. Follow this pattern in new modules.
