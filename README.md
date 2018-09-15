# Networking conference

The app can be run on Android devices running Android version 19 and above.

Use File->New->Import project to import this is in Android Studio. The default configuration should be able to install and run the app on any emulator/device connected to the computer.

## Architecture
- Simple Android app, data is fetched using a background service and stored in a local sqlite database. Once data is fetched from server, everything except the unfetched profile images, all the data is always available in the database for offline viewing. 
- The profile images are fetched and loaded only when displayed. 
- Dagger is used for dependency injection
- EventBus is used to pass filter, search events
- Hawk is used to store flags securely

## Components
- Splash screen - the first screen with the image as provided, checks whether to start the first time flow or go directly to the main activity
- LocationPermissionActivity - activity to request location permission, skipped for devices lower than Android version 23.
- StartUsingAppActivity - simple screen to move forward, stores the flag to determine if first flow is done.
- MainActivity - contains fragments and manges the menu options for search and filter
- AttendeeListFragment - contains the attendees list
- AttendeeDetailsFragment - contains attendee detail, navigated to when user clicks in AttendeeListFragment
- UpdateAttendeeListService - background service which fetches the list from server and stores in the database, launched as soon as the application is launched first time
- DataStoreHelper - helper to store the data in sqlite db
- DataFetchHelper - helper to fetch data from the sqlite db

## Third-party libaries used
- Calligraphy 3
- EventBus (GreenRobot)
- Hawk
- ReactiveX
- Dagger2
- Retrofit
- Gson
- Timber
- Ormlite
- Picasso
- RecyclerView

## Wish list
Sorry, did not get much time and have other committments, so have to put it to an end now.
- PromoView containing some profiles in which attendeeProviding matches attendeeLookingFor for our POV profile. Also considering the matching industry tags. The data is sorted and arranged in the database so should be a small task after this.
- Lazy loading for the list view. (to improve first time load)
