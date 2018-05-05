describe('UserModel', function () {

  describe('#insertUser()', function () {
    it('should check if user is created', function (done) {
      let email = "omarcruz11@hotmail.com";
      let username = "omar97";
      let password = "contraseña";
      let name = "omar";
      let usernme = "omar2";
      let biography = "This is me, this is real";
      let birthday = "29/02/1975";
      let country = "Argentina";
      let rank = "diamond";
      let profilePicture = "1";
      let hasFacebook = true;
      let hasGoogle = false;
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
        if(userCreated.email == email &&
        userCreated.username == username &&
        userCreated.password == password &&
        userCreated.name == name &&
        userCreated.usernme == usernme &&
        userCreated.biography == biography &&
        userCreated.birthday == birthday &&
        userCreated.country == country &&
        userCreated.rank == rank &&
        userCreated.profilePicture == profilePicture &&
        userCreated.hasFacebook == hasFacebook &&
        userCreated.hasGoogle == hasGoogle) 
        {
          done();
        }
        else done("Error creating user!!!");
      })
      .catch((err) => done(err));
    });
  });

});