/**
 * Conversation.js
 *
 * @description :: TODO: You might write a short summary of how this model works and what it represents here.
 * @docs        :: http://sailsjs.org/documentation/concepts/models-and-orm/models
 */

module.exports = {

  attributes: {
    user1: {
      model: "user"
    },
    user2: {
      model: "user"
    },
    email1:{
      type: "string"
    },
    email2:{
      type: "string"
    },
    newMessage1: {
      type: "boolean",
      defaultsTo: false
    },
    newMessage2: {
      type: "boolean",
      defaultsTo: false
    },
    lastMessageTime:{
      type: "datetime"
    }
  }
};

