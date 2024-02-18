# LibraryApp
Small project for object-oriented technologies 23/24 course at AGH.

## Technologies used: 
* JavaFX
* Spring Boot

# Project features 
* Java programming language 
* Project builder: gradle
* Based on Spring
* Standalone application

# Project requirements 
* New user input: first name, last name and email (data correctness)
* Recommended books
* View book information, including cover
* Various permissions
* System sends emails with notifications

# Books Dataset
* https://www.kaggle.com/datasets/dylanjcastillo/7k-books-with-metadata

# Database scheme
![image](https://github.com/dominiks01/LibraryApp/assets/114943272/5ab56bbc-56a1-461e-b50e-a4664e20dcb1)


# Usage 
* Windows
```
./gradlew run
```

* Linux
  You have to link javafx in `build.gradle`
```
repositories {
  mavenCentral()
  flatDir {
    dirs 'path/to/lib/openjfx-21.0.2_linux-x64_bin-sdk/javafx-sdk-21.0.2/lib'	
  }	
}
```
```
./gradlew run
```
<br />


![image](https://github.com/dominiks01/LibraryApp/assets/114943272/d305b82e-8c81-4517-82c7-a3f840880eaa) <br />
![image](https://github.com/dominiks01/LibraryApp/assets/114943272/4d447d48-ddde-4d0d-bb75-f103c0cfe74d) <br />
![image](https://github.com/dominiks01/LibraryApp/assets/114943272/4dcaf8f4-0b6a-4025-8bb5-f717c7d88817) <br />
![image](https://github.com/dominiks01/LibraryApp/assets/114943272/be90dd49-88db-431d-ae5d-72b93724cb22) <br />
![image](https://github.com/dominiks01/LibraryApp/assets/114943272/a714c3cc-36fd-4e8a-9c07-6059ff7211e1) <br />
![image](https://github.com/dominiks01/LibraryApp/assets/114943272/dfb2ccc2-66b9-461d-93a8-a82f45503c0f) <br />
![image](https://github.com/dominiks01/LibraryApp/assets/114943272/5be79f00-495b-4af9-9fc6-dd3452448d8e) <br />


