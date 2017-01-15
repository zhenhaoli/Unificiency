## Getting Started ##

1. [Install NodeJS](https://nodejs.org/en/download/) if you don't have it already.

2. Adjust the `config.js` in the config folder to your database setting

3. The msp.sql in the db folder is the current database. You can easily import it using: `mysql -uroot msp < msp.sql`
  - Make sure you have a database named msp
  - if you want to use another user than root or root has password then use  `mysql -uuser -ppassword msp < msp.sql`
 
4. Run `npm install` to install dependencies
  - If you use a new dependency make sure it's saved into package.json by using ` npm install [package] --save` 
  
5. In the directory where `app.js` is located at run `node app.js` 
