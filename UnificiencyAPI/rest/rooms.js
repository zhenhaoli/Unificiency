var rooms = require('../models/room');
var jwt     = require('express-jwt');
var config = require('../config/config');

module.exports = {
  setRoutes: function(app) {

    var jwtCheck = jwt({
      secret: config.secret
    });

   // app.use('/rooms', jwtCheck);

    app.get('/rooms', function(req, res) {
      return rooms.getByBuildingPartAdrress(req, res);
    });

  }
};