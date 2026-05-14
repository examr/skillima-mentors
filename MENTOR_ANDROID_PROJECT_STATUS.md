# MENTOR_ANDROID_PROJECT_STATUS

This document is the authoritative project status file for the Mentor Android application. All AI agents must read this file before starting work and update it after any code, UI, architecture, API, database, dependency, navigation, or configuration change.

---

# 1. Project Overview

- **Project Name:** Skillima Mentors Android
- **Purpose:** Android app for mentor onboarding, profile completion, guild/skill selection, verification tracking, and mentor dashboard access.
- **High-Level Description:** The app authenticates mentors with Supabase Auth, stores local session/app flags with DataStore and Room, uses Navigation 3 with a custom `Navigator`, and shows different screens depending on onboarding, profile completeness, guild selection, and mentor verification state.
- **Current Development Stage:** Functional MVP with auth, onboarding, profile completion, guild selection, pending verification UI, and basic home flow.
- **Supported Platforms:** Android only
- **Primary Audience:** Mentors. Student flow is not implemented yet.

## Tech Stack Summary

- Kotlin
- Jetpack Compose + Material 3
- MVVM-style presentation layer
- Modularized Gradle project
- Koin 4 for DI
- Supabase Auth + PostgREST
- Room for local user cache
- DataStore Preferences for app/session flags
- Firebase Cloud Messaging
- Coroutines + Flow
- Navigation 3 (`androidx.navigation3`)

---

# 2. Current Project Status

- **Overall Completion:** ~68% complete
- **Current Focus:** Mentor session correctness, profile completeness gating, mentor verification flow, and project documentation

## Status Summary

| Area | Status | Notes |
| --- | --- | --- |
| Onboarding flow | ✅ Completed | First-time flow routes correctly |
| Email login/signup | ✅ Completed | Uses Supabase Auth |
| Local session persistence | ✅ Completed | DataStore + Room |
| Mentor profile completion screen | ✅ Completed | Required before home access |
| Guild and skill selection | ✅ Completed | Saves user skills and updates local flag |
| Pending verification screen | ✅ Completed | Home redirects to awaiting screen when status is `pending` |
| Home dashboard | 🚧 In Progress | Basic only; not a full approved-mentor dashboard |
| Rejected mentor flow | ❌ Pending | No distinct rejected-state UI yet |
| Approved mentor advanced dashboard | ❌ Pending | Home is still basic |
| Student flow | ❌ Pending | Architecture mentions it, app does not implement it |
| Deep linking | ❌ Pending | No implemented deep link routing |
| Notification tap navigation | ❌ Pending | FCM displays notifications only |

## Stable Modules

- `core:datastore`
- `core:navigation`
- `core:supabase`
- `data:local`
- `data:profile`
- `screens:auth`
- `screens:onboarding`
- `screens:guild`
- `screens:mentor-profile`

## Modules Under Development

- `screens:home`
- `app`
- `core:notifications`

## Blockers / Issues

- Rejected and approved verification states are not fully differentiated in UI logic.
- `Navigator.initialize()` is one-shot, so startup state must be fully correct before first render.
- No automated tests currently protect auth/profile/navigation regressions.

## Known Bugs / Gaps

- `HomeViewModel` treats any non-`pending` verification status as regular home success; rejected mentors are not handled specially.
- Logout currently clears login flag but does not yet fully clear all local user data and skills.
- `SupabaseConstants` contains hardcoded URL/key constants in source and also runtime values via `BuildConfig`; this should be rationalized.

## Technical Debt

- ⚠️ Duplicate/legacy assumptions around profile completeness existed and may still surface in future code if not reused via shared helper.
- ⚠️ Navigation and startup state are spread across `MainViewModel`, `Navigator`, auth routes, and local app flags.
- ⚠️ The `data:user` module exists in Gradle includes but is not part of the active documented flow.

---

# 3. Application Flow

## Current End-to-End Flow

