# Catalog App

Android Product Catalog app built with `Clean Architecture`, `MVVM`, `Coroutines/Flow`, `Room`, `Dagger Hilt`, and `modularization`.

## Features

- Product list from Fake Store API
- Product detail with image, description, category, price, and rating
- Favorite and unfavorite product with Room
- Cart management
- Local search by title
- Category filtering
- Offline fallback from cached Room data
- User profile and order history
- Unit testing
- Native C++ security module via NDK

## Tech Stack

- Kotlin
- Jetpack Compose
- Clean Architecture
- MVVM Pattern
- Kotlin Coroutines and Flow
- Room Database
- Ktor Networking
- Dagger Hilt
- NDK (C++)

## Module Structure

- `:app` - app entry point
- `:core:common` - shared helpers
- `:core:domain` - entities, repository contract, use cases
- `:core:data` - repository implementation
- `:core:database` - Room database, DAO, entities
- `:core:network` - API service and network setup
- `:core:designsystem` - shared Compose UI components and theme
- `:core:security` - native security layer
- `:feature:catalog` - catalog feature UI, state, event, mapper, viewmodel
- `:feature:auth` - auth feature login

## Sample Account Login
username: johnd
password: m38rmF$

## Run

```bash
./gradlew testDebugUnitTest
```

Open with Android Studio, sync Gradle, then run the `app` module on emulator or device.
