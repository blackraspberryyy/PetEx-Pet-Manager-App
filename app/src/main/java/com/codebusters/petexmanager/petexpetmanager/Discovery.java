package com.codebusters.petexmanager.petexpetmanager;

/**
 * Created by asus-pc on 2/28/2018.
 */

public class Discovery {
    private int discovery_id;
    private int pet_id;
    private int user_id;
    private Double discovery_latitude;
    private Double discovery_longitude;
    private int discovery_added_at;

    private String pet_nfc_tag;
    private String pet_name;
    private int pet_bday;
    private String pet_specie;
    private String pet_sex;
    private String pet_breed;
    private String pet_size;
    private String pet_status;
    private Boolean pet_access;
    private Boolean pet_neutered_spayed;
    private String pet_admission;
    private String pet_description;
    private String pet_history;
    private String pet_picture;
    private String pet_video;
    private int pet_added_at;
    private int pet_updated_at;

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

    public Discovery(int discovery_id, int pet_id, int user_id, Double discovery_latitude, Double discovery_longitude, int discovery_added_at, String pet_nfc_tag, String pet_name, int pet_bday, String pet_specie, String pet_sex, String pet_breed, String pet_size, String pet_status, Boolean pet_access, Boolean pet_neutered_spayed, String pet_admission, String pet_description, String pet_history, String pet_picture, String pet_video, int pet_added_at, int pet_updated_at, String user_firstname, String user_lastname, String user_username, String user_password, int user_bday, String user_sex, Boolean user_status, String user_email, String user_verification_code, Boolean user_isverified, String user_contact_no, String user_picture, String user_address, int user_added_at, int user_updated_at) {
        this.discovery_id = discovery_id;
        this.pet_id = pet_id;
        this.user_id = user_id;
        this.discovery_latitude = discovery_latitude;
        this.discovery_longitude = discovery_longitude;
        this.discovery_added_at = discovery_added_at;
        this.pet_nfc_tag = pet_nfc_tag;
        this.pet_name = pet_name;
        this.pet_bday = pet_bday;
        this.pet_specie = pet_specie;
        this.pet_sex = pet_sex;
        this.pet_breed = pet_breed;
        this.pet_size = pet_size;
        this.pet_status = pet_status;
        this.pet_access = pet_access;
        this.pet_neutered_spayed = pet_neutered_spayed;
        this.pet_admission = pet_admission;
        this.pet_description = pet_description;
        this.pet_history = pet_history;
        this.pet_picture = pet_picture;
        this.pet_video = pet_video;
        this.pet_added_at = pet_added_at;
        this.pet_updated_at = pet_updated_at;
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

    public int getDiscovery_id() {
        return discovery_id;
    }

    public void setDiscovery_id(int discovery_id) {
        this.discovery_id = discovery_id;
    }

    public int getPet_id() {
        return pet_id;
    }

    public void setPet_id(int pet_id) {
        this.pet_id = pet_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Double getDiscovery_latitude() {
        return discovery_latitude;
    }

    public void setDiscovery_latitude(Double discovery_latitude) {
        this.discovery_latitude = discovery_latitude;
    }

    public Double getDiscovery_longitude() {
        return discovery_longitude;
    }

    public void setDiscovery_longitude(Double discovery_longitude) {
        this.discovery_longitude = discovery_longitude;
    }

    public int getDiscovery_added_at() {
        return discovery_added_at;
    }

    public void setDiscovery_added_at(int discovery_added_at) {
        this.discovery_added_at = discovery_added_at;
    }

    public String getPet_nfc_tag() {
        return pet_nfc_tag;
    }

    public void setPet_nfc_tag(String pet_nfc_tag) {
        this.pet_nfc_tag = pet_nfc_tag;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public int getPet_bday() {
        return pet_bday;
    }

    public void setPet_bday(int pet_bday) {
        this.pet_bday = pet_bday;
    }

    public String getPet_specie() {
        return pet_specie;
    }

    public void setPet_specie(String pet_specie) {
        this.pet_specie = pet_specie;
    }

    public String getPet_sex() {
        return pet_sex;
    }

    public void setPet_sex(String pet_sex) {
        this.pet_sex = pet_sex;
    }

    public String getPet_breed() {
        return pet_breed;
    }

    public void setPet_breed(String pet_breed) {
        this.pet_breed = pet_breed;
    }

    public String getPet_size() {
        return pet_size;
    }

    public void setPet_size(String pet_size) {
        this.pet_size = pet_size;
    }

    public String getPet_status() {
        return pet_status;
    }

    public void setPet_status(String pet_status) {
        this.pet_status = pet_status;
    }

    public Boolean getPet_access() {
        return pet_access;
    }

    public void setPet_access(Boolean pet_access) {
        this.pet_access = pet_access;
    }

    public Boolean getPet_neutered_spayed() {
        return pet_neutered_spayed;
    }

    public void setPet_neutered_spayed(Boolean pet_neutered_spayed) {
        this.pet_neutered_spayed = pet_neutered_spayed;
    }

    public String getPet_admission() {
        return pet_admission;
    }

    public void setPet_admission(String pet_admission) {
        this.pet_admission = pet_admission;
    }

    public String getPet_description() {
        return pet_description;
    }

    public void setPet_description(String pet_description) {
        this.pet_description = pet_description;
    }

    public String getPet_history() {
        return pet_history;
    }

    public void setPet_history(String pet_history) {
        this.pet_history = pet_history;
    }

    public String getPet_picture() {
        return pet_picture;
    }

    public void setPet_picture(String pet_picture) {
        this.pet_picture = pet_picture;
    }

    public String getPet_video() {
        return pet_video;
    }

    public void setPet_video(String pet_video) {
        this.pet_video = pet_video;
    }

    public int getPet_added_at() {
        return pet_added_at;
    }

    public void setPet_added_at(int pet_added_at) {
        this.pet_added_at = pet_added_at;
    }

    public int getPet_updated_at() {
        return pet_updated_at;
    }

    public void setPet_updated_at(int pet_updated_at) {
        this.pet_updated_at = pet_updated_at;
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
