  /**
 * User.js
 *
 * @description :: TODO: You might write a short summary of how this model works and what it represents here.
 * @docs        :: http://sailsjs.org/documentation/concepts/models-and-orm/models
 */

module.exports = {

  attributes: {
    email:{
      type: 'string',
      primaryKey: true
    },
    username: {
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
    },
    hasFacebook:{
      type:'boolean',
      defaultsTo: false
    },
    hasGoogle:{
      type: 'boolean',
      defaultsTo: false
    }
  }
};

