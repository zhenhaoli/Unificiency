var express = require('express');
var bodyparser = require('body-parser');
var cors = require('cors');


var app = express();
app.use(bodyparser.urlencoded({extended: true}));
app.use(bodyparser.json());

var connection = require('./db/connection');
connection.init();


//APIs
var users = require('./rest/users');
var buildings = require('./rest/buildings');
var groups = require('./rest/groups');

users.setRoutes(app);
buildings.setRoutes(app);
groups.setRoutes(app);

app.use(function(err, req, res, next) {
  if (err.name === 'StatusError') {
    res.send(err.status, err.message);
  } else {
    next(err);
  }
});

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




