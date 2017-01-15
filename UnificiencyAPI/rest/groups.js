var group = require('../models/group');
var jwt     = require('express-jwt');
var config = require('../config/config');

module.exports = {
  setRoutes: function(app) {

    var jwtCheck = jwt({
      secret: config.secret
    });

    app.use('/groups', jwtCheck);

    app.get('/groups', function(req, res) {

      if(!req.query.userid) {
        return group.getAll(req, res);
      }

      return group.getByUser(req, res);
    });

  }
};