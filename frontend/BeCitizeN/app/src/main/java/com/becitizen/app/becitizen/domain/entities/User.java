package com.becitizen.app.becitizen.domain.entities;

public class User {
    private String username;
    private String mail, password;
    private String firstName, lastName;
    private String birthDate;
    private String country;
    private String biography;
    private String rank;
    private int image;
    private boolean facebook;
    private boolean google;

    /**
     * Creadora. Crea un usuario que no ha iniciado con Facebook
     * ni Google y con rango carbón.
     */
    public User() {
        facebook = google = false;
        rank = "coal";
        image = (int) (Math.random() * 8) + 1;
        mail = "";
        password = "";
        firstName = "";
        lastName = "";
        birthDate = "";
        biography = "";
    }

    /**
     * Creadora. Crea un usuario con el correo electronico y
     * contrasena de los parametros
     *
     * @param mail Email
     * @param password Contrasena
     */
    public User (String mail, String password) {
        this.mail = mail;
        this.password = password;
        rank = "coal";
        image = (int) (Math.random() * 8) + 1;
        firstName = "";
        lastName = "";
        birthDate = "";
        biography = "";
    }

    /**
     * Metodo para obtener el nombre de usuario
     *
     * @return Nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Metodo para asignar el nombre de usuario
     *
     * @param username Nombre de usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Metodo para obtener el correo electronico del usuario
     *
     * @return Correo electronico
     */
    public String getMail() {
        return mail;
    }

    /**
     * Metodo para asignar el correo electronico al usuario
     *
     * @param mail Correo electronico
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Metodo para obtener la contrasena del usuario
     *
     * @return Contrasena
     */
    public String getPassword() {
        return password;
    }

    /**
     * Metodo para asignar la contrasena al usuario
     *
     * @param password Contrasena
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Metodo para obtener el nombre del usuario
     *
     * @return Nombre
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Metodo para asignar el nombre al usuario
     *
     * @param firstName Nombre
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Metodo para obtener el apellido del usuario
     *
     * @return Apellido
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Metodo para asignar el apellido al usuario
     *
     * @param lastName Apellido
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Metodo para obtener la fecha de nacimiento del usuario
     *
     * @return Fecha de nacimiento
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Metodo para asignar la fecha de nacimiento al usuario
     *
     * @param birthDate Fecha de nacimiento
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Metodo para obtener el pais del usuario
     *
     * @return Pais
     */
    public String getCountry() {
        return country;
    }

    /**
     * Metodo para asignar el pais al usuario
     *
     * @param country Pais
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Metodo para obtener la biografia del usuario
     *
     * @return Biografia
     */
    public String getBiography() {
        return biography;
    }

    /**
     * Metodo para asignar la biografia al usuario
     *
     * @param biography Biografia
     */
    public void setBiography(String biography) {
        this.biography = biography;
    }

    /**
     * Metodo para asignar el rango del usuario
     *
     * @return rango
     */
    public String getRank() {
        return rank;
    }

    /**
     * Metodo para obtener el rango del usuario
     *
     * @param rank Rango
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * Devuelve cierto si el usuario se ha registrado con Facebook
     *
     * @return true si el usuario se ha registrado con Facebook, false de lo contrario
     */
    public boolean isFacebook() {
        return facebook;
    }

    /**
     * Asignar que el usuario se ha registrado con Facebook o no
     *
     * @param facebook true si el usuario se ha registrado con Facebook, false de lo contrario
     */
    public void setFacebook(boolean facebook) {
        this.facebook = facebook;
    }

    /**
     * Devuelve cierto si el usuario se ha registrado con Google
     *
     * @return true si el usuario se ha registrado con Google, false de lo contrario
     */
    public boolean isGoogle() {
        return google;
    }

    /**
     * Asignar que el usuario se ha registrado con Google o no
     *
     * @param google true si el usuario se ha registrado con Google, false de lo contrario
     */
    public void setGoogle(boolean google) {
        this.google = google;
    }

    /**
     * Metodo para asignar el valor de la imagen del usuario
     *
     * @param image
     */
    public void setImage(int image) {
        this.image = image;
    }

    /**
     * Metodo para obtener el valor de la imagen del usuario
     *
     * @return valor de la imagen
     */
    public int getImage() {
        return image;
    }
}
