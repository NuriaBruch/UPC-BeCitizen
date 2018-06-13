/**
 * Faq.js
 *
 * @description :: TODO: You might write a short summary of how this model works and what it represents here.
 * @docs        :: http://sailsjs.org/documentation/concepts/models-and-orm/models
 */

module.exports = {

    attributes: {
    faq: {
        model: 'faq'
    },
    user: {
        model: 'user'
    },    
    valoration:{
        type: 'integer',
        defaultsTo: 0
    }
    }
  };
  
  