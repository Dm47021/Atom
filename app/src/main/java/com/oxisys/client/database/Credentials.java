package com.oxisys.client.database;

/**
 * Created by joshu on 9/3/2016.
 */
public class Credentials {

    //private variables
    int _id;
    String _username;
    String _password;
    int _admin;
    private String comment;

    // Empty constructor
    public Credentials() {

    }

    // constructor
    public Credentials(int id, String username, String password, int admin) {
        this._id = id;
        this._username = username;
        this._password = password;
        this._admin = admin;
    }

    // constructor
    public Credentials(String username, String password, int admin) {
        this._username = username;
        this._password = password;
        this._admin = admin;
    }

    public Credentials(String username, String password) {
        this._username = username;
        this._password = password;
    }

    public Credentials(String _username){
        this._username = _username;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
    }

    // getting name
    public String getUsername() {
        return this._username;
    }

    // setting name
    public void setUsername(String username) {
        this._username = username;
    }

    // getting phone number
    public String getPassword() {
        return this._password;
    }

    // setting phone number
    public void setPassword(String password) {
        this._password = password;
    }

    public int getPrivilege() {
        return this._admin;
    }

    public void setPrivilege(int admin) {
        this._admin = admin;
    }

    @Override
    public String toString() {
        return _username;
    }
}
