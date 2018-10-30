package com.codebusters.petexmanager.petexpetmanager;

class Pet {
    private int pet_id;
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

    public Pet(int pet_id, String pet_nfc_tag, String pet_name, int pet_bday, String pet_specie, String pet_sex, String pet_breed, String pet_size, String pet_status, Boolean pet_access, Boolean pet_neutered_spayed, String pet_admission, String pet_description, String pet_history, String pet_picture, String pet_video, int pet_added_at, int pet_updated_at) {
        this.pet_id = pet_id;
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
    }

    public int getPet_id() {
        return pet_id;
    }

    public void setPet_id(int pet_id) {
        this.pet_id = pet_id;
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
}
