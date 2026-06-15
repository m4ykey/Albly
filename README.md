[![CI](https://github.com/Arvuno/albly/actions/workflows/ci.yml/badge.svg)](https://github.com/Arvuno/albly/actions)

## Project Setup
1. Clone repository and open in the latest version of Android Studio
2. Create ```local.properties``` file
3. Add your [Discogs](https://www.discogs.com/developers) key:
4. Add your [Genius](https://docs.genius.com/) key:
```
token=YOUR_DISCOGS_KEY
genius_token=YOUR_GENIUS_KEY
```

## Screenshots

## Tech Stack
- **Kotlin** — primary language for all modules
- **Jetpack Compose** — modern UI toolkit (Kotlin Compose plugin)
- **Gradle** — build system (Kotlin DSL)
  - Android Application & Library plugins
  - Kotlin Symbol Processing (KSP)
  - Kotlinx Serialization
  - Google Mobile Services & Crashlytics
- **Modules**
  - `:app` — application entry point
  - `:album` — album feature
  - `:artist` — artist feature
  - `:collection` — user collection
  - `:lyrics` — lyrics feature
  - `:network` — networking layer
  - `:rss` — RSS feed integration
  - `:search` — search feature

# License
```
Copyright (C) 2025 Michał F

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
```