```text
App Launch
   ↓
Splash / MainActivity observes MainViewModel
   ↓
Bootstrap session state
   ├── Sync profile completeness from local cached user
   └── Sync guild selection flag from remote user_skills if needed
   ↓
Navigator.initialize(AppDataConfig)
   ↓
Logged In?
   ├── No
   │    ├── First time → OnboardingScreen
   │    └── Returning user → LoginScreen
   └── Yes
        ↓
   Profile Complete?
        ├── No → MentorProfileScreen
        └── Yes
             ↓
        Guild Selected?
             ├── No → GuildScreen
             └── Yes → Home flow
                          ↓
                   Verification Status
                      ├── pending → Awaiting Verification Screen
                      └── non-pending → HomeScreen
```

## Splash Flow

1. `MainActivity` installs splash screen.
2. `MainViewModel` boots session state.
3. It synchronizes:
   - `isProfileComplete` from local cached `UserEntity`
   - `isGuildSelected` from remote `user_skills` if needed
4. `MainActivity` only initializes `Navigator` when bootstrap is complete.

## Authentication Flow

### Login

1. User submits email/password on `LoginScreen`.
2. `AuthViewmodel` calls `AuthRepository.login`.
3. Supabase signs the user in.
4. Profile is fetched from `profiles`.
5. User skills are fetched from `user_skills`.
6. Full user is saved locally in Room.
7. Profile completeness is now defined as all of these being non-blank:
   - `bio`
   - `githubUrl`
   - `linkedinUrl`
   - `xUrl`
8. Routing:
   - incomplete profile -> `MentorProfileScreen`
   - complete profile but no skills -> `GuildScreen`
   - complete profile and skills -> `HomeScreen`

### Sign Up

1. User registers with email/password/name.
2. App signs the user in immediately after signup.
3. Profile row is fetched.
4. Full user is saved locally in Room.
5. If required profile fields are missing, route to `MentorProfileScreen`.
6. Otherwise route to `GuildScreen`.

## Mentor Verification Flow

1. Mentor completes profile.
2. App updates `profiles`.
3. App upserts `mentor_profiles` with `verification_status = "pending"`.
4. Home fetch uses `data:profile` repository to combine local user + remote verification status.
5. If `verification_status == "pending"` -> show `MentorAwaitingScreen`.

## Pending Approval Flow

```text
Login / Restore Session
   ↓
Home requested
   ↓
ProfileRepository.getCurrentMentorProfile()
   ↓
Fetch mentor_profiles.verification_status
   ↓
pending ? AwaitingVerification : HomeSuccess
```

## Rejected Flow

- **Current Implementation:** Not implemented as a separate branch.
- **Actual Current Behavior:** Any non-`pending` status falls through to `HomeScreen`.
- **Required Future Work:** Distinguish `approved` from `rejected`, likely with a dedicated rejection/retry flow.

## Approved Mentor Flow

- **Current Implementation:** Non-`pending` verification status goes to basic `HomeScreen`.
- **Limitation:** There is no advanced approved-mentor dashboard yet.

## Student Flow

- **Current State:** Not implemented.
- **Notes:** Documentation and TODOs reference future student support, but current app behavior is mentor-only.

## Navigation Hierarchy

```text
OnboardingScreen
  └── WelcomeScreen

LoginScreen
  └── SignupScreen

MentorProfileScreen
  └── GuildScreen
       └── HomeScreen
            └── MentorAwaitingScreen (state within Home flow)
```

## Deep Linking Flow

- ❌ Not implemented.
- Notification tap deep linking is also not implemented.

## Session Handling

- Room caches the logged-in user.
- DataStore tracks:
  - `loggedIn`
  - `firstTime`
  - `isGuildSelected`
  - `isProfileComplete`
- Startup sync now corrects `isProfileComplete` from the cached user before navigation begins.

## Logout Flow

1. `HomeViewModel.logout()` attempts `supabaseClient.auth.signOut()`.
2. It sets `loggedIn = false`.
3. `HomeRoute` clears the navigation back stack on logout completion.
4. **Gap:** local Room user is not yet fully cleared.

---

# 4. Architecture Documentation

## Folder / Module Structure

```text
app/
build-logic/
core/
  datastore/
  models/
  navigation/
  notifications/
  supabase/
  ui/
  utils/
data/
  auth/
  guild/
  local/
  profile/
  user/   (included but not central to current flow)
screens/
  auth/
  guild/
  home/
  mentor-profile/
  onboarding/
```

## Architecture Style

