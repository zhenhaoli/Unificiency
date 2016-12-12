
## Defined Resources so far

Embedded tomcat uses the port 8080 by default <br />
So the base url is localhost:8080 <br />

GET `localhost:8080/buildings` 
returns a list of buildings including rooms of each building<br />

GET `http://localhost:8080/buildings?latitude=48.12893&longitude=11.6381313` 
returns the list of buildings ordered by the nearest distance from user <br />
