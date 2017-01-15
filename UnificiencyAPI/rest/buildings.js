var building = require('../models/building');
var jwt     = require('express-jwt');
var config = require('../config/config');

module.exports = {
  setRoutes: function(app) {

    var jwtCheck = jwt({
      secret: config.secret
    });

    app.use('/buildings', jwtCheck);

    app.get('/buildings', function(req, res) {
      return building.getAll(res);
    });

    app.get('/buildings/nearest', function(req, res) {
      return building.getNearest(req, res);
    });

  }
};