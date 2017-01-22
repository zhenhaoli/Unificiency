var config = require('../config/config');
var async = require('asyncawait/async');
var await = require('asyncawait/await');
var unirest = require('unirest');

module.exports = {
  setRoutes: function(app) {


    app.post('/groups/:groupId/notes/', function(req, res) {
      var groupId = req.params.groupId;
      conole.log('group id: ' + groupId);

      var authToken = req.get('Authorization');
      console.log('token: ' + authToken);

      unirest
        .post(config.pythonAPI + 'groups/' + groupId + 'notes/')
        .headers({
          'Authorization': req.query.pythonToken
        })
        .send(req.body)
        .end(function (response) {
          console.log(response.body);
          res.json(response.body)
        });

    });

    app.put('/notes/:id', function(req, res) {
      var noteId = req.params.id;
      var authToken = req.get('Authorization');

      unirest
        .put(config.pythonAPI + 'notes/' + noteId + '/')
        .headers({
          'Authorization': authToken
        })
        .send(req.body)
        .end(processResponse);

      function processResponse(response) {
        res.send(response);
        if(response.statusCode === 200){
          getGroupName();
        }
      }

      function getGroupName() {
        unirest
          .get(config.pythonAPI + 'groups/' + req.body.groupId)
          .headers({
            'Authorization': authToken
          })
          .end(function (response) {
            console.log(response.body)
            var groupName = response.body.name;
            notifyGroupMembersAboutUpdatedNote(groupName)
          })
      }

      function notifyGroupMembersAboutUpdatedNote(group) {
        var message = "Note " + req.body.name +  " in your group " + group + " was updated";

        unirest
          .post('https://fcm.googleapis.com/fcm/send')
          .headers({
            "Content-Type": "application/json",
            "Authorization": "key=AAAAntf0k9g:APA91bHcmbj33OnjcIDGLUpTYX_RJ9oq45AQQn8KUsLCv3mdM4tp3yYHVVS1ZsKGmRTjMEkmN_x1SjEJ0SXrtqCy1Lkb0oCd0aI-qapW7TCTVos_STk0MyPRWlQIA-8Wc0CPdY0ghGMA"
          })
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