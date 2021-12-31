## JTTracker
### Open Source Java Time Tracker build in Spring Boot with Nuxt.js

Following project was created inorder to learn modern web-dev technologies. 
During a team-driven and company-observed universaty project, I learnt how web-apps work, but sadly only with dated tech like thymeleaf and jquery. Since I already heard about Vue.js, there was no hasitation left and JTTracker was born. 

The idea of the app came from my always haunting question about another app I used during that universaty-project: Toggl. 
I always kept asking myself and others "How hard can it be to build something like Toggl?" and as I started this project, it turns out:  Not that easy, as it looks. Especially when working alone and learning a new Tech.  

### Keep in mind, this project was build as a "**learning**" and "**portfolio**" project. I have no intention on copying Toggl or other commercial-products.

The whole project will be and stay Open-Source, since I want to learn and improve my skills threv community-feedback
Once bug-free and save, this project will be launched for testing purposes for some weeks or months. 

***

### To run JTTracker in a dev env. you need to have
    
* NodeJS-Version: 16.X
* OpenJDK-Version: 17.X
* Spring-Boot-Version: 2.6.X
* MariaDB-Version: latest (optional)
    
   
***  
    
### Folder-structure


* Frontend: ./frontend
* Backend: ./
    
***    
    
    
### How to start the app for dev.

1. Use the Terminal and type in "./mvnw spring-boot::run" to start the Sping-Server
2. Navigate with the Terminal to ./frontend and launch the frontend with "npm run dev"
    Additional information: If you want to use MariaDB instead of the H2 database, you must:
    1. replace the spring.profiles.active=<> 'dev' with 'test'. This entry is located in ./src/main/resources/application.properties
    2. have a local MariaDB database available, e.g. through xampp.

***
   
### Docker

The docker-compose file is functional, can build and host the application.

This 3 containers:
1. database: mariadb
2. nuxtjs: jttracker_nuxt
3. springboot: jttracker_springboot

With 2 additional volumes:
1. volume for the database
2. volume for the profile pictures

While docker is used all created app-data remains persistent

***
### How deploy via docker

In the root-directory run the following command in the console/terminal: docker-compose up
    
