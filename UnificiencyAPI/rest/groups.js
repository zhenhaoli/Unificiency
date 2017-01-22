var group = require('../models/group');
var jwt     = require('express-jwt');
var config = require('../config/config');
var unirest = require('unirest');


module.exports = {
  setRoutes: function(app) {

    var jwtCheck = jwt({
      secret: config.secret
    });

    //app.use('/groups', jwtCheck);

    app.get('/groups', function(req, res) {

      var authToken = req.get('Authorization');
      console.log(authToken)

      unirest
        .get(config.pythonAPI + 'groups/lmu/')
        .headers({
          'Authorization': req.query.pythonToken
        })
        .end(function (response) {
          console.log(response.body)
          res.json(response.body)
        });

    });


    app.post('/groups/:id/join', function(req, res) {
      // notify group members new user joined
    });

    app.post('/groups/:id/leave', function(req, res) {
      // notify group members member named xx left
    });

    app.put('/groups/:id', function(req, res) {
      // notify group members user xx changed group info
    });

  }
};