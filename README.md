# Comic Book Application based on Marvel API
This is a comic book application for Android that lets you search for comics based on the Marvel API - https://developer.marvel.com

I also implemented this application in iOS with 100% feature parity:
https://github.com/thomasquinto/comic-book-ios

## Screenshots
<img src="https://github.com/thomasquinto/comic-book-android/assets/217340/dffca6a9-27c3-455e-9389-559c37f99fd6" width="160"/>
<img src="https://github.com/thomasquinto/comic-book-android/assets/217340/cab1dc8a-34b4-45cb-bc6a-ad830c78fd29" width="160"/>
<img src="https://github.com/thomasquinto/comic-book-android/assets/217340/893dd248-018a-4024-b7b9-24814773858b" width="160"/>
<img src="https://github.com/thomasquinto/comic-book-android/assets/217340/7161f266-bb7a-4657-b3fb-0eab2162a6f6" width="160"/>
<img src="https://github.com/thomasquinto/comic-book-android/assets/217340/b06e9489-6058-4d4b-97c7-fc3744440a0e" width="160"/>

## Features
- Browse comics, characters, creators, series and more from the entire Marvel comics catalog
- Search by keyword
- Bookmark comics, characters, etc to your custom Favorites list

## Tools and Frameworks Used
- 100% Kotlin using the latest frameworks
  - Jetpack Compose for UI
  - Jetpack Navigation
  - Room for local storage
  - Retrofit for network API calls
  - Dagger Hilt for dependency injection
  - Coroutines for concurrency
 
## Implementation Details
- Based on a Clean Architecture design with these basic layers
  - Domain
  - Data
    - Local
    - Remote
  - Presentation
    - MVVM
- Supports portrait and landscape modes
- Supports dark and light modes
- Supports phone and tablet screen sizes
- Supports infinite scrolling and pull-to-refresh

## Setup
1. Clone the repository
2. Register to get your own public and private Marvel API keys here: https://developer.marvel.com
3. Copy and paste your keys to this file: [app/src/main/java/com/quinto/comicbook/data/remote/ComicBookApi.kt](https://github.com/thomasquinto/comic-book-android/blob/main/app/src/main/java/com/quinto/comicbook/data/remote/ComicBookApi.kt)
4. Build the application in your favorite IDE (i.e. Android Studio)
   
