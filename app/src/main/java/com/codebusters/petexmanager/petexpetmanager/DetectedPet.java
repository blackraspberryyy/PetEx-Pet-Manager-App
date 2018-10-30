package com.codebusters.petexmanager.petexpetmanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetectedPet extends AppCompatActivity implements SuperTask.TaskListener {

    int detectedPet_id;

    JSONArray jsonArray;
    JSONObject jsonObject;
    Adoption detected_pet;

    /*UI ELEMENTS*/
    TextView detected_pet_name;
    BootstrapThumbnail detected_pet_img;
    TextView detected_pet_desc;
    TextView detected_pet_breed;
    TextView detected_pet_owner;
    TextView detected_pet_contact_no;
    CircleImageView detected_pet_owner_img;
    TextView detected_pet_isfound;

    /*MAP VARIABLES*/
    Location location;
    LocationManager locationManager;
    LocationListener locationListener;

    String currLoc;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detected_pet);
        TypefaceProvider.registerDefaultIconSets();

        detectedPet_id = GLOBAL.getDetected_adoption_id();
        SuperTask.execute(DetectedPet.this, "get_adoption",MyConfig.URL_STR + "getJson_adoption_by_id");




    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onTaskRespond(String id, String json) {
        switch(id){
            case "get_adoption":{
                try{
                    jsonObject = new JSONObject(json);
                    jsonArray = jsonObject.getJSONArray("adoption");
                    int i = 0;
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


                    detected_pet = new Adoption(adoption_id,
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
                    );

                    //DO SOMETHING HERE
                    detected_pet_img = findViewById(R.id.detected_pet_img);
                    detected_pet_name = findViewById(R.id.detected_pet_name);
                    detected_pet_desc = findViewById(R.id.detected_pet_desc);
                    detected_pet_breed = findViewById(R.id.detected_pet_breed);
                    detected_pet_owner = findViewById(R.id.detected_pet_owner);
                    detected_pet_contact_no = findViewById(R.id.detected_pet_contact_no);
                    detected_pet_owner_img = findViewById(R.id.detected_pet_owner_img);
                    detected_pet_isfound = findViewById(R.id.detected_pet_isfound);

                    Picasso.with(getApplicationContext()).load(MyConfig.BASE_URL + String.valueOf(detected_pet.getPet_picture())).into(detected_pet_img);
                    detected_pet_name.setText(String.valueOf(detected_pet.getPet_name()));
                    detected_pet_desc.setText(String.valueOf(detected_pet.getPet_description()));
                    detected_pet_breed.setText(String.valueOf(detected_pet.getPet_breed()));
                    Picasso.with(getApplicationContext()).load(MyConfig.BASE_URL + String.valueOf(detected_pet.getUser_picture())).into(detected_pet_owner_img);
                    detected_pet_owner.setText(String.valueOf(detected_pet.getUser_firstname() + " " + detected_pet.getUser_lastname()));
                    detected_pet_contact_no.setText(String.valueOf(detected_pet.getUser_contact_no()));

                    if(detected_pet.getAdoption_isMissing()){
                        detected_pet_isfound.setText("You have found "+detected_pet.getPet_name()+" pet!");
                        Toast.makeText(getApplicationContext(),"You have found "+ detected_pet.getPet_name() + "!", Toast.LENGTH_SHORT).show();
                        //GETTING LATITUDE and LONGITUDE OF THE SCANNER
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            //PERMISSION NOT CHECKED
                            Toast.makeText(getApplicationContext(), "Please Check Permission First", Toast.LENGTH_SHORT).show();
                        } else {
                            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();

                            locationListener = new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    longitude = location.getLongitude();
                                    latitude = location.getLatitude();

                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle extras) {

                                }

                                @Override
                                public void onProviderEnabled(String provider) {

                                }

                                @Override
                                public void onProviderDisabled(String provider) {

                                }
                            };
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
                        }
                        Log.d("LATITUDE : ", String.valueOf(latitude));
                        Log.d("LONGITUDE : ", String.valueOf(longitude));

                        SuperTask.execute(DetectedPet.this, "pet_discovered",MyConfig.URL_STR+"add_to_discovery");

                    }else{
                        detected_pet_isfound.setText("");
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
            case "pet_discovered":{
                try{
                    jsonObject = new JSONObject(json);
                    Boolean success = jsonObject.getBoolean("success");
                    String result = jsonObject.getString("result");
                    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public ContentValues setRequestValues(String id, ContentValues contentValues) {
        switch(id){
            case "get_adoption":{
                contentValues.put("adoption_id", detectedPet_id);
                return contentValues;
            }
            case "pet_discovered":{
                contentValues.put("latitude", latitude);
                contentValues.put("longitude", longitude);
                contentValues.put("user_id", GLOBAL.getCurrent_user_login());
                contentValues.put("pet_id", detected_pet.getPet_id());
                return contentValues;
            }
        }
        return null;
    }
}
