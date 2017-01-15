var unirest = require('unirest');
var connection = require('../db/connection');
var config = require('../config/config.js');

function Building() {

  this.getAll = function(res) {
    connection.acquire(function(err, con) {
      con.query('select * from our_buildings', function(err, buildings) {
        con.release();
        res.json(buildings);
      });
    });
  };

  this.getNearest = function(req, res) {
    connection.acquire(function(err, con) {
      con.query('select * from our_buildings', function(err, buildings) {
        con.release();

        var sourceLocation;

        if(req.query.lat && req.query.lng) {
          sourceLocation = req.query.lat + "," + req.query.lng;
        }
        else {
          sourceLocation = "48.150479,11.580015";
        }

        var targetLocations = [];
        for(let building of buildings){
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
              key: config.GOOGLE_API_KEY
            })
            .end(function (response) {

              //if we we used up our API limit we use the unlimited mode aka walking
              if(response.body.error_message){
                getDistranceFromAPI('walking');
                return;
              }

              var distances = response.body.rows[0].elements;

              buildings.forEach( (building, i) => {
                building.distanceText = distances[i].distance.text;
                building.durationText = distances[i].duration.text;
                building.distance = distances[i].distance.value;
                building.duration = distances[i].duration.value;
              });

              buildings.sort(function (a, b) {
                return a.duration - b.duration;
              });

              res.json(buildings)

            });
        }

        getDistranceFromAPI('transit'); //this api requires key but is better for real time usage since student will use public transits ...

      })
    });
  }
}

module.exports = new Building();