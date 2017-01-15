var express = require('express');
var bodyparser = require('body-parser');
var connection = require('./db/connection');


//APIs
var buildings = require('./rest/buildings');

var app = express();
app.use(bodyparser.urlencoded({extended: true}));
app.use(bodyparser.json());

connection.init();

buildings.setRoutes(app);




app.get('/', function(req, res) {

  var routes = [];
  app._router.stack.forEach(function(r){
    if (r.route && r.route.path){
      routes.push(r.route.path)
    }
  });

  res.json(routes)

});


var server = app.listen(3000, function() {
  console.log('Server listening on port ' + server.address().port);
});




