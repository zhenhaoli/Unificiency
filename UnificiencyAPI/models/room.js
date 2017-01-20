var connection = require('../db/connection');

function Room() {

  var hours = [6, 8, 10, 12, 14, 16, 18, 20];
  var minutes = [0, 15, 30, 45];

  var freeHours = [0, 1, 2, 3, 4];
  var freeMinutes = [7];

  this.getByBuildingPartAdrress = function(req, res) {

    connection.acquire(function(err, con) {

      console.log(req.query.address)
      con.query(`
select r.name, f.level, bp.address
from room r 
join floor f
on r.floorCode = f.code
join building_part bp
on f.buildingPart = bp.code
join building b 
on bp.buildingCode = b.code
where bp.address = ?
`, [req.query.address], function(err, rooms) {

        rooms.forEach( (g,i) => {

          g.freeFromHour = hours[i% (hours.length)];
          g.freeFromMinutes = minutes[i% (minutes.length)];

          g.freeToHour = g.freeFromHour +  freeHours[i% (freeHours.length)];
          g.freeToMinutes = g.freeFromMinutes + freeMinutes[i% (freeMinutes.length)];
          return g;
        })

        res.json(rooms);

      });
      con.release();
    });
  };

}

module.exports = new Room();