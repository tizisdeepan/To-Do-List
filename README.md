# Simple To-Do List
The Simple To-Do List App is an Android application designed to help users efficiently manage their tasks. With a focus on utilizing SQLite and Room for local data storage, the app ensures a seamless experience through basic CRUD (Create, Read, Update, Delete) operations. Key features include:

1. Main Screen: Displays a list of to-do items, each with a title and a timestamp indicating when it was created. Users can easily add and delete items as needed.
2. Database: The sample app was developed in such a way to explore both SQLite and Room databases, to understand the complexities in SQLite and what Room offers in terms of performance and code maintainability. Robust local storage implemented with SQLite and Room, including tables for storing to-do items and handling all necessary CRUD operations asynchronously to maintain responsiveness.
3. Scaling Considerations: A method to prepopulate the database with 2,000 to-do items ensures the app performs well under a moderate load, allowing for performance testing and validation.
4. User Interface: A clean, simple, and responsive UI allows users to interact with the to-do list effortlessly, even with a large number of items. App is designed using Material Design 3 components library.
5. Testing: Includes unit tests for Business logics in ViewModel and Domain layer use cases with 100% coverage and SQLite / Room database operations Android inregration tests to ensure reliability and correctness.

# Installation
1. Clone the repository
   ```git clone https://github.com/tizisdeepan/To-Do-List.git```
2. Open the build.gradle from the cloned folder via Android Studio
3. Connect your test device or run the build in an emulator

Here are some useful Gradle/adb commands for executing this example:
1. `./gradlew clean build` - Build the entire example and execute unit and integration tests plus lint check.
2. `./gradlew installDebug` - Install the debug apk on the current connected device.
3. `./gradlew runUnitTests` - Execute domain and data layer tests (both unit and integration).

# Features
### Home Page
1. Task List: List all tasks in a paginated manner to reduce load on the IO thread
2. Ability to mark a task as Completed by checking the Checkbox
3. Ability to open the Task creation page
4. Ability to view the test options from the more menu on the home page toolbar
5. Ability to scroll through the list of tasks smoothly without any UI janks
6. Ability to go to the top of a page if you are in the middle or end of the list - this can alternatively be used to stress test the RecyclerView load performance
7. Ability to open the Task details page on tapping a task
8. Ability to choose between Room and SQLite databases using the toggle at the top of the page

### Task Creation Page
1. Ability to enter the title for the task to be created
2. Ability to create a task

### Task Details Page
1. Ability to view the title of the task
2. Ability to mark the task as Completed by checking the Checkbox
3. Ability to delete the task

### Test Options Menu
Available in the main page with 3 important features for testing and benchmarking the app,
1. Create 2000 tasks: Creates 2000 tasks in the current configured database table
2. Clear all tasks: Clears all tasks from the current configured database table
3. Monitor Performance: Displays benchmark data for database operations on both Room and SQLite databases

