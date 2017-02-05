var unirest = require('unirest');
fs = require('fs');


var groups = [1, 39, 41, 42, 43, 44];

var names = [
  "Building API aufsetzen",
  "Group Logik", "Projekt Doku",
  "REST Best Practices",
  "PUSH",
  "Publish and Subscribe",
  "Location aware",
  "Context Awareness",
  "Klausurtermin",
  "Vortrag slides",
  "Aufgabe2 von MMN","Übungen von ML","Aufgabe2 von MMN","Übungen von ML","MMI","KRR","MMI2","Übungen von ML","Wissenschaftliche Arbeit","Data Science","Python","Java","Javascript","C","C++","C#","Scala","Fotran","OpenMP","Julia","Spring Framework","Neural Netword","Deep learning","Data Mining","Verteilte System","Mobilkommunication","Data Struktur","Algorithmus","IT Management","VR","AR","MR","Web Design","Software entwickln","Medientechnik","3D Printing","Hardware","Automobil Praktikzum","Recent Data Science","Clustering","Classification","Unsupervised Training","Supervised Leraning","Reforcement Learning","Auto encode","DCGAN"
];

var topics = [
  "MSP",
  "Proggen",
  "Hacken",
  "Hackathon",
  "Lernen",
  "Klausur",
  "Party",
  "Mathe",
  "Java",
  "JavaScript",
  "Python",
  "Machine Learning",
  "Data Science",
  "Stohastik",
  "Jobs",
  "Meetup",
  "Essen",
  "Android",
  "verteilte Systeme",
  "netzwerk",
  "mobile systeme"
];



for(let group of groups){
  for(let name of names ){
    for(let topic of topics) {

        unirest.post('http://romue404.pythonanywhere.com/api/groups/' + groupId + '/notes/')
          .headers({
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjo0LCJleHAiOjE0ODg5MDU5MTl9.C092k6aQsCQSsGZHdvEq3JDGp1fQzYQrlBrgWw5LPYQ'
          })
          .send({
            "name": name,
            "topic": topic ,
            "file": fs.readFileSync('./logo.gif')
          })
          .end(function (response) {
            console.log(response.body);
          });


    }
  }

}