- Modular Android app
- Presentation layer with ViewModels
- Repository-based data access
- Shared core modules for navigation, models, UI, datastore, utils, and backend constants

## State Management

- Screen state is modeled with sealed UI state types.
- ViewModels expose `StateFlow`.
- Navigation state is held in a custom singleton `Navigator`.
- Startup app state is produced by `MainViewModel`.

## Dependency Injection

- Koin 4
- Root module file: `app/src/main/java/skillima/mentors/DI.kt`
- Feature modules register both ViewModels and `navigation<Destination>` providers for Navigation 3.

## Data Layer

- `data:auth` handles login and signup.
- `data:guild` handles guild/skill fetch and persistence.
- `data:local` handles Room + DataStore wrappers.
- `data:profile` handles mentor profile/verification reads for home flow.

## Domain Layer

- No separate use-case module currently.
- Shared models live in `core:models`.
- Business rules are implemented mostly inside repositories and ViewModels.

## Presentation Layer

- Compose screens and routes live under `screens:*`.
- `Route` composables connect ViewModels, state collection, and navigation.

## Repository Pattern

- Repositories provide boundary between screen logic and backend/local data.
- Recent decision: profile-related reads for home should live in `data:profile`, not in `HomeViewModel`.

## UseCase Pattern

- ❌ Not implemented as a dedicated layer.
- If added later, auth bootstrap and profile-completeness evaluation are good candidates.

## UI State Handling

- Login and signup use dedicated UI state classes.
- Home uses:
  - `Loading`
  - `AwaitingVerification`
  - `Success`
  - `Error`
- Mentor profile uses:
  - `Idle`
  - `Loading`
  - `Success`
  - `Error`

## Error Handling

- Auth maps Supabase errors to typed `AuthError`.
- Profile repository returns typed `ProfileError`.
- Some screens still use generic `Error` UI without polished recovery.

## Offline Handling

- Local cached user supports startup/session continuity.
- Guild completion is reconciled from remote on startup.
- No full offline-first sync strategy exists.

## Caching Strategy

- **Room:** cached logged-in user
- **DataStore:** lightweight session/app flags
- **Remote truth:** Supabase `profiles`, `mentor_profiles`, `user_skills`

## Key Architecture Decisions

1. **Custom Navigator over Navigation Compose routes**
   - Chosen to use Navigation 3 and Koin entry providers with a mutable back stack.
2. **Profile completeness centralized**
   - Shared helper now defines profile completeness as all required mentor links + bio.
3. **Startup bootstrap before navigation**
   - Needed because `Navigator.initialize()` is one-time and cannot be safely corrected after first render.

---

# 5. Tech Stack & Dependencies

## Core Versions

| Item | Version |
| --- | --- |
| AGP | 8.13.0 |
| Kotlin | 2.2.21 |
| Compose BOM | 2026.03.01 |
| Supabase | 3.4.1 |
| Koin | 4.2.1 |
| Room | 2.8.4 |
| Firebase BOM | 34.12.0 |
| Navigation 3 | 1.1.0 |

## Important Dependencies

| Dependency | Purpose | Used In |
| --- | --- | --- |
| `androidx.compose.material3` | UI layer | Screen modules |
| `androidx.navigation3` | Navigation runtime/UI | `core:navigation`, screens, app |
| `io.insert-koin` | Dependency injection | Entire app |
| `io.github.jan-tennert.supabase:auth-kt` | Authentication | `data:auth`, app |
| `io.github.jan-tennert.supabase:postgrest-kt` | Table access / backend queries | `data:*`, `app`, `screens:mentor-profile` |
| `androidx.room` | Local user cache | `data:local` |
| `androidx.datastore` | Session/app flags | `core:datastore` |
| Firebase Messaging | Push notifications | `core:notifications`, app |

## Plugins / Build Setup

- Custom convention plugins under `build-logic`
- `skillima.android`
- `skillima.android.compose`
- `skillima.android.library`
- `skillima.android.library.compose`
- `skillima.android.koin`
- `skillima.android.room`
- `com.google.gms.google-services`

## Dependency Notes

- `postgrest-kt-android:3.4.1` uses request-builder signatures that differ from newer docs; use local compile truth.
- Compose compiler strong-skipping warnings are present but non-blocking.
- Some modules still use deprecated `kotlinOptions.jvmTarget` style.

