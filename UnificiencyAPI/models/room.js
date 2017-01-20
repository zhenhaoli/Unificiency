var connection = require('../db/connection');

function Room() {

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

          if (i % 5) g.available = false;
          else  g.available = true;
          return g;
        })

      res.json(rooms);

    });
    con.release();
  });
};

}

module.exports = new Room();