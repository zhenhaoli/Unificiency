var building = require('../models/building');

module.exports = {
  setRoutes: function(app) {

    app.get('/buildings', function(req, res) {
      return building.getAll(res);
    });

    app.get('/buildings/nearest', function(req, res) {
      return building.getNearest(req, res);
    });

  }
};