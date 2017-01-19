var unirest = require('unirest');

var groups = [169, 171, 172];

var names = [
  "Building API aufsetzen",
  "Group Logik", "Projekt Doku",
  "REST Best Practices",
  "PUSH",
  "Publish and Subscribe",
  "Location aware",
  "Context Awareness",
  "Klausurtermin",
  "Vortrag slides"
];

var topics = [
  "verteilte Systeme",
  "netzwerk",
  "mobile systeme"
];

var contents = [
  "hier schreiben wir was cooles",
  "und da auch was cooles",
  "sometimes you win, sometimes you lose",
  "never give it up",
  "always have a good time"
];


for(let group of groups){
  for(let name of names ){
    for(let topic of topics) {
      for(let content of contents){

        unirest.post('http://romue404.pythonanywhere.com/api/notes/' + group)
          .headers({
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxMDYsImV4cCI6MTQ4NzQ1Mzc4Mn0.tiH1mntQLQMMmr-wzELfAij2D6UNqyYx6FUDLhs2jZ4'
          })
          .send({
            "name": name,
            "topic": topic ,
            "content": content
          })
          .end(function (response) {
            console.log(response.body);
          });



      }
    }
  }

}
