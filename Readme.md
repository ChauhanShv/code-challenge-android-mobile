# Description

**Zeller** is the banking application allows its users to deposit and withdraw money from their bank account. Users can also view their past transactions.

Application has 4 major features as listed below:
1. Deposit Feature
2. Withdraw Feature
3. View Balance
4. Display Past Transactions:

# Getting Started
1. Clone the repository:
git clone https://github.com/ChauhanShv/code-challenge-android-mobile.git

2. Install/upgrade required dependencies as per your Android Studio IDE

# Architecture Choice
This project follows mvvm + clean approach. Though some improvements needs to be made to achieve 100% clean architecture.
Project contains 3 layers:
1. Data - Single source of truth and store data (It can be easily be connected to remote data source i.e APIs)
2. Domain - Contains Repo interface and Usecases handling business logic and act as a mediator between repository and presentation layer
3. Presentation- Contains Viewmodel holding data state and UI for user to interact with

# Decision made
MainViewModel:
1. Migration object class to class extending Android ViewModel in order to use repository and use-cases initiation
2. Using state flows to emit various data states
3. Using lifecycle scope to avoid memory leaks
4. Survive device orientation

## Repository and usecases
1. To Achieve abstraction and no needs to worry about how to access data 
2. Contain Business logic well - easily scalable into transforming data required buy UI layer
3. Each usecase has single responsibility hence adhering to SOLID principles

## Data Repository
1. Data is stored in repository instead of viewmodel as a single source of truth (e.g balance is moved to repository instead of keeping in viewmodel)
2. Update balance and observe real time when transaction made
3. Repository stayed in same releasable state but modified the way it is being accessed creating no regressions

# Further Improvements i would like to do:
1. Implement layer specific data models
2. Mappers (data to domain, domain to presentation) to map data between layers to achieve full abstraction
3. Better data store in repo using data class or similiar data structure and creating new resource called data provider which the connect with repository.
3. HILT/ Dagger to create DI modules for repository and usecases
   a. Which then easily do constructor injection in view model and usecases
   b. When project scaled and same usecases/ repository needs to be used in other features of the App.
4. Fragments inside main activity
   a.First fragment to do banking operation and display balance to user
   b.Second fragment to display past transactions using lazy column
5. Navigation graph for navigation between fragments
6. Shared view model between fragments
7. UI choice of composables instead of traditional xml
8. Support both light & Dark Mode
9. Further tests for edge cases

# Assumptions Made:
1. Forked Repository into my github
2. UI list to display a single textview is not a good approach but for the sake of just a task, so i did it that way, the right way is to use either recycler view or lazy column
3. Withdraw 0.1 not supported but can withdraw greater than that or was it user can only withdraw whole amount e.g 25.00 not 25.10?, Am i right here? 
4. To display past transaction on same page with button without creating new page


