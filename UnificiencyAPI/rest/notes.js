var config = require('../config/config');
var async = require('asyncawait/async');
var await = require('asyncawait/await');
var unirest = require('unirest');

module.exports = {
  setRoutes: function(app) {

    app.post('/groups/:groupId/notes/', function(req, res) {
      var authToken = req.get('Authorization');
      var groupId = req.params.groupId;

      unirest
        .post(config.pythonAPI + 'groups/' + groupId + 'notes/')
        .headers({ 'Authorization': authToken })
        .send(req.body)
        .end(function (response) {
          res.json(response.body)
        });

    });

    app.put('/notes/:id', function(req, res) {
      var noteId = req.params.id;
      var authToken = req.get('Authorization');

      unirest
        .put(config.pythonAPI + 'notes/' + noteId + '/')
        .headers({ 'Authorization': authToken })
        .send(req.body)
        .end(processResponse);

      function processResponse(response) {
        if(response.statusCode === 200){
          getGroupName();
        }
        res.send(response);
      }

      function getGroupName() {
        unirest
          .get(config.pythonAPI + 'groups/' + req.body.groupId)
          .headers({ 'Authorization': authToken })
          .end(function (response) {
            var groupName = response.body.name;
            notifyGroupMembersAboutUpdatedNote(groupName);
          })
      }

      function notifyGroupMembersAboutUpdatedNote(group) {
        var message = "Note #" + req.body.name +  "# in your group @" + group + "@ was updated";

        unirest
          .post(config.firebaseAPI)
          .headers(config.firebaseAPIKey)
          .send({
            "to": "/topics/group" + req.body.groupId,
            "data": {
              "message": message,
            },
            "notification": {"body": message}
          })
          .end()
      }
    });
  }
};