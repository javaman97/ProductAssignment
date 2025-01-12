# SWIPE Assignment (Product Management App)

This Android app allows users to manage products through two main screens: **Product Listing** and **Add Product**. The app fetches products from a remote API, displays them in a list, allows users to add new products.

The app is built with modern Android development practices, including **MVVM architecture**, **Retrofit for networking**, **Koin for dependency injection**, **Coroutines for background tasks**, and **Coil for image loading**.

## Features

### Product Listing Screen
- Displays a list of products fetched from a REST API.
- Users can:
  - **Search products** by name.
  - **View all products** in the list.
  - **Navigate to Add Product screen** using a button.
- **Image Handling**: Loads product images from URLs. If no image URL is available, a default image is shown.
- **Loading Indicator**: Displays a progress bar while products are being fetched from the server.

### Add Product Screen
- Allows users to add new products with the following features:
  - Select product type from a predefined list.
  - Enter product name, price, and tax rate using input fields.
  - **Image Upload**: Select an image (JPEG or PNG with 1:1 ratio).
- **Validation**: Ensures fields are correctly filled out before submission.
- **Progress Indicator**: Shows loading while uploading the product.
- **User Feedback**: Displays a success or failure message after submission, using a dialog and notification.

### Offline Functionality
- If there is no internet connection, products are saved locally in a database.
- Once an internet connection is restored, the app syncs the locally saved products with the server.

---

## Tech Stack

- **Architecture:** MVVM (Model-View-ViewModel)
- **Networking:**
  - **Retrofit** for REST API communication.
  - **Gson** for JSON serialization and deserialization.
  - **Moshi** for alternative JSON parsing (optional).
- **Dependency Injection:** **Koin** for dependency injection.
- **Coroutines:** **Kotlin Coroutines** for background tasks.
- **Image Loading:** **Coil** for image loading.
- **UI Components:** 
  - **Material Design Components** for modern UI.
  - **Jetpack Navigation Component** for handling screen navigation.

---

## Libraries Used

- **Jetpack Navigation Component:**
  - `androidx.navigation.runtime.ktx`
  - `androidx.navigation.fragment.ktx`
  - `androidx.navigation.ui.ktx`
  - `androidx.lifecycle.livedata.ktx`
  - `androidx.lifecycle.viewmodel.ktx`

- **Circular ImageView:**
  - `circleimageview`

- **Coroutines:**
  - `kotlinx.coroutines.core`
  - `kotlinx.coroutines.android`

- **Image Loading:**
  - `coil`

- **Dependency Injection (Koin):**
  - `koin.core`
  - `koin.android`

- **Networking:**
  - **Retrofit:**
    - `retrofit`
  - **Gson:**
    - `converter.gson`
  - **OkHttp:**
    - `okhttp.bom`
  - **Moshi:**
    - `moshi.kotlin`
    - `converter.moshi`

---

## API Endpoints

### Product Listing API
- **GET**: `https://app.getswipe.in/api/public/get`
- **Response:**
```json
[
  {
    "image": "https://vx-erp-product-images.s3.ap-south-1.amazonaws.com/9_1619635829_Farm_FreshToAvatar_Logo-01.png",
    "price": 1694.91,
    "product_name": "Testing app",
    "product_type": "Product",
    "tax": 18.0
  },
  {
    "image": "https://vx-erp-product-images.s3.ap-south-1.amazonaws.com/9_1619873597_WhatsApp_Image_2021-04-30_at_19.43.23.jpeg",
    "price": 84745.76,
    "product_name": "Testing Update",
    "product_type": "Service",
    "tax": 18.0
  }
]
```

Add Product API
POST: https://app.getswipe.in/api/public/add
Body Type: Form-data
Response:
```json
{
  "message": "Product added successfully!",
  "product_details": {
    "product_name": "New Product",
    "price": 2000,
    "tax": 18.0
  },
  "product_id": 2657,
  "success": true
}

```


### Explanation of Key Directories and Files:

- **data/**: Contains all the data-related components such as API services and local database (Room).
  - `ProductApi.kt`: Defines the Retrofit service for making API calls.
  - `ProductsRepository.kt`: The repository class that acts as a middleman between data sources (API/Room) and ViewModel.

- **di/**: Contains Koin dependency injection modules for setting up dependencies.
  - `ProductModule.kt`: Defines Koin modules for injecting dependencies like Retrofit, Room, and repositories.

- **ui/**: Contains the UI components, including fragments and view models.
  - `ProductListFragment.kt`: Displays the list of products and includes search functionality.
  - `AddProductFragment.kt`: Provides the form to add a new product.
  - `ProductAdapter.kt`: Adapter for displaying products in a `RecyclerView`.
  - `ProductsViewModel.kt`: ViewModel for managing the data for the product list.
  - `AddProductViewModel.kt`: ViewModel for managing the data for adding a new product.

- **utils/**: Contains utility classes like network-related functions.
  - `NetworkUtils.kt`: Contains functions for checking network connectivity.

- **ProductApp.kt**: The application class where Koin is initialized.

- **res/**: Contains XML layouts and resources for the app.
  - `fragment_product_list.xml`: Layout for displaying the list of products.
  - `fragment_add_product.xml`: Layout for adding a new product.


├── app/ │ ├── src/ │ │ ├── main/ │ │ │ ├── java/ │ │ │ │ └── com/ │ │ │ │ └── aman/ │ │ │ │ └── swipeassignment/ │ │ │ │ ├── data/ │ │ │ │ │ ├── api/ │ │ │ │ │ │ └── ProductApi.kt │ │ │ │ │ ├── database/ │ │ │ │ │ │ └── ProductDao.kt │ │ │ │ │ ├── repository/ │ │ │ │ │ │ └── ProductsRepository.kt │ │ │ │ ├── di/ │ │ │ │ │ └── ProductModule.kt │ │ │ │ ├── ui/ │ │ │ │ │ ├── fragment/ │ │ │ │ │ │ ├── ProductListFragment.kt │ │ │ │ │ │ └── AddProductFragment.kt │ │ │ │ │ ├── adapter/ │ │ │ │ │ │ └── ProductAdapter.kt │ │ │ │ │ └── viewmodel/ │ │ │ │ │ ├── ProductsViewModel.kt │ │ │ │ │ └── AddProductViewModel.kt │ │ │ │ ├── utils/ │ │ │ │ │ └── NetworkUtils.kt │ │ │ │ └── ProductApp.kt │ │ ├── res/ │ │ │ ├── layout/ │ │ │ │ ├── fragment_product_list.xml │ │ │ │ ├── fragment_add_product.xml │ │ │ ├── values/ │ │ │ │ ├── colors.xml │ │ │ │ ├── strings.xml │ │ │ └── drawable/ │ │ │ └── default_image.png └── build.gradle


