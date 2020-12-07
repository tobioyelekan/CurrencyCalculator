# Currency Calculator

This is a simple currency converter app that showcases modern android development with MVVM Architecture

The app uses the following api:

* [fixer api](http://fixer.io) for retrieving list of countries, symbols and their current rates.
* [flagcdn](https://flagcdn.com) for getting a countries logo with the corresponding country code

## Introduction
The application is written entirely in Kotlin.

It uses the single activity acrhitecture pattern which is the recommended way of building modern android apps and it's entirely structured with
MVVM acrhitectire to ensure separation of concern and highly testable code.

With the help of [giuide to architecture](https://developer.android.com/jetpack/guide), we simply check if there's cache data, if there's is, we proceed to the main screen displaying the cached
and we make a network call to refresh the database, otherwise (if the cache is empty), we make a network call and save the response to the local database
and proceed to the main screen.

## Libraries used
* [Room](https://developer.android.com/topic/libraries/architecture/room): The Room persistence library provides an abstraction 
layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
* [Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started): a library that manages and eases fragment transactions
* [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager): WorkManager is an API that makes it easy to schedule deferrable, 
asynchronous tasks that are expected to run even if the app exits or the device restarts
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android): for dependency injection
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata): An observable data holder class consumed by the layout to display ui data
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): a class that housed UI-related data
* [Retrofit](https://square.github.io/retrofit/): for making network calls
* [Coroutine](https://developer.android.com/kotlin/coroutines): recommended way to make execute asynchronous code, main safe!, and eliminates call back hell code
* [Timber](): used for logging
* [Glide](https://github.com/bumptech/glide): a library for loading images
* [https://github.com/hdodenhof/CircleImageView](https://github.com/hdodenhof/CircleImageView): a library to display circle image view
* [PhilJay:MPAndroidChart](https://github.com/PhilJay/MPAndroidChart): library for displaying line chart

## Clone project
Use Android Studio 4.0 and above

* Register on [fixer api](http://fixer.io) to get your api key
* create a file called `secrets.properties` in your root project
* in your created file add this `API_KEY="your_key"`, build the project and you good to go!

**Note: you don't need an api key for flagcdn**

## ToDo
* UI testing with espresso