package com.codebusters.petexmanager.petexpetmanager;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PetPreviewActivity extends AppCompatActivity implements SuperTask.TaskListener{

    JSONArray jsonArray;
    JSONObject jsonObject;

    ArrayList<Adoption> pets = new ArrayList<>();

    int selectedPos;
    ImageView prevpet_img;
    TextView prevpet_name;
    TextView prevpet_name2;
    TextView prevpet_specie;
    TextView prevpet_neutered_spayed;
    TextView prevpet_breed;
    TextView prevpet_sex;
    TextView prevpet_age;
    TextView prevpet_bday;
    TextView prevpet_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_preview);

        selectedPos = getIntent().getIntExtra("pos", -1);

        prevpet_img = (ImageView) findViewById(R.id.prevpet_img);
        prevpet_name = (TextView) findViewById(R.id.prevpet_name);
        prevpet_name2 = (TextView) findViewById(R.id.prevpet_name2);
        prevpet_specie = (TextView) findViewById(R.id.prevpet_specie);
        prevpet_neutered_spayed = (TextView) findViewById(R.id.prevpet_neutered_spayed);
        prevpet_breed = (TextView) findViewById(R.id.prevpet_breed);
        prevpet_sex = (TextView) findViewById(R.id.prevpet_sex);
        prevpet_age = (TextView) findViewById(R.id.prevpet_age);
        prevpet_bday = (TextView) findViewById(R.id.prevpet_bday);
        prevpet_desc = (TextView) findViewById(R.id.prevpet_desc);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SuperTask.execute(PetPreviewActivity.this, "populate_mypets",MyConfig.URL_STR+"getJson_adoption");
    }


    public int calculateAge(String date){
        Calendar today = Calendar.getInstance();
        Calendar birthdate = Calendar.getInstance();
        int age = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        Date convertedDate = new Date();

        try{
            convertedDate = dateFormat.parse(date);
        }catch (ParseException e){
            e.printStackTrace();
        }

        birthdate.setTime(convertedDate);
        if(birthdate.after(today)){
            throw new IllegalArgumentException("Can't be born in the future");
        }
        age = today.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);

        if (birthdate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3 || (birthdate.get(Calendar.MONTH) > today.get(Calendar.MONTH))){
            age--;
        }else if((birthdate.get(Calendar.MONTH) == today.get(Calendar.MONTH)) && (birthdate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH))){
            age--;
        }
        return age;
    }


    @Override
    public void onTaskRespond(String id, String json) {
        switch(id){
            case "populate_mypets" :{
                try{
                    jsonObject = new JSONObject(json);
                    jsonArray = jsonObject.getJSONArray("adoption");
                    int i = 0;
                    pets.clear();
                    while (jsonArray.length() > i) {
                        jsonObject = jsonArray.getJSONObject(i);

                        int adoption_id = jsonObject.getInt("adoption_id");
                        int pet_id = jsonObject.getInt("adoption.pet_id");
                        int user_id = jsonObject.getInt("adoption.user_id");
                        String adoption_proof_img = jsonObject.getString("adoption_proof_img");
                        Boolean adoption_isRead = jsonObject.getInt("adoption_isRead") == 1;
                        Boolean adoption_isMissing = jsonObject.getInt("adoption_isMissing") == 1;
                        int adoption_adopted_at = jsonObject.getInt("adoption_adopted_at");

                        String user_firstname = jsonObject.getString("user_firstname");
                        String user_lastname = jsonObject.getString("user_lastname");
                        String user_username = jsonObject.getString("user_username");
                        String user_password = jsonObject.getString("user_password");
                        int user_bday = jsonObject.getInt("user_bday");

                        String user_sex = jsonObject.getString("user_sex");
                        Boolean user_status = jsonObject.getInt("user_status") == 1;
                        String user_email = jsonObject.getString("user_email");
                        String user_verification_code = jsonObject.getString("user_verification_code");

                        Boolean user_isverified = jsonObject.getInt("user_isverified") == 1;
                        String user_contact_no = jsonObject.getString("user_contact_no");
                        String user_picture = jsonObject.getString("user_picture");
                        String user_address = jsonObject.getString("user_address");

                        int user_added_at = jsonObject.getInt("user_added_at");
                        int user_updated_at = jsonObject.getInt("user_updated_at");
                        String pet_nfc_tag = jsonObject.getString("pet_nfc_tag");
                        String pet_name = jsonObject.getString("pet_name");
                        int pet_bday = jsonObject.getInt("pet_bday");

                        String pet_specie = jsonObject.getString("pet_specie");
                        String pet_sex = jsonObject.getString("pet_sex");
                        String pet_breed = jsonObject.getString("pet_breed");
                        String pet_size = jsonObject.getString("pet_size");
                        String pet_status = jsonObject.getString("pet_status");

                        Boolean pet_access = jsonObject.getInt("pet_access") == 1;
                        Boolean pet_neutered_spayed = jsonObject.getInt("pet_neutered_spayed") == 1;
                        String pet_admission = jsonObject.getString("pet_admission");
                        String pet_description = jsonObject.getString("pet_description");
                        String pet_history = jsonObject.getString("pet_history");

                        String pet_picture = jsonObject.getString("pet_picture");
                        String pet_video = jsonObject.getString("pet_video");
                        int pet_added_at = jsonObject.getInt("pet_added_at");
                        int pet_updated_at = jsonObject.getInt("pet_updated_at");

                        pets.add(new Adoption(adoption_id,
                                pet_id,
                                user_id,
                                adoption_proof_img,
                                adoption_isRead,
                                adoption_isMissing,
                                adoption_adopted_at,
                                pet_nfc_tag,
                                pet_name,
                                pet_bday,
                                pet_specie,
                                pet_sex,
                                pet_breed,
                                pet_size,
                                pet_status,
                                pet_access,
                                pet_neutered_spayed,
                                pet_admission,
                                pet_description,
                                pet_history,
                                pet_picture,
                                pet_video,
                                pet_added_at,
                                pet_updated_at,
                                user_firstname,
                                user_lastname,
                                user_username,
                                user_password,
                                user_bday,
                                user_sex,
                                user_status,
                                user_email,
                                user_verification_code,
                                user_isverified,
                                user_contact_no,
                                user_picture,
                                user_address,
                                user_added_at,
                                user_updated_at
                        ));
                        i++;
                    }

                    Picasso.with(getApplicationContext()).load(MyConfig.BASE_URL + String.valueOf(pets.get(selectedPos).getPet_picture())).into(prevpet_img);
                    prevpet_name.setText(String.valueOf(pets.get(selectedPos).getPet_name()));
                    prevpet_name2.setText(String.valueOf(pets.get(selectedPos).getPet_name()));
                    prevpet_specie.setText(String.valueOf(pets.get(selectedPos).getPet_specie().equals("Canine") ? "Dog" : "Cat"));
                    prevpet_neutered_spayed.setText(String.valueOf(pets.get(selectedPos).getPet_neutered_spayed() ? "Yes" : "No"));
                    prevpet_breed.setText(String.valueOf(pets.get(selectedPos).getPet_breed()));
                    prevpet_sex.setText(String.valueOf(pets.get(selectedPos).getPet_sex()));
                    prevpet_desc.setText(String.valueOf(pets.get(selectedPos).getPet_description()));
            /*Data Calculations*/
                    long dv = Long.valueOf(String.valueOf(pets.get(selectedPos).getPet_bday())) * 1000;
                    Date df = new java.util.Date(dv);
                    String pet_bday = new SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(df);
                    int age = calculateAge(pet_bday);
            /**/
                    prevpet_age.setText(String.valueOf(age));
                    prevpet_bday.setText(pet_bday);


                }catch (Exception e){
                    //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public ContentValues setRequestValues(String id, ContentValues contentValues) {
        try{
            switch (id){
                case "populate_mypets" :{
                    contentValues.put("user_id", GLOBAL.getCurrent_user_login());
                    return contentValues;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
