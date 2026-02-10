# RaceVision (F1InfoMobileApp)

Androidowa aplikacja F1 z danymi o wyścigach, klasyfikacjach i torach, plus mini‑gry. Projekt działa na architekturze Activity + Fragment, z Retrofitem do API i ViewBindingiem w większości ekranów.

**Funkcje**
- Aktualne informacje o ostatnim wyścigu sezonu (top 3, tor, data).
- Klasyfikacja kierowców i konstruktorów z odświeżaniem i cache.
- Kalendarz sezonu z przejściem do szczegółów toru.
- Szczegóły toru: opis, charakterystyka, ciekawostki, DRS, statystyki i grafika toru.
- Profil kierowcy: zdjęcie, punkty, podiumy sezonu, pozycja w mistrzostwach.
- Mini‑gry: test reakcji, rysowanie toru z symulacją przejazdu, quiz.
- Personalizacja: motyw zespołu, tryb ciemny, język aplikacji (EN/PL/ES), wybór sezonu.

**Źródła danych**
- OpenF1: `https://api.openf1.org/v1/` dla sezonów >= 2023 (sesje, wyniki, kierowcy, klasyfikacje).
- Jolpica/Ergast: `https://api.jolpi.ca/ergast/f1/` dla sezonów < 2023 (klasyfikacje, wyniki ostatniego wyścigu, lista wyścigów).
- Wikipedia API (thumbnail): `https://en.wikipedia.org/w/api.php` dla zdjęć kierowców z danych Ergast.

**Jak działa aplikacja**
- Start w `MainActivity` z dolną nawigacją i toolbar. Domyślnie ładuje `RaceInfoFragment` i ustawia kolorystykę na bazie wybranego zespołu. `app/src/main/java/com/example/f1info/MainActivity.kt`.
- Ustawienia języka inicjalizuje `F1InfoApp`, które ustawia locale na starcie. `app/src/main/java/com/example/f1info/F1InfoApp.kt`.
- `RaceInfoFragment` wybiera źródło danych wg sezonu i pokazuje top 3 z ostatniego wyścigu. `app/src/main/java/com/example/f1info/fragments/RaceInfoFragment.kt`.
- `DriverStandingsFragment` i `TeamStandingsFragment` ładują klasyfikacje, z cache w `SharedPreferences` i możliwością odświeżenia. `app/src/main/java/com/example/f1info/fragments/DriverStandingsFragment.kt`, `app/src/main/java/com/example/f1info/fragments/TeamStandingsFragment.kt`.
- `CalendarFragment` pobiera listę wyścigów sezonu; przy braku danych cofa się o 1 sezon. Kliknięcie wyścigu otwiera `CircuitDetailsActivity`. `app/src/main/java/com/example/f1info/fragments/CalendarFragment.kt`, `app/src/main/java/com/example/f1info/adapters/CalendarAdapter.kt`.
- `CircuitDetailsActivity` łączy zasoby tekstowe z `strings_circuits.xml` i obraz toru z `res/drawable`, a statystyki toru ma zakodowane w `getCircuitStats`. `app/src/main/java/com/example/f1info/CircuitDetailsActivity.kt`.
- `DriverDetailsActivity` pobiera podiumy sezonu (OpenF1 lub Ergast), przechowuje je w cache `F1_STATS` i ładuje zdjęcie przez Glide. `app/src/main/java/com/example/f1info/DriverDetailsActivity.kt`.
- Mini‑gry uruchamiane są z `GameActivity`. `app/src/main/java/com/example/f1info/GameActivity.kt`.

**Struktura projektu**
- `app/src/main/java/com/example/f1info` — aktywności, aplikacja i widoki customowe.
- `app/src/main/java/com/example/f1info/fragments` — główne ekrany aplikacji.
- `app/src/main/java/com/example/f1info/adapters` — adaptery RecyclerView.
- `app/src/main/java/com/example/f1info/api` — Retrofit klienci i serwisy API.
- `app/src/main/java/com/example/f1info/api/models` — modele odpowiedzi z API.
- `app/src/main/java/com/example/f1info/models` — modele aplikacyjne.
- `app/src/main/res/layout` — layouty ekranów i elementów list.
- `app/src/main/res/values` — podstawowe zasoby (strings, colors, themes).
- `app/src/main/res/values-pl`, `app/src/main/res/values-es` — lokalizacje PL/ES.
- `app/src/main/res/values/strings_circuits.xml` — opisy torów (EN), z odpowiednikami w PL/ES.
- `app/src/main/res/drawable` — ikony, grafiki torów i zasoby UI.
- `app/src/main/AndroidManifest.xml` — deklaracja aktywności i uprawnień.

**Ustawienia i cache**
- `SharedPreferences` `F1_PREFS`: `selected_season`, `favorite_team`, `dark_mode`, `app_language`.
- `SharedPreferences` `F1_CACHE`: cache klasyfikacji i zdjęć kierowców.
- `SharedPreferences` `F1_STATS`: cache podiumów sezonu.

**Wymagania**
- Android Studio (lub Gradle Wrapper).
- Android SDK z `compileSdk = 35`.
- JDK 17.

**Uruchomienie (CLI)**
1. Upewnij się, że `local.properties` wskazuje na Android SDK.
2. Zbuduj aplikację:
```powershell
.\gradlew.bat assembleDebug
```
3. (Opcjonalnie) uruchom na urządzeniu/emulatorze:
```powershell
.\gradlew.bat installDebug
```

**Testy**
- Przykładowe testy w `app/src/test` i `app/src/androidTest`. Nie ma testów domenowych.

**Użyte biblioteki**
- Retrofit + Gson, OkHttp, logging‑interceptor.
- Glide (obrazy).
- Material Components, RecyclerView, SwipeRefreshLayout, ViewPager2.
- MPAndroidChart (zależność dodana, obecnie nieużywana w kodzie).

**Uwagi**
- `SettingsFragment`, `LapAdapter`, `Result`, `Session` nie są podpięte do aktualnych ekranów.
- Dla sezonów >= 2023 aplikacja opiera się o OpenF1; dla starszych o Jolpica/Ergast.
- Aplikacja wymaga połączenia z internetem (`android.permission.INTERNET`).
