/**
 * Thread.js
 *
 * @description :: TODO: You might write a short summary of how this model works and what it represents here.
 * @docs        :: http://sailsjs.org/documentation/concepts/models-and-orm/models
 */

module.exports = {

  attributes: {
    content:{
      type: 'string',
      required: true
    },
    postedBy:{
      model: 'user'
    },
    reportedBy: {
      collection: 'user',
      via: 'reportedComments'
    },
    repliesTo:{
      model: 'comment',
      via:'id'
    },
    belongsTo:{
      model: 'thread',
      via:'id'
    }
  }
};

