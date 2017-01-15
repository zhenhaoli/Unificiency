var config = require('../config/config');
var user = require('../models/user');

module.exports = {
  setRoutes: function(app) {

    app.post('/users/register', function(req, res) {
      return user.register(req, res);
    });

    app.post('/users/login', function(req, res) {
      return user.login(req, res);
    });

  }
};