---

# 6. Database & Backend Structure

## Backend

- **Backend Type:** Supabase
- **Used Services:** Auth, PostgREST
- **Potential Future Services:** Realtime, Storage

## Main Tables

### `profiles`

- **Purpose:** Main mentor public profile
- **Important Fields:**
  - `id`
  - `email`
  - `full_name` / name mapping
  - `avatar_url`
  - `bio`
  - `github_url`
  - `linkedin_url`
  - `x_url`
  - `fcm_token`
  - `created_at`
  - `updated_at`
- **Validation Rules:**
  - For app flow, `bio`, `github_url`, `linkedin_url`, and `x_url` are required for "profile complete"

### `mentor_profiles`

- **Purpose:** Mentor verification state
- **Important Fields:**
  - `user_id`
  - `verification_status`
- **Known Statuses:** `pending`, `approved`, `rejected`
- **Current App Logic:**
  - `pending` -> awaiting screen
  - any other value -> home success

### `guilds`

- **Purpose:** Available mentor guilds
- **Important Fields:**
  - `id`
  - `name`
  - `description`
  - `icon_url`

### `skills`

- **Purpose:** Skills within guilds
- **Important Fields:**
  - `id`
  - `name`
  - `guild_id`

### `user_skills`

- **Purpose:** Mentor-to-skill mapping
- **Important Fields:**
  - `user_id`
  - `skill_id`

## Relationships

- `profiles.id` -> `mentor_profiles.user_id`
- `profiles.id` -> `user_skills.user_id`
- `guilds.id` -> `skills.guild_id`

## Auth Flow

- Email/password with Supabase Auth
- Signup also signs user in immediately

## Storage Usage

- ❌ No active Supabase Storage implementation yet
- Planned for profile photo/documents

## Security / RLS

- Not documented in repo code
- Needs explicit backend rules documentation from Supabase project

---

# 7. Feature Completion Tracker

## Authentication

- ✅ Login  
  - **Files:** `data/auth/*`, `screens/auth/*`
  - **Remaining:** forgot password, social auth, stronger auth edge-case UX
- ✅ Register
  - **Files:** `data/auth/*`, `screens/auth/*`
- ❌ Google Sign-In
- ❌ Forgot Password

## Mentor Profile

- ✅ Complete profile screen
  - **Files:** `screens/mentor-profile/*`
- ✅ Required profile completeness gating
  - **Files:** `core/models/AuthUserExtensions.kt`, `data/auth/AuthRepositoryImpl.kt`, `app/MainViewModel.kt`
- ✅ Save bio/github/linkedin/x
- ✅ Upsert pending mentor verification row
- 🚧 Better validation/error UX

## Guild / Skills

- ✅ Fetch guilds
- ✅ Fetch skills
- ✅ Save selected skills
- ✅ Route to home after save
- 🚧 Search/pagination robustness

## Home / Verification

- ✅ Basic home screen
- ✅ Awaiting verification UI
- ✅ Verification status fetch via profile repository
- ❌ Rejected state screen
- ❌ Approved mentor advanced dashboard

## Notifications

- ✅ FCM service setup
- ✅ Runtime notification permission request
- ✅ Token local persistence
- ❌ Upload FCM token to Supabase profile
- ❌ Notification tap routing

## Startup / Session

- ✅ Splash + bootstrap state
- ✅ Local session restore
- ✅ Startup profile completeness sync
- ✅ Startup guild-selection sync
- ⚠️ Logout local cache cleanup still incomplete

---

# 8. UI/UX Documentation

## Screen List

- Onboarding
- Welcome
- Login
- Signup
- Mentor Profile Completion
- Guild Selection
- Home
- Awaiting Verification

## UI States

### Login

- `Idle`
- `Loading`
- `Success`
- `Error`

### Signup

- `Idle`
- `Loading`
- `Success`
- `Error`

### Mentor Profile

- `Idle`
- `Loading`
- `Success`
- `Error(message)`

### Home

- `Loading`
- `AwaitingVerification(name)`
- `Success(name, email)`
- `Error`

## Loading States

- Home uses `HomeShimmerSkeleton()`
- Auth screens use loading UI state
- Mentor profile save shows loading UI state

