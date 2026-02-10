# F1Info Mobile App 🏎️📡  
**Android app for exploring Formula 1 data and viewing live driver telemetry-style information** (built in **Kotlin**).

> Repo: https://github.com/MaciejZiel/F1InfoMobileApp

---

## Why this project exists

**F1Info** is a “built for fun, built for learning” mobile app focused on **Formula 1 drivers and telemetry-like live data** — the kind of information fans want *during a session*: who’s fastest, what’s happening now, and how drivers compare.

This repository is written in **Kotlin** and uses a Gradle setup based on **Kotlin DSL** (`*.kts`).  

---

## What you can do (high-level)

> The exact screens/features depend on the current implementation, but the goal of the app is:

- Browse **drivers / teams**
- Observe **telemetry-style live metrics** per driver (e.g. speed / lap progress / session insights)
- Compare multiple drivers quickly (UI optimized for “race weekend” usage)
- Enjoy a smooth Android UI with clean navigation and responsive updates

---

## Tech (what’s in the repo)

- **Language:** Kotlin
- **Build system:** Gradle (Kotlin DSL)  
- **Platform:** Android (mobile)

---

## Quick start

### 1) Requirements
- Android Studio (latest stable)
- Android SDK + emulator (or physical device)

### 2) Run
1. Clone the repository:
   ```bash
   git clone https://github.com/MaciejZiel/F1InfoMobileApp.git
   cd F1InfoMobileApp
   ```
2. Open the project in **Android Studio**
3. Let Gradle sync finish
4. Run the `app` configuration on a device/emulator

---

## Configuration / API Keys (if applicable)

If the project uses any external APIs:
- Look for values in:
  - `local.properties`
  - `gradle.properties`
  - `app/src/main/...` config files
  - `.env`-style files (if present)

**Never commit real API keys** — use placeholders and document the setup here.

---

## Project structure (typical Android module layout)

```
F1InfoMobileApp/
├─ app/                     # main Android module
├─ gradle/                  # Gradle wrapper and configs
├─ build.gradle.kts         # root gradle config (Kotlin DSL)
├─ settings.gradle.kts      # modules
└─ ...
```

Inside `app/` you will typically find:
- `src/main/AndroidManifest.xml`
- `src/main/java/...` or `src/main/kotlin/...`
- UI + domain + data layers (depending on chosen architecture)

---

## Architecture (recommended way to reason about the code)

Even if your internal structure differs slightly, this app is easiest to understand as 3 layers:

### 1) UI layer
- Screens, navigation, view state
- Handles user interactions, delegates work to the domain layer

### 2) Domain layer
- Use-cases / business rules
- “What the app does” (e.g. fetch telemetry, format it, compare drivers)

### 3) Data layer
- API clients
- DTOs, mapping, repositories
- Optional caching (in-memory or persistent)

**Good rule of thumb:**  
UI does **not** talk to network directly → it talks to a **Repository / UseCase**.

---

## Quality checklist (what makes this repo “portfolio-ready”)

If you want this to look absolutely premium on GitHub:

- [ ] Add screenshots/GIFs to `/docs/` and embed below
- [ ] Add a release APK in GitHub Releases
- [ ] Add CI (GitHub Actions) for:
  - build + unit tests
  - lint / detekt / ktlint
- [ ] Add a simple “Architecture” diagram
- [ ] Add a “Data Source” section (what API provides telemetry)
- [ ] Add a “Roadmap” (3–10 bullets)

---

## Screenshots

> Add images under `docs/` and link them here.

Example:
```md
![Telemetry screen](docs/telemetry.png)
![Driver details](docs/driver_details.png)
```

---

## Roadmap / Ideas

- Session-aware UI (Practice / Quali / Race)
- Favorite drivers & pin them to top
- Offline cache for last known session data
- Better comparisons (delta, sector breakdown, trend charts)
- Home screen widgets (favorite driver status)

---

## Contributing

PRs are welcome. If you want to contribute:
1. Fork the repo
2. Create a feature branch
3. Open a PR with a clear description and screenshots (if UI changes)

---

## License

No license file detected in the repository root.  
If you want others to use it, add a `LICENSE` (e.g., MIT) and clarify assets/API usage.

---

## Author

Created by **Maciej Ziel**  
GitHub: https://github.com/MaciejZiel
