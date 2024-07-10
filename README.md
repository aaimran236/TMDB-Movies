# TMDB Movies

TMDB Movies is an Android application that fetches data from The Movie Database (TMDB) API to display popular movies. Users can search for any movie and view its details. 

## Features

- Display popular movies on the home screen.
- Search for movies using the search functionality.
- View detailed information about a selected movie.
## Video
https://github.com/aaimran236/TMDB-Movies/assets/106193863/dd705d85-08c2-4460-91f3-b66f6e880748

## Screenshots

<img src="https://github.com/aaimran236/TMDB-Movies/assets/106193863/d8992cce-720d-4eb1-86c0-b1b200fbc643" width="200" height="445">
 &nbsp; &nbsp; &nbsp;
 <img src="https://github.com/aaimran236/TMDB-Movies/assets/106193863/5cb72fd4-b46b-416e-a300-c09cf524b4a8" width="200" height="445">
  &nbsp; &nbsp; &nbsp;
  <img src="https://github.com/aaimran236/TMDB-Movies/assets/106193863/86e82aa6-c87e-4b34-a598-0d97720881d3" width="200" height="445">
  &nbsp; &nbsp; &nbsp;
<img src="https://github.com/aaimran236/TMDB-Movies/assets/106193863/a79a7213-19f0-41de-879e-5545d1e22584" width="200" height="445">
  &nbsp; &nbsp; &nbsp;
<img src="https://github.com/aaimran236/TMDB-Movies/assets/106193863/156cbaf7-d665-4e3a-8573-5241470e3046" width="200" height="445">
  &nbsp; &nbsp; &nbsp;
<img src="https://github.com/aaimran236/TMDB-Movies/assets/106193863/72286d61-c9df-4cef-b33c-88612e07a14a" width="200" height="445">


## Installation

1. **Clone the repository:**
    ```bash
    git clone https://github.com/aaimran236/TMDB-Movies
    ```

2. **Open the project in Android Studio:**
    - Open Android Studio.
    - Click on `File > Open` and navigate to the cloned directory.

3. **Add your TMDB API Key:**
    - Obtain an API key from [The Movie Database](https://www.themoviedb.org/documentation/api).
    - Open `gradle.properties` and add your TMDB API key:
      ```properties
      TMDB_API_KEY="your_api_key_here"
      ```

4. **Build and run the project:**
    - Click on `Run > Run 'app'` or press `Shift + F10`.

## Usage

### Home Screen

- The home screen displays a list of popular movies in a `RecyclerView`.
- Each movie item shows the movie poster, title, and rating.

### Search Functionality

- Click on the search icon in the toolbar.
- Enter the keyword(s) to search for movies.
- The search results replace the popular movies in the `RecyclerView`.

### Movie Details

- Click on any movie item in the `RecyclerView` to view its details.
- The details screen shows the movie poster, title, rating bar, and an overview of the movie.

### Returning to Popular Movies

- After closing the search view, the home screen will revert to displaying the popular movies.

## Libraries and Dependencies

- [Retrofit](https://square.github.io/retrofit/) for network requests.
- [Glide](https://github.com/bumptech/glide) for image loading.
- [Gson](https://github.com/google/gson) for JSON parsing.
- [LiveData](https://developer.android.com/jetpack/androidx/releases/lifecycle) for LiveData.
- [ViewModel](https://developer.android.com/jetpack/androidx/releases/lifecycle) for ViewModel.

Add the necessary dependencies to your `build.gradle` file:

```gradle
dependencies {
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.github.bumptech.glide:glide:4.12.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'
    def lifecycle_version = "2.8.3"
    // ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version"
    // LiveData
    implementation "androidx.lifecycle:lifecycle-livedata:$lifecycle_version"
}
```

## Contributing

Contributions are welcome! Please fork this repository and submit a pull request for any improvements.

## License

This project is licensed under the MIT License - see the [LICENSE](https://opensource.org/license/mit) file for details.

## Acknowledgments
- [The Movie Database (TMDB)](https://www.themoviedb.org/) for providing the API.
- [Retrofit](https://github.com/square/retrofit) for network request handling.
- [Glide](https://github.com/bumptech/glide) for image loading.
- [Gson](https://github.com/google/gson) for JSON parsing.
---
