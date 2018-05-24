/**
 * Information.js
 *
 * @description :: TODO: You might write a short summary of how this model works and what it represents here.
 * @docs        :: http://sailsjs.org/documentation/concepts/models-and-orm/models
 */

module.exports = {

  attributes: {
    category: {
      type: 'string',
      enum: [ "culture", "education and formation",
        "emergencies", "language", "justice", "public administration", "housing",
        "health", "work", "tourism", "off topic"]
    },
    title: {
      type: 'string',
      required: true
    },
    content: {
      type: 'string',
      required: true
    },
    url: {
      type: 'string'
    },
    type: {
      type: 'string',
      enum: ['text','list','map']
    },
    isTarifa: {
      type: 'boolean',
      defaultsTo: false
    }

  }
};

