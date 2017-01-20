var group = require('../models/group');
var jwt     = require('express-jwt');
var config = require('../config/config');
var unirest = require('unirest');


module.exports = {
  setRoutes: function(app) {

    var jwtCheck = jwt({
      secret: config.secret
    });

    app.use('/groups', jwtCheck);

    app.get('/groups', function(req, res) {
      return group.getAll(req, res);
    });

  }
};