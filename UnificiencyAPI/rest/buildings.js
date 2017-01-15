var building = require('../models/building');

module.exports = {
  setRoutes: function(app) {

    app.get('/buildings', function(req, res) {
      building.getAll(res);
    });

    app.get('/buildings/nearest', function(req, res) {
      building.getNearest(req, res);
    });

  }
};