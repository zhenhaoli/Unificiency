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


let names = ["Gallon", "Stretch", "Sheep", "Modernize", "Rare", "Lean", "Situation", "Physical", "Bake", "Cord", "Stake", "Valid", "Depend", "Qualified", "Background", "Hot", "Hand", "Ally", "Casualty", "Inside"];

names.forEach(function (name) {

  let rnd = Math.floor((Math.random() * areas.length));
  let rnd2 = Math.floor((Math.random() * names.length));
  let descriptions = (names.map( n => n + " " + areas[rnd])).map(d => d + " " +names[rnd2]);
  unirest.post('http://romue404.pythonanywhere.com/api/groups/')
    .headers({
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxMzQsImV4cCI6MTQ4ODY3NjQzM30.DKnpnEcFpyAysFGVx-5-8WJkPdo48bgF0_XY-QduSsA'
    })


    .send({
      "topic_area": areas[rnd],
      "name": name,
      "description": descriptions[rnd2]
    })
    .end(function (response) {
    });
})

