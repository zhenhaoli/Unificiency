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
        var message = content.username + " hat eine neue Notiz #" +
          req.body.name +  " in deiner Gruppe @" + content.group + " gepostet"
          ;

        console.log(message)
        unirest
          .post(config.firebaseAPI)
          .headers(config.firebaseAPIKey)
          .send({
            "to": "/topics/group" + groupId,
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
      var groupId = req.body.groupId;
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
        res.status(response.statusCode).send(response.body);
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
        var message = content.username + " hat die Notize #" + req.body.name +
          " in deiner Gruppe @" + content.group + " bearbeitet!";
        unirest
          .post(config.firebaseAPI)
          .headers(config.firebaseAPIKey)
          .send({
            "to": "/topics/group" + groupId,
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