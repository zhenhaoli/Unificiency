var unirest = require('unirest');
var connection = require('../db/connection');
var config = require('../config/config.js');

function Building() {

  this.getAll = function (res) {
    connection.acquire(function (err, con) {
      con.query('select * from our_buildings', function (err, buildings) {
        con.release();
        res.json(buildings);
      });
    });
  };


  this.getNearest = function (req, res) {
    connection.acquire(function (err, con) {
      con.query('select * from our_buildings', function (err, buildings) {
        con.release();

        var sourceLocation = {};

        if (req.query.lat && req.query.lng) {
          sourceLocation.lat = req.query.lat;
          sourceLocation.lng = req.query.lng;
        }
        else {
          // LMU Info Bau
          sourceLocation.lat = '48.1493226';
          sourceLocation.lng = '11.5918366';
        }

        buildings.forEach(building => {
          var targetLocation = {
            lat: building.lat,
            lng: building.lng
          };
          building.distance = getDistanceInM(sourceLocation, targetLocation);

          building.distanceText = "Not available";
          building.durationText = "Not available";
          building.duration = -1;
          return building;

        });

        buildings.sort(function (a, b) {
          return a.distance - b.distance;
        });

        res.json(buildings);
        function getDistanceInM(source, target) {
          var R = 6371000; // Radius of the earth in m
          var dLat = deg2rad(source.lat - target.lat);  // deg2rad below
          var dLng = deg2rad(source.lng - target.lng);
          var a =
              Math.sin(dLat / 2) * Math.sin(dLat / 2) +
              Math.cos(deg2rad(target.lat)) * Math.cos(deg2rad(source.lat)) *
              Math.sin(dLng / 2) * Math.sin(dLng / 2)
            ;
          var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
          var d = R * c; // Distance in m
          return d;
        }

        function deg2rad(deg) {
          return deg * (Math.PI / 180)
        }

      })
    });
  };

  this.getNearestGoogle = function (req, res) {
    connection.acquire(function (err, con) {
      con.query('select * from our_buildings', function (err, buildings) {
        con.release();

        var sourceLocation;

        if (req.query.lat && req.query.lng) {
          sourceLocation = req.query.lat + "," + req.query.lng;
        }
        else {
          sourceLocation = "48.150479,11.580015";
        }

        var targetLocations = [];
        for (let building of buildings) {
          targetLocations.push(building.lat + ',' + building.lng)
        }
        var dest = targetLocations.join('|');

        function getDistranceFromAPI(mode) {
          unirest
            .get(config.DistanceMatrixAPI)
            .query({
              origins: sourceLocation,
              destinations: dest,
              language: 'de-DE',
              mode: mode,
              //key: config.GOOGLE_API_KEY
            })
            .end(function (response) {
              if (!response || !response.body) {
                return res.status(503).send("API Calls to Google exceeded");
              }

              //if we we used up our API limit we use the unlimited mode aka walking
              if (response.body.error_message) {
                return res.status(503).send("API Calls to Google exceeded");
              }

              var distances = response.body.rows[0].elements;

              var failed = false;
              for (let i = 0; i < buildings.length; i++) {
                let building = buildings[i];

                if (!distances[i].distance) {
                  failed = true;
                  building.distanceText = "API Calls to Google exceeded";
                  building.durationText = "API Calls to Google exceeded";
                  building.distance = -1;
                  building.duration = -1;
                  break;
                }

                building.distanceText = distances[i].distance.text;
                building.durationText = distances[i].duration.text;
                building.distance = distances[i].distance.value;
                building.duration = distances[i].duration.value;
              }

              if (!failed) {
                buildings.sort(function (a, b) {
                  return a.distance - b.distance;
                });
              }

              res.json(buildings);

              //pushResultStatusToClient();

            });
        }

        getDistranceFromAPI('walking');
        //getDistranceFromAPI('transit'); //this api requires key but is better for real time usage since student will use public transits ...

      })
    });
  }
}
function pushResultStatusToClient() {
  unirest
    .post('https://fcm.googleapis.com/fcm/send')
    .headers({
      "Content-Type": "application/json",
      "Authorization": "key=AAAAntf0k9g:APA91bHcmbj33OnjcIDGLUpTYX_RJ9oq45AQQn8KUsLCv3mdM4tp3yYHVVS1ZsKGmRTjMEkmN_x1SjEJ0SXrtqCy1Lkb0oCd0aI-qapW7TCTVos_STk0MyPRWlQIA-8Wc0CPdY0ghGMA"
    })
    .send({
      "to": "/topics/news",
      "data": {
        "message": "This msg is pushed from server: no DB changes detected.  In the upcoming future, i will only appear when changes occured",
      },
      "notification": {"body": "Hello, we loaded our db and found no changes. In the upcoming future, i will only appear when changes occured"}
    })
    .end(function (response) {

    })
}

module.exports = new Building();