/**
 * User.js
 *
 * @description :: TODO: You might write a short summary of how this model works and what it represents here.
 * @docs        :: http://sailsjs.org/documentation/concepts/models-and-orm/models
 */

module.exports = {

  attributes: {
    username: {
      columnName: 'the_primary_key',
      type: 'string',
      primaryKey: true

    },
    email:{
      type: 'string',
      unique: true
    },
    password: {
      type: 'string'
    },
    pais: {
      type: 'string'
    },
    rango: {
      type: 'string'
    }
  }
};

