var express = require('express');
var bodyparser = require('body-parser');

var app = express();
app.use(bodyparser.urlencoded({extended: true}));
app.use(bodyparser.json());

var connection = require('./db/connection');
connection.init();


//APIs
var buildings = require('./rest/buildings');
var groups = require('./rest/groups');

buildings.setRoutes(app);
groups.setRoutes(app);


//print REST routes
app.get('/', function(req, res) {

  var routes = [];
  app._router.stack.forEach(function(r){
    if (r.route && r.route.path){
      routes.push(r.route.path)
    }
  });

  res.json(routes)

});


var server = app.listen(5048, function() {
  console.log('Server listening on port ' + server.address().port);
});




