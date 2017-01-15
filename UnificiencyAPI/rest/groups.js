var group = require('../models/group');

module.exports = {
  setRoutes: function(app) {

    app.get('/groups', function(req, res) {

      if(!req.query.userid) {
        return group.getAll(req, res);
      }

      return group.getByUser(req, res);
    });

  }
};