# App Screenshots
| Home  | Creation | Details | Test Options |
| ------------- | ------------- | ------------- | ------------- |
| ![](https://github.com/tizisdeepan/To-Do-List/blob/513389e3adefc5a7ccbf8649517406c5bf0ef18c/read_me_resources/main_page.png) | ![](https://github.com/tizisdeepan/To-Do-List/blob/513389e3adefc5a7ccbf8649517406c5bf0ef18c/read_me_resources/creation_page.png) | ![](https://github.com/tizisdeepan/To-Do-List/blob/513389e3adefc5a7ccbf8649517406c5bf0ef18c/read_me_resources/details_page.png) | ![](https://github.com/tizisdeepan/To-Do-List/blob/513389e3adefc5a7ccbf8649517406c5bf0ef18c/read_me_resources/options.png) |

# High Level Design
![](https://github.com/tizisdeepan/To-Do-List/blob/513389e3adefc5a7ccbf8649517406c5bf0ef18c/read_me_resources/high_level_diagram.png)

## Architecture
Model-View-ViewModel (MVVM) is an architectural pattern that separates the development of the graphical user interface (the view) from the business logic (the model) by introducing an intermediate component called the ViewModel.

1. Model: Represents the data and business logic of the application. It handles the data operations, such as fetching data from a database or a network, and provides a clean API for the ViewModel to interact with.
2. View: The UI layer, which displays the data and reacts to user interactions. It observes the ViewModel for data changes and updates the UI accordingly. The View should ideally contain no business logic.
3. ViewModel: Acts as a mediator between the View and the Model. It holds and processes the data required by the View, manages UI-related data in a lifecycle-conscious way, and handles the business logic. The ViewModel exposes data as observable LiveData or other observable types, allowing the View to react to changes automatically.

## Data Flow
1. Asynchronous Programming: Kotlin Coroutines (Structured Concurrency)
2. Reactive Programming: Kotlin Flows

## Database
### Room vs SQLite
Room is a wrapper, written on top of SQLite and is recommended by Google. Each database operation was performed for 2000 records over 10 times and the average was calculated. It can be observed that Room slightly performs better than SQLite.
| Operation | Room | SQLite |
| ------------- | ------------- | ------------- |
|Reads|8 ms|11 ms|
|Writes|45 ms|63 ms|
|Deletes|1 ms|3 ms|

## Paging 3
We are using Paging 3 for handling cache reads. Pagination is far more efficient than loading data as a whole from database when it comes to large amount of data.
### TaskPagingSource - Paging Keys
1. Load Size: 100 items per page
2. Page Key: Task ID (auto generated primary key), performance is more efficient than createdOn for our usecase and preserves insertion order when bulk insertion in Room
3. Previous Key, Next Key: Page keys to go backward and forward, and to calculate the Refresh Key
4. Refresh Key: To identify where the user is in the RecyclerView, this is calculated based on the Page's anchor position

## Domain Layer
1. FetchTasksUseCase: Fetches paginated Tasks from either SQLite database or Room database depending on the current Database Strategy
2. FetchTaskByIdUseCase: Fetches a single Task for a given task ID from either SQLite database or Room database depending on the current Database Strategy
3. UpdateTaskUseCase: Updates the given Task with new changes on either SQLite database or Room database depending on the current Database Strategy
4. DeleteTaskByIdUseCase: Deletes a single Task for a given task ID from either SQLite database or Room database depending on the current Database Strategy
5. CreateTaskUseCase: Creates a single Task for a given task title in either SQLite database or Room database depending on the current Database Strategy
6. ClearAllTasksUseCase: For testing purposes, clears all the cached tasks from either SQLite database or Room database depending on the current Database Strategy
7. CreateTasksForTestingUseCase: For testing purposes, creates 2000 tasks in either SQLite database or Room database depending on the current Database Strategy
8. CalculateDatabasePerformanceUseCase: Used for benchmarking database operations

## Repository
1. Task Repository: Acts as a controller that decides on what caching strategy to used based on the CurrentDatabasStrategy.kt object value
2. Design Pattern: Strategy


![](https://github.com/tizisdeepan/To-Do-List/blob/93ae8d2244d23db495db458196dc9e0cad6df6ba/read_me_resources/repository.png)

## Unit Testing
1. JUnit4: Local unit tests
2. Jupiter: Test instances
3. Turbine: Testing Kotlin flows
4. Paging-testing: For testing Paging Data, specifically to convert paging source to snapshot list for assertions

### Coverage
1. 33% Classes covered
2. 17% Lines covered

| Local Unit Tests  | Android Integration Tests |
| ------------- | ------------- |
|![](https://github.com/tizisdeepan/To-Do-List/blob/739a9666344bd2084d3b9e3b2b170164af8ba6d3/read_me_resources/unit_tests.png)|![](https://github.com/tizisdeepan/To-Do-List/blob/739a9666344bd2084d3b9e3b2b170164af8ba6d3/read_me_resources/integration_tests.png)|

# Low Level Design
![](https://github.com/tizisdeepan/To-Do-List/blob/523fd6aa1e5d080e4e9a2f374eb9ae9751f72943/read_me_resources/low_level_diagram.png)

# Continuous Integration
CI is achieved with the help of Github actions for this project,
1. Run Unit Tests
2. Generate Debug APK
3. Publish Debug APK as an artifact

![](https://github.com/tizisdeepan/To-Do-List/blob/23ac1911ae61dd9ad87ce4254a98cd86a58b1e5f/read_me_resources/actions.png)


Please contact me via tizisdeepan@gmail.com if you have any questions
