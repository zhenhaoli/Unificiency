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


let names = ["multirooted", "bacchylides", "commotion", "pseudoresidential", "unsupportableness", "zigzagging", "ethnolinguistic", "roddy", "jujuy", "overurge", "spidery", "cyclometer", "unexpounded", "remediably", "rejoin", "cyphella", "telephoned", "graphotype", "constableship", "purport", "companionless", "outsin", "sovietising", "submersing", "cedalion", "three", "spokesman", "nos", "protoxide", "nabalism", "zoser", "octave", "misproduce", "dubiety", "wanter", "nonsympathy", "papulose", "reverberating", "nonobservational", "glencoe", "mechanicsville", "electrostrictive", "percurrent", "cantata", "kedushoth", "dauphin", "rivalrousness", "aldo", "gipsy", "unnigh", "briskly", "monarda", "rampingly", "thick", "yeti", "gimmal", "lobulated", "hylozoistic", "lymphangitis", "posthaste", "guayaquil", "paradigm", "uninterpretative", "barefacedly", "oversized", "poignantly", "fibrocement", "repatronize", "edhessa", "missionaries", "faradizing", "porkier", "unoverhauled", "nominalism", "shiftingness", "overpricing", "illiberality", "helpfully", "bathythermograph", "potch", "leghorn", "steeper", "nucleus", "kashruth", "trotskyism", "catchpoll", "wastingness", "reroll", "tucker", "recopy", "sizeableness", "petalody", "shopman", "grapelike", "likeliest", "barracker", "reproducibility", "trochoidally", "wagoner", "cornel"];





names.forEach(function (name) {

  let rnd = Math.floor((Math.random() * areas.length));
  let rnd2 = Math.floor((Math.random() * names.length));
  let descriptions = (names.map( n => n + " " + areas[rnd])).map(d => d + " " +names[rnd2]);
  unirest.post('http://romue404.pythonanywhere.com/api/groups/')
    .headers({
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjo3MiwiZXhwIjoxNDg3MTc2MzA1fQ.NUCZV85_yKgAt7jIZL-XpANI3ibwiBFJ1cJjYhpBqyI'
    })


    .send({
      "topic_area": areas[rnd],
      "name": name,
      "description": descriptions[rnd2]
    })
    .end(function (response) {
    });
})

