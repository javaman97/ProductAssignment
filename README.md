
# SWIPE CATALOGUE 

# LISTING PRODUCTS SCREEN (FRAGMENT)

<img src="https://github.com/user-attachments/assets/34801971-c00e-45bc-9413-b7fc333e56bd" width="200" />
<img src="https://github.com/user-attachments/assets/bef2b896-1818-403c-8b9f-0968d885e3d5" width="200" />
<img src="https://github.com/user-attachments/assets/c5116211-5c2d-4f65-b158-5e133eac83c4" width="200" />
<img src="https://github.com/user-attachments/assets/5f015ea3-5bb2-4c0b-9b88-0b2f11c63274" width="200" />


# ADDING PRODUCT SCREEN (BOTTOM SHEET FRAGMENT)
<img src="https://github.com/user-attachments/assets/629ef6ac-0a50-4990-b9b1-2b58cd839b29" width="250" />
<img src= "https://github.com/user-attachments/assets/6033aaff-88b6-4bb6-b6a3-2bbda4f26dba" width ="250" />

### Adding Product Status (DIALOGs and Notification)
<img src="https://github.com/user-attachments/assets/6ca4c6e7-af62-4a3f-86b0-1926b32600af" width="200"  />
<img src="https://github.com/user-attachments/assets/8e3140a4-d67d-4f4f-87c5-c3926a0a4d73" width="200" />
<img src="https://github.com/user-attachments/assets/78b08838-9c2b-40a9-a756-d7716690de52" width="200" />



## Built With 🛠
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more..
sequentially emits values and completes normally or with an exception.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying database changes.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes. 
  - [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - Generates a binding class for each XML layout file present in that module and allows you to more easily write code that interacts with views.
- [Dependency Injection](https://developer.android.com/training/dependency-injection) - 
  - [KOIN](https://insert-koin.io/) - A framework to help you build any kind of Kotlin & Kotlin Multiplatform application.Fully made using Kotlin and easy and simple to use.
- [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- [Moshi](https://github.com/square/moshi) - A modern JSON library for Kotlin and Java.
- [Moshi Converter](https://github.com/square/retrofit/tree/master/retrofit-converters/moshi) - A Converter which uses Moshi for serialization to and from JSON.
- [Coil-kt](https://coil-kt.github.io/coil/) - An image loading library for Android backed by Kotlin Coroutines.
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.
- [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html) - For writing Gradle build scripts using Kotlin.

# Package Structure
    
    com.aman.swipeassignment    # Root Package
    .
    ├── data                # For data handling.   
    |   ├── api             # Retrofit API for remote end point.
    │   └── repository      # Single source of data.
    |
    ├── models               # Model classes
    |
    ├── di                  # Dependency Injection                  
    │   └── module          # DI Modules
    |
    ├── ui                  # Activity/View layer
    |   ├── adapter         # Adapter for RecyclerView 
    │   └── screens         # Listing and Add Product Screens
    ├── viewmodel           # ProductViewModel to manage data in lifecycle aware manner 
    ├── main                # Main Screen Activity
    └── utils               # Utility Classes / Kotlin extensions / Constants


## Architecture
This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.

![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)




