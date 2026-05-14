# Skillima Mentors Android — Design System

> **Claude instruction:** Read this file before writing or modifying any UI code in the Mentors Android project.

---

## 1. Theme Wrapper

Always wrap every screen and preview in `SkillimaMentorsTheme`:

```kotlin
import skillma.core.ui.theme.SkillimaMentorsTheme

SkillimaMentorsTheme {
    // your content
}
```

- **Dark-only** — there is no light theme. Never add a light color scheme.
- Defined in `core/ui/src/main/java/skillma/core/ui/theme/Theme.kt`

---

## 2. Shape Language — Sharp Corners

**All UI elements use sharp / near-sharp corners. This is a core brand rule.**

- Standard corner radius: **`RoundedCornerShape(2.dp)`** (nearly square)
- Do NOT use `CircleShape`, `RoundedCornerShape(8.dp)`, `RoundedCornerShape(12.dp)`, or higher values
- Do NOT use `MaterialTheme.shapes.*` — Material defaults are too rounded
- Apply explicitly on every `Button`, `TextField`, `Card`, `Surface`, `OutlinedTextField`, chips, bottom sheets, dialogs, and any container with a visible background

```kotlin
// Correct
shape = RoundedCornerShape(2.dp)

// Wrong — never use these
shape = RoundedCornerShape(12.dp)
shape = CircleShape
shape = MaterialTheme.shapes.medium
```

---

## 3. Color Palette

Source: `core/ui/src/main/java/skillma/core/ui/theme/Color.kt`
Import prefix: `import skillma.core.ui.theme.*`

### Violet (Primary / Brand)

| Token | Hex | Use |
|---|---|---|
| `Violet500` | `#6312B2` | Primary actions, active states, brand accent |
| `Violet700` | `#460D7E` | Primary containers |
| `Violet300` | `#9660CB` | Inverse primary |
| `Violet400` | `#8241C1` | Hover / pressed states |

### Neutral Blacks (Backgrounds & Surfaces)

| Token | Hex | Use |
|---|---|---|
| `NeutralBlack13` | `#020202` | App background (darkest) |
| `NeutralBlack12` | `#161616` | TextField container, secondary button container |
| `NeutralBlack11` | `#202020` | Cards, surfaces, tertiary button container |
| `NeutralBlack10` | `#282828` | Elevated surfaces |
| `NeutralBlack9` | `#464646` | Secondary text, hint text, icons, `surfaceVariant` |
| `NeutralBlack8` | `#5B5B5B` | Outline / dividers |

### Neutral Whites (Text & Foreground)

| Token | Hex | Use |
|---|---|---|
| `NeutralWhite1` | `#FFFFFF` | Primary text on dark surfaces |
| `NeutralWhite4` | `#FCFFFE` | Content text in inputs and secondary buttons |
| `NeutralWhite5` | `#FBFFFD` | Loading indicators on dark backgrounds |
| `NeutralWhite10` | `#707372` | Disabled text and container fallback |

### Supporting Palettes

| Token | Hex | Use |
|---|---|---|
| `Blue300` | `#8286E9` | Secondary accent (`MaterialTheme.colorScheme.secondary`) |
| `Green300` | `#54FF84` | Tertiary / success highlight |
| `Green500` | `#00FF48` | Success state indicators |
| `Orange700` | `#B5610C` | Error state (`MaterialTheme.colorScheme.error`) |

### Color Scheme Mappings

```
primary              = Violet500
onPrimary            = NeutralBlack13
primaryContainer     = Violet700
onPrimaryContainer   = NeutralWhite1
secondary            = Blue300
secondaryContainer   = Blue700
tertiary             = Green300
background           = NeutralBlack13
onBackground         = NeutralWhite1
surface              = NeutralBlack11
onSurface            = NeutralWhite1
surfaceVariant       = NeutralBlack9
onSurfaceVariant     = NeutralWhite1
error                = Orange700
onError              = NeutralWhite1
outline              = NeutralBlack8
```

**Always use `MaterialTheme.colorScheme.*` tokens in composables** — never hardcode hex values or import palette tokens directly into UI code.

---

## 4. Typography

Source: `core/ui/src/main/java/skillma/core/ui/theme/Type.kt`
Font family: `displayFontFamily` (custom fonts from `core/ui/res/font/`)

All text uses this single font family across all weights. Always use `MaterialTheme.typography.*` roles:

