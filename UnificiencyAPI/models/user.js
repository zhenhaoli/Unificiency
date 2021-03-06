var jwt = require('jsonwebtoken');
var _ = require('lodash');
var connection = require('../db/connection');
var config = require('../config/config.js');

function Group() {

  function createToken(user) {
    return jwt.sign(_.omit(user, 'password'), config.secret, { expiresIn: 60*60*5 });
  }

  this.register = function (req, res) {

    console.log(req.body)

    var user = {
      email: req.body.email,
      username: req.body.username,
      password: req.body.password,
      major: req.body.major
    };


    if (!user.email || !user.password) {
      return res.status(400).send("Some required fields missing");
    }

    connection.acquire(function (err, con) {

      con.query(`
 SELECT email 
 from user
`, function (err, users) {

        if (_.find(users, {email: user.email})) {
          return res.status(409).send("A user with that email already in use");
        }

        con.query(`
Insert into user
SET ?
`, user, function (err, result) {

          if (err) {
            res.status(400).send({
              message: 'Failed to create user'
            });
          } else {
            res.status(201).send({
              id_token: createToken(user)
            });

          }

        });
      });

      con.release();
    });
  };

  this.login = function (req, res) {

    var user = {
      email: req.body.email,
      password: req.body.password,
    };

    console.log(user)

    if (!user.email || !user.password) {
      return res.status(400).send("Some required fields missing");
    }

    connection.acquire(function (err, con) {

      con.query(`
 SELECT * 
 from user
`, function (err, users) {

        con.release();

        var foundUser = _.find(users, {email: user.email, password: user.password});

        if (!foundUser) {
          return res.status(401).send("The username or password don't match");
        }

        if (foundUser.password !== user.password) {
          return res.status(401).send("The username or password don't match");
        }

        if (foundUser.password !== user.password) {
          return res.status(401).send("The username or password don't match");
        }

        res.status(200).send({
          id_token: createToken(user)
        });

      });
    });
  };
}

module.exports = new Group();