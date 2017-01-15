var connection = require('../db/connection');
var config = require('../config/config.js');

function Group() {

  //TODO: make req.id based on token to identify user
  this.getByUser = function(req, res) {
    connection.acquire(function(err, con) {
      con.query(`
 SELECT name, topic, description
 from studygroup sg 
 left join usergroup ug 
 on ug.groupid = sg.id 
 left join user u 
 on u.id = ug.userid
 where u.id = ?
`, [req.query.userid], function(err, groups) {

        con.release();
        res.json(groups);

      });
    });
  };

  this.getAll = function(req, res) {
    connection.acquire(function(err, con) {

      con.query(`
 SELECT name, topic, description
 from studygroup sg 
`, function(err, groups) {

        con.release();
        //TODO: remove when have more data but need now for list view testing
        res.json(groups.concat(groups).concat(groups).concat(groups).concat(groups));

      });
    });
  };



}

module.exports = new Group();