var unirest = require('unirest');


let areas = [
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
];


let names = ["Belt", "Wash", "Haircut", "Recession", "Forward", "Stem", "Story", "Lamp", "Whip", "Commitment", "Sheep", "Viable", "Corn", "Print", "Bulletin", "Smile", "Graze", "Rotate", "Beautiful", "Prospect"];

names.forEach(function (name, index) {

  let rnd = Math.floor((Math.random() * areas.length));
  let rnd2 = Math.floor((Math.random() * names.length));
  let description = "Das ist die Beschreibung der Gruppe " + name + " für das Thema " + areas[rnd] + ". Wir sind eine tolle Gruppe! :) ";

  var toSend = {
    "topic_area": areas[rnd],
    "name": name,
    "description": description
  };

  if( !(index % 3) ){
    toSend.password = "123456"
  }


  unirest.post('http://romue404.pythonanywhere.com/api/groups/')
    .headers({
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjo0LCJleHAiOjE0ODg5MDU5MTl9.C092k6aQsCQSsGZHdvEq3JDGp1fQzYQrlBrgWw5LPYQ'
    })

    .send(toSend)
    .end(function (response) {
    });
})

