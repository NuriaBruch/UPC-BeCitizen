/**
 * User.js
 *
 * @description :: TODO: You might write a short summary of how this model works and what it represents here.
 * @docs        :: http://sailsjs.org/documentation/concepts/models-and-orm/models
 */

module.exports = {

  attributes: {
    username: {
      type: 'string',
      unique: true
    },
    email:{
      type: 'string',
      primaryKey: true
    },
    password: {
      type: 'string'
    },
    pais: {
      type: 'string'
    },
    rango: {
      type: 'string'
    },
    tipoLogIn: {
      type: 'string',
      enum: ['facebook', 'google', 'mail'],
      defaultsTo: 'mail'
    }
  }
};