## Error States

- Auth maps resource-backed errors
- Home still shows minimal `Text("Error")`
- Mentor profile shows simple string error

## Empty States

- Not comprehensively implemented/documented

## Design System

- `core:ui` contains shared theme/components utilities
- Shimmer modifier exists in `core:ui`

## Typography / Colors / Animations

- Material 3 based
- Awaiting verification screen is described as cinematic/stylized
- No formal design token document yet

## Reusable Composables

- Theme components from `core:ui`
- Home shimmer

## Navigation Pattern

- Route composables own navigation reactions
- Screen composables remain mostly presentational

---

# 9. API & Networking Documentation

## Base / Environment

- Runtime Supabase values are provided through `BuildConfig` from `local.properties`
- `SUPABASE_URL`
- `SUPABASE_ANON_KEY`

## API Access Pattern

- Supabase PostgREST via:
  - `supabaseClient.from("table")`
  - `supabaseClient.postgrest["table"]`

## Auth Method

- Supabase email/password auth

## Serialization

- Kotlinx Serialization
- DTO example: `AuthUserDTO`

## Error Mapping

- Auth uses `mapAuthError`
- Profile uses `ProfileError`
- Some modules still return generic errors

## Retry Logic

- ❌ No explicit retry framework in current code

## Pagination

- Guild repository supports offset/limit patterns and RPC-based reads

## Example Query Patterns

```kotlin
supabaseClient.from(SupabaseConstants.USER_SKILLS)
    .select {
        filter { eq("user_id", userId) }
    }
```

```kotlin
supabaseClient.postgrest[SupabaseConstants.MENTOR_PROFILES].upsert(
    buildJsonObject {
        put("user_id", userId)
        put("verification_status", "pending")
    }
) {
    onConflict = "user_id"
}
```

---

# 10. State Management Documentation

- ViewModels use `MutableStateFlow` + exposed immutable flows.
- Input validation in auth is debounced with Flow operators.
- `MainViewModel` combines app data + bootstrap completion to decide when the UI may initialize navigation.
- `Navigator` holds mutable back stack state outside Compose screen ViewModels.
- Screen routes observe ViewModel state and trigger navigation side effects.

---

# 11. File & Folder Reference

```text
app/
  MainActivity.kt              -> app entry, splash, NavDisplay
  MainViewModel.kt             -> startup bootstrap and session sync
  DI.kt                        -> root Koin module aggregation

core/models/                   -> shared app models and profile completeness helper
core/navigation/               -> custom Navigator and destinations
core/supabase/                 -> Supabase constants and DI
core/datastore/                -> preference keys and helpers
core/notifications/            -> FCM service and token repo
core/ui/                       -> theme, shared UI, shimmer
core/utils/                    -> response wrapper, validators, misc helpers

data/auth/                     -> login/signup repositories and DTOs
data/local/                    -> Room entities, DAO, DataStore repositories
data/guild/                    -> guild/skill repositories
data/profile/                  -> mentor profile and verification reads

screens/auth/                  -> login/signup UI, routes, ViewModel, UI states
screens/mentor-profile/        -> profile completion UI and save flow
screens/guild/                 -> guild and skills selection flow
screens/home/                  -> home + awaiting verification
screens/onboarding/            -> first-time onboarding flow
```

---

# 12. Agent Coordination Section

## Agent Rules

- Read this file before editing code.
- Update this file after every completed task.
- Check this section before touching shared modules.

## Current Active Tasks

| Agent | Task | Files |
| --- | --- | --- |
| Unassigned | None recorded at the time of this update | N/A |

## Reserved / Sensitive Files

These files affect multiple flows and should be edited carefully:

- `app/src/main/java/skillima/mentors/MainViewModel.kt`
- `app/src/main/java/skillima/mentors/DI.kt`
- `core/navigation/src/main/java/skillima/mentors/navigation/Navigator.kt`
- `data/auth/src/main/java/skillima/data/auth/repository/AuthRepositoryImpl.kt`
- `screens/mentor-profile/src/main/java/skillima/screens/mentorprofile/MentorProfileViewModel.kt`
- `screens/home/src/main/java/skillima/screens/home/HomeViewModel.kt`

