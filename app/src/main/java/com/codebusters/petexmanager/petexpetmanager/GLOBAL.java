package com.codebusters.petexmanager.petexpetmanager;

/**
 * Created by asus-pc on 2/23/2018.
 */

public class GLOBAL {
    static int current_user_login = 0;
    static int edit_user_id = 0;
    static int edit_pet_id = 0;
    static String edit_pet_name = "";
    static String edit_pet_desc = "";
    static int report_missing_user_id = 0;
    static String report_missing_adoption_id = "";
    static int detected_adoption_id = 0;
    static int updates_on_pet_id = 0;


    public static void clear(){
        current_user_login = 0;
        edit_user_id = 0;
        edit_pet_id = 0;
        edit_pet_name = "";
        edit_pet_desc = "";
        report_missing_user_id = 0;
        report_missing_adoption_id = "";
        detected_adoption_id = 0;
        updates_on_pet_id = 0;
    }

    public static int getReport_missing_user_id() {
        return report_missing_user_id;
    }

    public static void setReport_missing_user_id(int report_missing_user_id) {
        GLOBAL.report_missing_user_id = report_missing_user_id;
    }

    public static int getEdit_user_id() {
        return edit_user_id;
    }

    public static void setEdit_user_id(int edit_user_id) {
        GLOBAL.edit_user_id = edit_user_id;
    }

    public static int getUpdates_on_pet_id() {
        return updates_on_pet_id;
    }

    public static void setUpdates_on_pet_id(int updates_on_pet_id) {
        GLOBAL.updates_on_pet_id = updates_on_pet_id;
    }

    public static int getDetected_adoption_id() {
        return detected_adoption_id;
    }

    public static void setDetected_adoption_id(int detected_adoption_id) {
        GLOBAL.detected_adoption_id = detected_adoption_id;
    }

    public static String getReport_missing_adoption_id() {
        return report_missing_adoption_id;
    }

    public static void setReport_missing_adoption_id(String report_missing_adoption_id) {
        GLOBAL.report_missing_adoption_id = report_missing_adoption_id;
    }

    public static int getCurrent_user_login() {
        return current_user_login;
    }

    public static void setCurrent_user_login(int current_user_login) {
        GLOBAL.current_user_login = current_user_login;
    }

    public static int getEdit_pet_id() {
        return edit_pet_id;
    }

    public static void setEdit_pet_id(int edit_pet_id) {
        GLOBAL.edit_pet_id = edit_pet_id;
    }

    public static String getEdit_pet_name() {
        return edit_pet_name;
    }

    public static void setEdit_pet_name(String edit_pet_name) {
        GLOBAL.edit_pet_name = edit_pet_name;
    }

    public static String getEdit_pet_desc() {
        return edit_pet_desc;
    }

    public static void setEdit_pet_desc(String edit_pet_desc) {
        GLOBAL.edit_pet_desc = edit_pet_desc;
    }

}
