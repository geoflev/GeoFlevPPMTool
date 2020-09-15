package io.geoflev.ppmtool.exceptions;

public class InvalidLoginResponse {

    //we create the json object we want to return for invalid login
    private String username;
    private String password;


    public InvalidLoginResponse() {
        this.username = "Invalid Username";
        this.password = "Invalid Password";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
