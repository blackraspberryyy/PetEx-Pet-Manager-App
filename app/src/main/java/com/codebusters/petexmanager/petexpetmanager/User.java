package com.codebusters.petexmanager.petexpetmanager;

/**
 * Created by asus-pc on 11/15/2017.
 */

class User {
    private int user_id;
    private String user_firstname;
    private String user_lastname;
    private String user_username;
    private String user_password;
    private int user_bday;
    private String user_sex;
    private Boolean user_status;
    private String user_email;
    private String user_verification_code;
    private Boolean user_isverified;
    private String user_contact_no;
    private String user_picture;
    private String user_address;
    private int user_added_at;
    private int user_updated_at;

    public User(int user_id, String user_firstname, String user_lastname, String user_username, String user_password, int user_bday, String user_sex, Boolean user_status, String user_email, String user_verification_code, Boolean user_isverified, String user_contact_no, String user_picture, String user_address, int user_added_at, int user_updated_at) {
        this.user_id = user_id;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
        this.user_username = user_username;
        this.user_password = user_password;
        this.user_bday = user_bday;
        this.user_sex = user_sex;
        this.user_status = user_status;
        this.user_email = user_email;
        this.user_verification_code = user_verification_code;
        this.user_isverified = user_isverified;
        this.user_contact_no = user_contact_no;
        this.user_picture = user_picture;
        this.user_address = user_address;
        this.user_added_at = user_added_at;
        this.user_updated_at = user_updated_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_firstname() {
        return user_firstname;
    }

    public void setUser_firstname(String user_firstname) {
        this.user_firstname = user_firstname;
    }

    public String getUser_lastname() {
        return user_lastname;
    }

    public void setUser_lastname(String user_lastname) {
        this.user_lastname = user_lastname;
    }

    public String getUser_username() {
        return user_username;
    }

    public void setUser_username(String user_username) {
        this.user_username = user_username;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public int getUser_bday() {
        return user_bday;
    }

    public void setUser_bday(int user_bday) {
        this.user_bday = user_bday;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public Boolean getUser_status() {
        return user_status;
    }

    public void setUser_status(Boolean user_status) {
        this.user_status = user_status;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_verification_code() {
        return user_verification_code;
    }

    public void setUser_verification_code(String user_verification_code) {
        this.user_verification_code = user_verification_code;
    }

    public Boolean getUser_isverified() {
        return user_isverified;
    }

    public void setUser_isverified(Boolean user_isverified) {
        this.user_isverified = user_isverified;
    }

    public String getUser_contact_no() {
        return user_contact_no;
    }

    public void setUser_contact_no(String user_contact_no) {
        this.user_contact_no = user_contact_no;
    }

    public String getUser_picture() {
        return user_picture;
    }

    public void setUser_picture(String user_picture) {
        this.user_picture = user_picture;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public int getUser_added_at() {
        return user_added_at;
    }

    public void setUser_added_at(int user_added_at) {
        this.user_added_at = user_added_at;
    }

    public int getUser_updated_at() {
        return user_updated_at;
    }

    public void setUser_updated_at(int user_updated_at) {
        this.user_updated_at = user_updated_at;
    }
}
