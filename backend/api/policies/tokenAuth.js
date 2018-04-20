/**
 * sessionAuth
 *
 * @module      :: Policy
 * @description :: Simple policy to allow any authenticated user
 *                 Assumes that your login action in one of your controllers sets `req.session.authenticated = true;`
 * @docs        :: http://sailsjs.org/#!/documentation/concepts/Policies
 *
 */
module.exports = function(req, res, next) {

    // User is allowed, proceed to the next policy, 
    // or if this is the last policy, the controller
    //if (req.session.authenticated) {
      //return next();
    //}
    var jwt = require('jsonwebtoken');
    jwt.verify(req.query.token, 'bienquesito', function(err, decoded) {
        if(err !== undefined && err) return res.send({
            status: "Error",
            errors: ["User not logged in"]
        });
        else next();
    });
  };
  