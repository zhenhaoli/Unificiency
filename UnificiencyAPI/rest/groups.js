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

      unirest
        .get(config.pythonAPI + 'groups/lmu/')
        .headers({
          'Authorization': req.body.pythonToken
        })
        .end(function (response) {
          console.log(response)
          res.json(response)
        });

    });

    app.post('/groups', function(req, res) {
      return group.getAll(req, res);
    });

    app.get('/groups/:id', function(req, res) {
      return group.getAll(req, res);
    });

    app.post ('/groups/:id/join', function(req, res) {
      return group.getAll(req, res);
    });

    app.get('/groups/:id/join', function(req, res) {
      return group.getAll(req, res);
    });

    app.put('/groups/:id', function(req, res) {
      return group.getAll(req, res);
    });

  }
};