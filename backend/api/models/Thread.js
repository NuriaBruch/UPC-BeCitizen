/**
 * Thread.js
 *
 * @description :: TODO: You might write a short summary of how this model works and what it represents here.
 * @docs        :: http://sailsjs.org/documentation/concepts/models-and-orm/models
 */

module.exports = {

  attributes: {
    title:{
      type: 'string',
      required: true
    },
    content: {
      type: 'string',
      required: true
    },
    category: {
      type: 'string',
      enum: [ "culture", "Education y formation",
        "emergencies", "language", "justice", "public administration", "housing",
        "health", "work", "tourism", "off topic"],
    },
    postedBy:{
      model: 'user'
    },
    votedBy:{
      collection: 'user',
      via: 'votes'
    },
    reportedBy:{
      collection: 'user',
      via: 'reports'
    },
    numberReports:{
      type: 'integer',
      defaultsTo: 0
    },
    numberVotes:{
      type: 'integer',
      defaultsTo: 0
    }
  }


};

