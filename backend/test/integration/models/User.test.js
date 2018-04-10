describe('UserModel', function () {

  describe('#insertUser()', function () {
    it('should check if user is created', function (done) {
      var email = "omarcruz11@hotmail.com";
      var username = "omar97";
      var password = "contraseña";
      var name = "omar";
      var usernme = "omar2";
      var biography = "This is me, this is real";
      var birthday = "29/02/1975";
      var country = "Argentina";
      var rank = "diamond";
      var profilePicture = "http://images/omar.png";
      var hasFacebook = "true";
      var hasGoogle = "false";
      User.create({
        email: email,
        username: username,
        password: password,
        name: name,
        usernme: usernme,
        biography: biography,
        birthday: birthday,
        country: country,
        rank: rank,
        profilePicture: profilePicture,
        hasFacebook: hasFacebook,
        hasGoogle: hasGoogle
      })
      .then(function (userCreated) {
        //if(userCreated.email == email) done();
        //else done("Error creating user!!!");
        done("asds");
      })
      .catch((err) => done(err));
    });
  });

});