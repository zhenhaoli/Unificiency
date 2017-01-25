var config = require('../config/config');
var async = require('asyncawait/async');
var await = require('asyncawait/await');
var unirest = require('unirest');

module.exports = {
  setRoutes: function(app) {

    app.post('/groups/:groupId/notes/', function(req, res) {

      var authToken = req.get('Authorization');
      var groupId = req.params.groupId;

      var message = {};
      getGroupName(message);

      function getGroupName(message) {
        unirest
          .get(config.pythonAPI + 'groups/' + groupId)
          .headers({ 'Authorization': authToken })
          .end(function (response) {
            message.group = response.body.name;
            getUserName(message);
          })
      }

      function getUserName(message) {
        unirest
          .get(config.pythonAPI + 'users/')
          .headers({ 'Authorization': authToken })
          .end(function (response) {
            message.username = response.body.username;
            notifyGroupMembersAboutNewNote(message);
          })
      }

      function notifyGroupMembersAboutNewNote(content) {
        var message = content.username + " posted a new note #" + req.body.name +  "# in your group @" + content.group;

        console.log(message)
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

      res.status(200).json({message: 'ok'});

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
          var message = {};
          getGroupName(message);
        }
        res.send(response.body);
      }

      function getGroupName(message) {
        unirest
          .get(config.pythonAPI + 'groups/' + req.body.groupId)
          .headers({ 'Authorization': authToken })
          .end(function (response) {
            message.group = response.body.name;
            getUserName(message);

          })
      }

      function getUserName(message) {
        unirest
          .get(config.pythonAPI + 'users/')
          .headers({ 'Authorization': authToken })
          .end(function (response) {
            message.username = response.body.username;
            notifyGroupMembersAboutUpdatedNote(message);
          })
      }

      function notifyGroupMembersAboutUpdatedNote(content) {
        var message = content.username + " updated the note #" + req.body.name +  "# in your group @" + content.group;

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