## Getting Started

### Version Control

#### If you have the repo set up, make sure you always update your project! e.g by `git pull`

1. install git
2. clone this repo: `git clone https://github.com/zhenhaoli/Unificiency.git`
3. after changes push your code to this repo:
  1. `git add *`
  2. `git commit -m "some useful comments"`
  3. `git push origin master`


### Update Dependencies

When downloading the project, you might need to download some dependencies to keep your local project up to date. Use IDE e.g. Intellij or Android and build the project to download missing files.

  
### Initiate our database MySQL

You can install MySQL standalone, however, it's recommended to use XAMPP since it brings everything you need especially the GUI aka phpMyAdmin.

Run the sql script in `Unificiency/UnificiencyAPI/src/sql/init.sql` to create the database and user accounts to access it.
 
### Run Server using Spring Boot on Eclipse

1. Download latest Eclipse
2. In Eclipse -> Help -> Eclipse Marketplace -> Search for 'Spring' and download the Spring IDE
3. After installtion of the Spring IDE you can run application as Spring Boot Application in Eclipse (right click on project -> run as -> spring boot app)