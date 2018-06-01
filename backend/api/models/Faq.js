/**
 * Faq.js
 *
 * @description :: TODO: You might write a short summary of how this model works and what it represents here.
 * @docs        :: http://sailsjs.org/documentation/concepts/models-and-orm/models
 */

module.exports = {

  attributes: {
    question:{
      type: 'string',
      required: true
    },
    answer:{
      type: 'string',
      required: true
    },
    category: {
      type: 'string',
      enum: [ "culture", "education and formation",
        "emergencies", "language", "justice", "public administration", "housing",
        "health", "work", "tourism", "off topic"],
      required: true
    },
    puntuation:{
      type: 'integer',
      defaultsTo: 0
    },
    numberReports:{
      type: 'integer',
      defaultsTo: 0
    },
    reportedBy: {
      collection: 'user',
      via: 'reportedFaq'
    },
    valoration: {
      model: 'valorationFAQ'
    } 

  }
};

