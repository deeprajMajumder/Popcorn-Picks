# Popcorn Picks- Android App

## Overview
This android project Movie Explorer app that allows users to browse a list of movies and view
detailed information about each movie. App uses Jetpack Compose for UI. The app utilises the latest trends
in Android development and incorporate modern design principles. The app follows the MVVM architecture pattern
and handle data and state management using Kotlin Flow.

![Sample GIF](https://github.com/deeprajMajumder/Popcorn-Picks/blob/master/PopcornPickDemo.mp4)


## Implementation Details

1. **Movie List Screen:** The app have a screen that displays a list of movies in a paginated manner.
    1. The list is fetched from the local database (using Room) and displayed initially.
    2. If the data is not available in the local database or there is more data to be fetched, the app fetches
       additional movie items from the first API using pagination and update the local database accordingly.
    3. Each movie item in the list displays the movie title, release year, and a poster image (thumbnail). 

2. **Movie Detail Screen:** 
    1. The app have a separate screen that displays detailed information about a selected movie.
    2. When a user clicks on a movie item from the list, it navigates to the Movie Detail screen and
    display relevant information like movie title, release date, overview, genres, runtime, and the full-size
    movie poster.
3. **Dark and Light Theme:** 
    1. The app supports both dark and light themes.
    2. The user can switch between the two themes within the app settings.

## Project Structure

### Code Components
- **Views:** Contains the MainActivity and MovieDetails Activity.
- **ViewModel:** Contains the stateflow, pagination and data fetching logic from repository.
- **Model:** Contains the Model class for API data.
- **Dependency Injecttion:** Contains App Module that provides object for Retrofit, AppData Base, SharedPreference.
- **DataSources:** Contains Single repository that fetches data from Local Data base and if not available then call Network repository which calls APIs.
- **WorkManager:** Contains a custom work manager that runs every 30 mins after app is created to keep the local database upto date for next 20 items to be paginated.

### Resources
- **Build.gradle:** Utilizes BuildConfig and local properties to secure API key in run time.

### Additional Notes
- The project contains a Grid View for movie thumbnails about all the latest movies, where data is loaded locally as first preference.

## How to Use

1. Clone or download the project.
2. Import it into Android Studio or your preferred IDE.
3. Run the app on an Android device or emulator.
4. Click designated UI elements to trigger icon changes using the implemented method.

## Contributions

Contributions and feedbacks to enhance the project or extend its functionality are welcome! Feel free to fork this repository, make changes, and create pull requests.

## References

- [API reference](https://api.themoviedb.org/3/trending/movie/)







