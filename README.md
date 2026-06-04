[![CI](https://github.com/Arvuno/albly/actions/workflows/ci.yml/badge.svg)](https://github.com/Arvuno/albly/actions)

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
