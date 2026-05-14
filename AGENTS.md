# Skillima Mentors Android — Developer Reference

> **Purpose:** This file is the single source of truth for any AI or developer continuing work on this app.  
> It documents architecture, module structure, data flows, onboarding logic, and conventions.

---

## 1. Project Overview

**Skillima** is a mentor-matching Android app built with:

| Layer | Technology |
|---|---|
| UI | Jetpack Compose (Material 3) |
| Navigation | Navigation 3 (`androidx.navigation3`) |
| DI | Koin 4 |
| Backend | Supabase (Auth + PostgREST + Realtime) |
| Local storage | Room (user cache) + DataStore (preferences) |
| Push notifications | Firebase Cloud Messaging (FCM) |
| Build system | Gradle version catalogs + custom convention plugins |

---

## 2. Module Structure

```
root/
├── app/                          ← Application entry point
├── build-logic/                  ← Custom Gradle convention plugins
├── core/
│   ├── auth/                     ← (reserved, not used directly)
│   ├── datastore/                ← DataStore keys & flows (DatastoreHelper)
│   ├── models/                   ← All shared data/domain models (AuthUser, AuthResult, etc.)
│   ├── navigation/               ← Navigator + all Destination objects
│   ├── notifications/            ← Firebase FCM service + token repository
│   ├── supabase/                 ← SupabaseClient singleton + constants
│   ├── ui/                       ← Design system (theme, components, shimmer)
│   └── utils/                   ← Response<T> wrapper, extension functions
├── data/
│   ├── auth/                     ← AuthRepository + AuthRepositoryImpl + DTOs
│   ├── guild/                    ← GuildRepository (skill/guild fetching)
│   ├── local/                    ← Room DB, LocalAppDataRepository, UserLocalRepository
│   └── user/                     ← (user-specific data access if separate)
└── screens/
    ├── auth/                     ← Login + Signup screens + ViewModel
    ├── guild/                    ← Guild & skill selection screen + ViewModel
    ├── home/                     ← Home screen + awaiting-verification screen + ViewModel
    ├── mentor-profile/           ← Mentor profile form + ViewModel
    └── onboarding/               ← First-launch onboarding carousel
```

---

## 3. Core Models (`core:models` — `skillima.mentors.module`)

All shared data classes live here. **Never define models inside repository or ViewModel files.**

| Class | Purpose |
|---|---|
| `AuthUser` | Authenticated user profile (id, email, name, bio, githubUrl, linkedinUrl, xUrl, fcmToken, createdAt, updatedAt) |
| `AuthResult` | Result of login/signup: `user: AuthUser`, `hasSkills: Boolean`, `isProfileComplete: Boolean` |
| `UserData` | Input for login/signup: `email`, `password`, `name` |
| `GuildDto` | Supabase `guilds` table row |
| `GuildSkillDto` | Supabase `guild_skills` table row |
| `GuildsWithSkills` | Aggregated guild + its skills list |
| `SkillDto` | Supabase `skills` table row |

---

## 4. Supabase Tables

| Table | Key Columns | Usage |
|---|---|---|
| `profiles` | `id`, `name`, `email`, `bio`, `github_url`, `linkedin_url`, `x_url` | Mentor public profile |
| `mentor_profiles` | `user_id`, `verification_status` (`pending`/`approved`/`rejected`) | Verification state |
| `guilds` | `id`, `name`, `description`, `icon_url` | Available guilds |
| `skills` | `id`, `name`, `guild_id` | Skills within a guild |
| `user_skills` | `user_id`, `skill_id` | Many-to-many: mentor ↔ skills |

> **SupabaseConstants** in `core:supabase` holds all table/column name strings.

---

## 5. Local Storage

### DataStore (preferences — `core:datastore`)

Managed by `DatastoreHelper`. Keys and their semantics:

| Key | Type | Meaning |
|---|---|---|
| `IS_LOGGED_IN` | Boolean | User is authenticated |
| `IS_FIRST_TIME` | Boolean | True until onboarding is completed |
| `IS_GUILD_SELECTED` | Boolean | Guild + skills have been chosen |
| `IS_PROFILE_COMPLETE` | Boolean | Mentor has filled in bio (profile form submitted) |

Exposed as `Flow<T>` properties and `suspend fun set*()` functions via `LocalAppDataRepository`.

### Room Database (`data:local`)

- `UserLocalRepository` — stores & retrieves the logged-in `AuthUser` locally.
- `LocalAppDataRepository` — wraps DataStore preferences for read/write.
- `AppDataConfig` — aggregated data class combining all four DataStore booleans: `loggedIn`, `firstTime`, `isGuildSelected`, `isProfileComplete`.

---

## 6. Authentication & Onboarding Flow

### 6.1 First-time launch

```
App starts
  └─ MainActivity reads AppDataConfig from DataStore
       └─ Navigator.initialize(data: AppDataConfig)
            └─ determineDestination():
                 • loggedIn && isProfileComplete && isGuildSelected → HomeScreen
                 • loggedIn && !isProfileComplete                   → MentorProfileScreen
                 • loggedIn && !isGuildSelected                     → GuildScreen
                 • firstTime                                        → OnboardingScreen
                 • else                                             → LoginScreen
```

