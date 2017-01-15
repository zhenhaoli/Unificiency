var group = require('../models/group');

module.exports = {
  setRoutes: function(app) {

    app.get('/groups', function(req, res) {

      if(!req.query.userid) {
        group.getAll(req, res);
      }

      group.getByUser(req, res);
    });

  }
};