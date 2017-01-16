var mysql = require('mysql');

var config = require('../config/config.js');

function Connection() {
  this.pool = null;

  this.init = function() {
    this.pool = mysql.createPool({
      connectionLimit: config.MAX_CONNECTIONS,
      host: config.HOST,
      user: config.USER,
      password: config.PASSWORD,
      database: config.DATABASE
    });
  };

  this.acquire = function(callback) {
    this.pool.getConnection(function(err, connection) {
      console.log('err', err)
      callback(err, connection);
    });
  };
}

module.exports = new Connection();