### 6.2 Sign-Up Flow

```
SignupScreen
  └─ AuthViewModel.signup(UserData)
       └─ AuthRepositoryImpl.signup()
            1. supabase.auth.signUpWith(Email) — creates user
            2. supabase.auth.signInWith(Email) — auto-login
            3. Fetch profiles row → check bio → isProfileComplete
            4. localAppDataRepository.setLoggedIn(true)
            5. localAppDataRepository.setProfileComplete(isProfileComplete)
            └─ Emits Response.Success(AuthResult)
  └─ SignupUiState.Success(user, hasSkills=false, isProfileComplete)
  └─ SignupRoute.onSuccess(isProfileComplete):
       • !isProfileComplete → navigate to MentorProfileScreen
       • isProfileComplete  → navigate to GuildScreen
```

### 6.3 Login Flow

```
LoginScreen
  └─ AuthViewModel.login(UserData)
       └─ AuthRepositoryImpl.login()
            1. supabase.auth.signInWith(Email)
            2. Fetch profiles row → check bio → isProfileComplete
            3. Fetch user_skills → count > 0 → hasSkills
            4. localAppDataRepository.setLoggedIn(true)
            5. localAppDataRepository.setProfileComplete(isProfileComplete)
            6. if hasSkills → localAppDataRepository.setGuildSelectionComplete(true)
            └─ Emits Response.Success(AuthResult)
  └─ LoginUiState.Success(user, hasSkills, isProfileComplete)
  └─ LoginRoute.onSuccess(hasSkills, isProfileComplete):
       • !isProfileComplete → navigate to MentorProfileScreen
       • !hasSkills         → navigate to GuildScreen
       • else               → navigate to HomeScreen
```

### 6.4 Mentor Profile Completion

```
MentorProfileScreen
  └─ MentorProfileViewModel.saveProfile(bio, githubUrl, linkedinUrl, xUrl)
       1. supabase.from("profiles").upsert(id, bio, github_url, linkedin_url, x_url)
       2. supabase.from("mentor_profiles").upsert(user_id, verification_status="pending")
       3. localAppDataRepository.setProfileComplete(true)
       └─ MentorProfileUiState.Success
  └─ MentorProfileRoute.onSuccess → navigate to GuildScreen
```

### 6.5 Guild & Skill Selection

```
GuildScreen
  └─ GuildViewModel fetches guilds + skills from Supabase
  └─ User selects a guild + skills
  └─ GuildViewModel.saveSkills(selectedSkillIds)
       1. Insert rows into user_skills table
       2. localAppDataRepository.setGuildSelectionComplete(true)
       └─ GuildUiState.Success
  └─ GuildRoute.onSuccess → navigate to HomeScreen
```

### 6.6 Home Screen

```
HomeRoute → HomeViewModel.loadUser()
  1. userLocalRepository.observeUser().first() — reads from local Room DB
  2. supabase.from("mentor_profiles").select where user_id=user.id
       → verificationStatus == "pending" → HomeUiState.AwaitingVerification
       → otherwise                       → HomeUiState.Success
  
Loading state → HomeShimmerSkeleton()
AwaitingVerification → MentorAwaitingScreen (cinematic verification pending UI)
Success → HomeScreen (profile header + Explore Guilds + Logout)
```

---

## 7. UI States

### Auth screens (`screens:auth — skillima.screens.auth.state`)

| State class | States |
|---|---|
| `LoginUiState` | `Idle`, `Loading`, `Success(user, hasSkills, isProfileComplete)`, `Error(messageResId)` |
| `SignupUiState` | `Idle`, `Loading`, `Success(user, hasSkills, isProfileComplete)`, `Error(messageResId)` |

### Home screen (`screens:home`)

| State class | States |
|---|---|
| `HomeUiState` | `Loading`, `Success(name, email)`, `AwaitingVerification(name)`, `Error` |
| `LogoutState` | `Idle`, `Loading`, `Done` |

### Mentor Profile (`screens:mentor-profile`)

| State class | States |
|---|---|
| `MentorProfileUiState` | `Idle`, `Loading`, `Success`, `Error(message)` |

---

## 8. Navigation (`core:navigation`)

**Navigator** is a Koin singleton. It manages a `mutableStateListOf<Destinations>` back stack.

```kotlin
Navigator.initialize(AppDataConfig) // Called once in MainActivity
Navigator.goTo(destination)          // Push
Navigator.replaceTop(destination)    // Replace current
Navigator.goBack()                   // Pop
Navigator.backStack.clear()          // Clear on logout/login success
```

### Registered Destinations

| Destination object | Screen |
|---|---|
| `OnboardingScreen` | First-time launch carousel |
| `LoginScreen` | Email/password login |
| `SignupScreen` | Mentor registration |
| `MentorProfileScreen` | Bio + links profile form |
| `GuildScreen` | Guild and skill selection |
| `HomeScreen` | Main mentor dashboard |