## Recently Modified Files

- `MENTOR_ANDROID_PROJECT_STATUS.md`
- `build-logic/convention/src/main/kotlin/AndroidApplicationFirebaseConventionPlugin.kt`
- `app/src/main/java/skillima/mentors/MainViewModel.kt`
- `app/src/main/java/skillima/mentors/DI.kt`
- `app/build.gradle.kts`
- `settings.gradle.kts`
- `core/models/src/main/java/skillima/mentors/module/AuthUserExtensions.kt`
- `core/models/src/main/java/skillima/mentors/module/AuthResult.kt`
- `data/auth/src/main/java/skillima/data/auth/repository/AuthRepositoryImpl.kt`
- `data/profile/src/main/java/skillima/data/profile/repository/ProfileRepository.kt`
- `data/profile/src/main/java/skillima/data/profile/repository/ProfileRepositoryImpl.kt`
- `data/profile/src/main/java/skillima/data/profile/model/CurrentMentorProfile.kt`
- `data/profile/src/main/java/skillima/data/profile/error/ProfileError.kt`
- `screens/home/src/main/java/skillima/screens/home/HomeViewModel.kt`
- `screens/home/src/main/java/skillima/screens/home/HomeDI.kt`
- `screens/mentor-profile/src/main/java/skillima/screens/mentorprofile/MentorProfileViewModel.kt`

## Conflicting Areas / Merge Warnings

- Startup flow changes can conflict between `MainViewModel` and `Navigator`.
- Auth/profile changes often affect:
  - `data:auth`
  - `data:local`
  - `screens:auth`
  - `screens:mentor-profile`
  - `screens:home`
- Any change to profile completeness rules must be documented here and kept centralized.

## Assumptions Currently in Use

- Mentor profile is considered complete only when `bio`, `githubUrl`, `linkedinUrl`, and `xUrl` are all non-blank.
- Rejected verification is not yet separately handled.

---

# 13. Change Log

## [2026-05-03 15:15 IST]

### Changes Made
- Audited the Firebase Android convention plugin referenced from `build-logic`.
- Confirmed the real convention plugin already exists in `build-logic/convention/src/main/kotlin/AndroidApplicationFirebaseConventionPlugin.kt`.
- Removed the stray duplicate copy from `build-logic/convention/bin/main` so there is a single authoritative plugin source.

### Files Modified
- `build-logic/convention/bin/main/AndroidApplicationFirebaseConventionPlugin.kt`
- `MENTOR_ANDROID_PROJECT_STATUS.md`

### Notes
- The plugin is already registered in `build-logic/convention/build.gradle.kts` as `skillima.firebase`.
- Current behavior of the plugin:
  - applies `com.google.gms.google-services`
  - applies `com.google.firebase.firebase-perf`
  - applies `com.google.firebase.crashlytics`
  - adds Firebase BOM, Analytics, Performance, and Crashlytics dependencies
  - disables Crashlytics mapping file upload for build types

## [2026-05-03 15:05 IST]

### Changes Made
- Created `MENTOR_ANDROID_PROJECT_STATUS.md` as the project single source of truth.
- Documented current architecture, module setup, feature status, backend usage, and agent coordination rules.
- Recorded recent mentor-profile routing and profile-completeness changes.

### Files Modified
- `MENTOR_ANDROID_PROJECT_STATUS.md`

### Notes
- This is the first authoritative version of the status file.
- Future agents must update this file after every task.

## [2026-05-03 14:55 IST]

### Changes Made
- Changed mentor profile completeness rule from "bio only" to all required fields:
  - bio
  - GitHub URL
  - LinkedIn URL
  - X URL
- Updated login/signup to save full user data and route incomplete mentors to `MentorProfileScreen`.
- Updated mentor profile save flow to require all fields and update the local cached user.
- Updated app startup bootstrap to sync `isProfileComplete` from the cached user before navigation initialization.

### Files Modified
- `core/models/src/main/java/skillima/mentors/module/AuthUserExtensions.kt`
- `core/models/src/main/java/skillima/mentors/module/AuthResult.kt`
- `data/auth/src/main/java/skillima/data/auth/repository/AuthRepositoryImpl.kt`
- `screens/mentor-profile/src/main/java/skillima/screens/mentorprofile/MentorProfileViewModel.kt`
- `app/src/main/java/skillima/mentors/MainViewModel.kt`

