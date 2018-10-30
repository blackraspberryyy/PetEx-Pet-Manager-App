package com.codebusters.petexmanager.petexpetmanager;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportMissingActivity extends AppCompatActivity implements SuperTask.TaskListener{
    JSONArray jsonArray;
    JSONObject jsonObject;
    String JsonString;
    int selectedPos;
    ImageView reportpet_image;
    TextView reportpet_name;
    TextView reportpet_desc;
    TextView reportpet_breed;

    ArrayList<Adoption> pets = new ArrayList<>();
    ArrayList<String> names_list = new ArrayList<>();
    ArrayList<String> imageId_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_report_missing);
        setContentView(R.layout.activity_report_missing);

        reportpet_image = (ImageView)findViewById(R.id.reportpet_img);
        reportpet_name = (TextView)findViewById(R.id.reportpet_name);
        reportpet_desc = (TextView)findViewById(R.id.reportpet_desc);
        reportpet_breed = (TextView)findViewById(R.id.reportpet_breed);
        selectedPos = getIntent().getIntExtra("pos",-1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SuperTask.execute(ReportMissingActivity.this, "populate_reports",MyConfig.URL_STR+"getJson_adoption");
    }

    public void btn_cancel_report(View view) {
        finish();
    }

    public void btn_submit_report(View view) {
        //DO SUPERTASK ID: report_missing
        SuperTask.execute(ReportMissingActivity.this, "report_missing",MyConfig.URL_STR+"report_pet_missing");
        Toast.makeText(ReportMissingActivity.this, "Your pet has been reported as missing", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onTaskRespond(String id, String json) {
        switch(id){
            case "populate_reports":{
                if(selectedPos != -1){
                    try{
                        jsonObject = new JSONObject(json);
                        jsonArray = jsonObject.getJSONArray("adoption");
                        int i = 0;
                        while (jsonArray.length() > i) {
                            jsonObject = jsonArray.getJSONObject(i);

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
                                    user_updated_at));
                            i++;
                        }

                        Picasso.with(getApplicationContext()).load(MyConfig.BASE_URL + String.valueOf(pets.get(selectedPos).getPet_picture())).into(reportpet_image);
                        reportpet_name.setText(String.valueOf(pets.get(selectedPos).getPet_name()));
                        reportpet_desc.setText(String.valueOf(pets.get(selectedPos).getPet_description()));
                        reportpet_breed.setText(String.valueOf(pets.get(selectedPos).getPet_breed()));
                        GLOBAL.setReport_missing_adoption_id(String.valueOf(pets.get(selectedPos).getAdoption_id()));
                    }catch (Exception e){
                        //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                break;
            }
            case "report_missing":{
                //DO REPORT MISSING
                try{

                    jsonObject = new JSONObject(json);
                    Boolean success = jsonObject.getBoolean("success");
                    String result = jsonObject.getString("result");

                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                    finish();
                }
                catch(Exception e){
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
                case "populate_reports" :{
                    contentValues.put("user_id", GLOBAL.getCurrent_user_login());
                    return contentValues;
                }
                case "report_missing" :{
                    contentValues.put("adoption_id", GLOBAL.getReport_missing_adoption_id());
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