| Role | Size | Weight | Use |
|---|---|---|---|
| `displayLarge` | 57sp | Black | Hero / splash only |
| `displayMedium` | 45sp | Bold | Major landing headings |
| `displaySmall` | 36sp | SemiBold | Section headings |
| `headlineLarge` | 32sp | Bold | Screen titles |
| `headlineMedium` | 28sp | SemiBold | Card headings, subtitles |
| `headlineSmall` | 24sp | Medium | Sub-section labels |
| `titleLarge` | 22sp | Medium | App bars |
| `titleMedium` | 16sp | Medium | List item titles |
| `titleSmall` | 14sp | Medium | Metadata, tags |
| `bodyLarge` | 16sp | Medium | Button labels, primary copy |
| `bodyMedium` | 14sp | Normal | Secondary body copy |
| `bodySmall` | 12sp | Normal | Captions, helper text |
| `labelLarge` | 14sp | Medium | Prominent labels |
| `labelMedium` | 12sp | Medium | Badges, chips |
| `labelSmall` | 11sp | Medium | Fine print |

---

## 5. Component Library

All components live in `core/ui/src/main/java/skillma/core/ui/design/`.
Import prefix: `import skillma.core.ui.design.*`

### Button

```kotlin
import skillma.core.ui.design.button.Button
import skillma.core.ui.design.utils.ButtonColor
import skillma.core.ui.design.utils.ButtonState

Button(
    onClick = { },
    state = ButtonState.Idle,       // Idle | Loading | Success
    colors = ButtonColor.Primary,   // Primary | Secondary | Tertiary
    enabled = true
) {
    Text("Label", style = MaterialTheme.typography.bodyLarge)
}
```

**Button color variants:**

| Variant | Container | Content | Use |
|---|---|---|---|
| `Primary` | `Violet500` | `NeutralBlack13` | Main CTA |
| `Secondary` | `NeutralBlack12` | `NeutralWhite4` | Secondary actions |
| `Tertiary` | `NeutralBlack11` | `NeutralWhite4` | Ghost / subtle actions |

- Shape is `RoundedCornerShape(2.dp)` — hardcoded in the component, do not override
- `SkillimaButton` is deprecated — use `Button` directly

### TextField

```kotlin
import skillma.core.ui.design.input.SkillimaTextField
import skillma.core.ui.design.utils.TextFieldColors
import skillma.core.ui.design.utils.TextFieldState

SkillimaTextField(
    value = text,
    onValueChange = { text = it },
    hintValue = "Placeholder",
    state = TextFieldState.Idle,        // Idle | Loading | Error
    color = TextFieldColors.Primary,
    navigationIcon = { /* optional leading icon */ },
    trailingIcon = { /* optional trailing icon */ }
)
```

### Password TextField

```kotlin
import skillma.core.ui.design.input.SkillimaPasswordTextField

SkillimaPasswordTextField(
    value = password,
    onValueChange = { password = it },
    hintValue = "Password",
    state = TextFieldState.Idle,
    color = TextFieldColors.Primary
)
```

**TextField color (Primary only):**

| Role | Token |
|---|---|
| Container | `NeutralBlack12` |
| Text | `NeutralWhite4` |
| Hint | `NeutralBlack9` |
| No underline/indicator | `Color.Transparent` |

Shape is `RoundedCornerShape(2.dp)` — do not override unless given explicit instruction.

### Logo

```kotlin
import skillma.core.ui.design.logo.AnimatedLogo
import skillma.core.ui.design.logo.Logo

AnimatedLogo()   // Use on splash / onboarding
Logo()           // Static variant
```

---

## 6. Spacing & Layout

Use Material3's standard spacing scale via `Dp` literals. Common values used in this project:

| Purpose | Value |
|---|---|
| Screen horizontal padding | `24.dp` |
| Section vertical gap | `16.dp` |
| Component internal padding | `8.dp` – `12.dp` |
| Icon size (standard) | `24.dp` |
| Icon size (small) | `16.dp` |

Use `Arrangement.spacedBy(...)` for consistent gaps in Column/Row — do not use manual padding between siblings.

---

## 7. Icons

Use `painterResource(R.drawable.ic_*)` with `Icon()`. Always pass a `tint` — default to `MaterialTheme.colorScheme.onSurface` unless context requires otherwise (e.g., hint-colored icons use `color.hintColor` = `NeutralBlack9`).

---

## 8. Do / Don't Summary

| Do | Don't |
|---|---|
| `RoundedCornerShape(2.dp)` on all containers | Use `CircleShape` or any radius > 4.dp |
| `SkillimaMentorsTheme { }` in every Preview | Skip the theme wrapper |
| `MaterialTheme.colorScheme.*` in composables | Hardcode `Color(0xFF...)` in UI code |
| `MaterialTheme.typography.*` roles | Set raw `fontSize` / `fontFamily` inline |
| `Button`, `SkillimaTextField` from `core/ui` | Use raw Material3 `Button` / `TextField` directly |
| Dark backgrounds: `NeutralBlack11` (surface), `NeutralBlack13` (bg) | Use white or light backgrounds |
| Single font family (`displayFontFamily`) for all text | Import or use any other font |