### Notes
- This prevents logged-in users with incomplete mentor profiles from incorrectly landing on Home.

## [2026-05-03 14:40 IST]

### Changes Made
- Registered `MentorProfileScreen` module in app DI and Gradle wiring.
- Fixed mentor-profile screen backend write calls to match installed Supabase PostgREST API usage.
- Verified mentor-profile module and app compile successfully.

### Files Modified
- `app/src/main/java/skillima/mentors/DI.kt`
- `app/build.gradle.kts`
- `settings.gradle.kts`
- `screens/mentor-profile/src/main/java/skillima/screens/mentorprofile/MentorProfileViewModel.kt`

### Notes
- Fix addressed runtime crash: `IllegalStateException: Unknown screen MentorProfileScreen`.

## [2026-05-03 14:20 IST]

### Changes Made
- Moved home-facing mentor profile lookup into `data:profile`.
- Added `ProfileRepository.getCurrentMentorProfile()`.
- Simplified `HomeViewModel` so it no longer fetches verification state directly.
- Added profile-specific error and current-profile model objects.

### Files Modified
- `data/profile/src/main/java/skillima/data/profile/repository/ProfileRepository.kt`
- `data/profile/src/main/java/skillima/data/profile/repository/ProfileRepositoryImpl.kt`
- `data/profile/src/main/java/skillima/data/profile/di/ProfileDataModule.kt`
- `data/profile/src/main/java/skillima/data/profile/model/CurrentMentorProfile.kt`
- `data/profile/src/main/java/skillima/data/profile/error/ProfileError.kt`
- `data/profile/src/main/res/values/strings.xml`
- `screens/home/src/main/java/skillima/screens/home/HomeViewModel.kt`
- `screens/home/src/main/java/skillima/screens/home/HomeDI.kt`
- `screens/home/build.gradle.kts`

### Notes
- Home module now depends on profile data abstraction instead of owning profile endpoint logic.

---

# 14. Pending Tasks & Roadmap

## High Priority

- Implement distinct rejected mentor flow
- Clear Room user + local skills on logout
- Upload FCM token to Supabase profile
- Improve `HomeUiState.Error` and mentor-profile error UX
- Add tests for auth/profile completeness/navigation bootstrap

## Medium Priority

- Build approved mentor dashboard
- Add profile photo upload
- Add notification tap routing
- Replace hardcoded Supabase constants with a cleaner environment strategy

## Low Priority

- Add deep links
- Introduce use-case layer
- Add student flow
- Refactor deprecated Gradle/Kotlin DSL usage

---

# 15. Setup & Run Instructions

## Required Setup

1. Copy `local.properties.example` to `local.properties` if needed.
2. Add:
   ```properties
   SUPABASE_URL=https://your-project.supabase.co
   SUPABASE_ANON_KEY=your-anon-key
   ```
3. Place `google-services.json` in `app/`.

## Common Commands

```bash
./gradlew assembleDebug
./gradlew :app:compileDebugKotlin
./gradlew :app:installDebug
```

## Recommended Verification Commands

```bash
./gradlew :core:models:compileDebugKotlin :data:auth:compileDebugKotlin :screens:mentor-profile:compileDebugKotlin :app:compileDebugKotlin
```

## Runtime Notes

- Android 13+ requests `POST_NOTIFICATIONS`
- App expects Supabase and Firebase setup to be valid

---

# 16. Testing Documentation

## Automated Tests

- No meaningful project-level unit/UI/integration test suite is currently documented.

## Manual Test Flows

1. Fresh install -> onboarding -> login/signup
2. Signup with incomplete profile -> verify route to `MentorProfileScreen`
3. Login with missing `bio/github/linkedin/x` -> verify route to `MentorProfileScreen`
4. Complete profile -> verify route to `GuildScreen`
5. Save skills -> verify route to `HomeScreen`
6. With `mentor_profiles.verification_status = pending` -> verify awaiting screen
7. Logout -> verify session exits cleanly

## Known Gaps

- No test coverage for rejected mentor flow because it does not exist yet
- No automated regression tests for startup bootstrap behavior
