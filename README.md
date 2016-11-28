# Unificiency
Making your life at uni better

## Getting Started

### Version Control

1. install git
2. clone this repo: `git clone https://github.com/zhenhaoli/Unificiency.git`
3. after changes push your code to this repo:
  1. `git add *`
  2. `git commit -m "some useful comments"`
  3. `git push origin master`
  
### Initiate MySQL

You can install MySQL standalone, however, it's recommended to use XAMPP since it brings everything you need especially the GUI aka phpMyAdmin.

Run the sql script in `Unificiency/UnificiencyAPI/src/sql/init.sql` to create the database and user accounts to access it.
 
### Spring Boot on Eclipse

1. Download latest Eclipse
2. In Eclipse -> Help -> Eclipse Marketplace -> Search for 'Spring' and download the Spring IDE
3. After installtion of the Spring IDE you can run application as Spring Boot Application in Eclipse (right click on project -> run as -> spring boot app)

## Defined Resources so far

Embedded tomcat uses the port 8080 by default <br />
So the base url is localhost:8080 <br />

GET `localhost:8080/buildings` returns a list of buildings including rooms of each building<br />
GET `http://localhost:8080/buildings?latitude=48.12893&longitude=11.6381313` returns the list of buildings ordered by the nearest distance from user <br />