---

## 9. Push Notifications (`core:notifications`)

**Service:** `SkillimaFirebaseMessagingService` extends `FirebaseMessagingService`.

- `onNewToken(token)` → persists to `SharedPreferences` via `NotificationTokenRepositoryImpl`.
  - **TODO:** also upload token to Supabase `profiles` table so server-side targeting works.
- `onMessageReceived(message)` → shows a `NotificationCompat` notification.

**Koin binding:** `NotificationTokenRepository` → `NotificationTokenRepositoryImpl` (singleton).

**Permission:** `POST_NOTIFICATIONS` is requested at runtime in `MainActivity.onCreate()` on Android 13+.

**Setup required:** Drop `google-services.json` into `app/`. The `gms` plugin is already applied in `app/build.gradle.kts`.

---

## 10. Shimmer Loading

`Modifier.shimmerEffect()` is defined in `core:ui` (`skillma.core.ui.design.utils.ShimmerModifier`).  
Uses an animated `Brush.linearGradient` sweep. Import it anywhere in the UI layer.

`HomeShimmerSkeleton` in `screens:home/HomeScreen.kt` mirrors the real home layout with shimmer boxes.

---

## 11. Design & Naming Conventions

### File structure rules
- **Data classes / DTOs** → `core:models` (`skillima.mentors.module`) or `data:*/model`
- **UI state sealed classes** → `screens:*/state/` or root package of screen module, one class per file
- **Repository interfaces** → root of their `repository/` package, no data classes inline
- **DTOs used by a single module** → `data:*/model/` (e.g. `MentorProfileDto` in `data:auth/model`)

### Packages
| Layer | Package prefix |
|---|---|
| Core models | `skillima.mentors.module` |
| Auth data | `skillima.data.auth` |
| Local data | `skillima.data.local` |
| Auth screen | `skillima.screens.auth` |
| Home screen | `skillima.screens.home` |
| Notifications | `skillima.core.notifications` |

### Response wrapper (`core:utils`)

```kotlin
sealed class Response<out T> {
    object Loading : Response<Nothing>()
    data class Success<T>(val data: T) : Response<T>()
    data class Error(val error: /* AuthError or similar */) : Response<Nothing>()
}
```

---

## 12. Pending / Future Work

| # | Task | Notes |
|---|---|---|
| 1 | Upload FCM token to Supabase | Inside `NotificationTokenRepositoryImpl.onTokenRefreshed()` — call Supabase `profiles` upsert |
| 2 | Student role support | App currently targets mentors only. Student flow needs its own profile + guild logic |
| 3 | Profile photo upload | `profiles` table has `profile_photo` column — add Supabase Storage upload flow |
| 4 | Approved mentor dashboard | After verification is approved, show full mentor dashboard (projects, students, etc.) |
| 5 | Logout clears local DB | `logout()` in `HomeViewModel` should also call `userLocalRepository.clearUser()` |
| 6 | Deep links / notification tap | When FCM notification is tapped, navigate to the relevant screen |
| 7 | Error state UX | `HomeUiState.Error` and `LoginUiState.Error` need proper UI (retry button, snackbar) |
| 8 | Token refresh on app resume | Re-fetch `verification_status` on `onResume` or via Supabase Realtime subscription |

---

## 13. Build & Plugins

### Convention plugins (in `build-logic/`)

| Plugin alias | Class | What it does |
|---|---|---|
| `skillima.android` | `AndroidApplicationConventionPlugin` | Sets compileSdk, minSdk, Kotlin options |
| `skillima.android.compose` | `AndroidApplicationComposeConventionPlugin` | Enables Compose + BOM |
| `skillima.android.library` | `AndroidLibraryConventionPlugin` | Library variant of the above |
| `skillima.android.library.compose` | `AndroidLibraryComposeConventionPlugin` | Library + Compose |
| `skillima.android.koin` | `AndroidKoinConventionPlugin` | Adds all Koin dependencies |
| `skillima.firebase` | `AndroidApplicationFirebaseConventionPlugin` | Adds Firebase BOM + Crashlytics + Perf |
| `skillima.android.room` | `SkillimaAndroidRoomConventionPlugin` | Room + KSP |

### Key version catalog entries (`gradle/libs.versions.toml`)

| Alias | Version |
|---|---|
| `supabase` | 3.4.1 |
| `koin` | 4.2.1 |
| `composeBom` | 2026.03.01 |
| `firebaseBom` | 34.12.0 |
| `nav3Core` | 1.1.0 |
| `room` | 2.8.4 |

---

## 14. Environment Setup

1. Copy `local.properties.example` → `local.properties`
2. Add:
   ```
   SUPABASE_URL=https://your-project.supabase.co
   SUPABASE_ANON_KEY=your-anon-key
   ```
3. Drop `google-services.json` into `app/`
4. Run `./gradlew assembleDebug`
