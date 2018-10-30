package com.codebusters.petexmanager.petexpetmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PetUpdates extends AppCompatActivity implements OnMapReadyCallback, SuperTask.TaskListener{

    private GoogleMap mMap;
    private LatLngBounds mDiscoveriesLatLng;
    int selectedPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_updates);

        //FRAGMENT OF MAP
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        SuperTask.execute(PetUpdates.this, "populate_mypets",MyConfig.URL_STR+"getJson_adoption");
    }

    public void foundPet(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Your pet has found").setMessage("Tap \"ok\" if you get your pet.");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //PET IS FOUND
                SuperTask.execute(PetUpdates.this, "pet_found", MyConfig.URL_STR + "pet_found");
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //DO NOTHING
            }
        });
        alert.show();
    }

    public static class FindersDetails{
        String modalpet_img;
        String modalpet_name;
        String modalpet_desc;
        String modalpet_breed;
        String modalowner_img;
        String modalfinder_location;
        String modalowner_name;
        String modalowner_contactno;
        String modalowner_address;
        String modalowner_email;

        public FindersDetails(String modalpet_img, String modalpet_name, String modalpet_desc, String modalpet_breed, String modalowner_img, String modalfinder_location, String modalowner_name, String modalowner_contactno, String modalowner_address, String modalowner_email) {
            this.modalpet_img = modalpet_img;
            this.modalpet_name = modalpet_name;
            this.modalpet_desc = modalpet_desc;
            this.modalpet_breed = modalpet_breed;
            this.modalowner_img = modalowner_img;
            this.modalfinder_location = modalfinder_location;
            this.modalowner_name = modalowner_name;
            this.modalowner_contactno = modalowner_contactno;
            this.modalowner_address = modalowner_address;
            this.modalowner_email = modalowner_email;
        }
    }

    public static class MissingPetDetailDialogFragment extends DialogFragment {

        static MissingPetDetailDialogFragment newInstance(FindersDetails findersDetails){
            MissingPetDetailDialogFragment f = new MissingPetDetailDialogFragment();
            Bundle args = new Bundle();
            args.putString("modalowner_img", findersDetails.modalowner_img);
            args.putString("modalfinder_location", findersDetails.modalfinder_location);
            args.putString("modalowner_name", findersDetails.modalowner_name);
            args.putString("modalowner_contactno", findersDetails.modalowner_contactno);
            args.putString("modalowner_address", findersDetails.modalowner_address);
            args.putString("modalowner_email", findersDetails.modalowner_email);
            f.setArguments(args);
            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.finder_detail_layout, null);

            ImageView modalowner_img = (ImageView) v.findViewById(R.id.modalowner_img);
            TextView modalowner_name = (TextView) v.findViewById(R.id.modalowner_name);
            TextView modalfinder_location = (TextView) v.findViewById(R.id.modal_founded_at);
            TextView modalowner_contactno = (TextView) v.findViewById(R.id.modalowner_contactno);
            TextView modalowner_address = (TextView) v.findViewById(R.id.modalowner_address);
            TextView modalowner_email = (TextView) v.findViewById(R.id.modalowner_email);

            Picasso.with(v.getContext()).load(MyConfig.BASE_URL + getArguments().getString("modalowner_img")).into(modalowner_img);

            modalowner_name.setText(getArguments().getString("modalowner_name"));
            modalfinder_location.setText(getArguments().getString("modalfinder_location"));
            modalowner_contactno.setText(getArguments().getString("modalowner_contactno"));
            modalowner_address.setText(getArguments().getString("modalowner_address"));
            modalowner_email.setText(getArguments().getString("modalowner_email"));


            builder.setView(v).setNegativeButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //user cancelled the dialog
                }
            });
            return builder.create();
        }
    }

    public void finder_details(FindersDetails missingPetDetails){
        DialogFragment missingPetDetail = MissingPetDetailDialogFragment.newInstance(missingPetDetails);
        missingPetDetail.show(getFragmentManager(), "missingPetDetail");
    }

    @Override
    public void onTaskRespond(String id, String json) {
        switch(id){
            case "populate_mypets":{
                try{
                    JSONObject jsonObject;
                    JSONArray jsonArray;
                    ArrayList<Adoption> pets = new ArrayList<>();

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

                    selectedPos = getIntent().getIntExtra("pos", -1);
                    GLOBAL.setUpdates_on_pet_id(pets.get(selectedPos).getPet_id());

                    BootstrapButton update_found_btn = findViewById(R.id.update_found_btn);

                    if(pets.get(selectedPos).getAdoption_isMissing()){
                        update_found_btn.setEnabled(true);
                        update_found_btn.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);

                    }else{
                        update_found_btn.setEnabled(false);
                        update_found_btn.setBootstrapBrand(DefaultBootstrapBrand.SECONDARY);
                    }
                    SuperTask.execute(PetUpdates.this, "get_discoveries",MyConfig.URL_STR+"getJson_discoveries");

                }catch (Exception e){
                    //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            }
            case "get_discoveries":{
                ArrayList<FindersDetails> finders = new ArrayList<>();
                try{
                    JSONObject jsonObject;
                    JSONArray jsonArray;

                    ArrayList<Discovery> discoveries = new ArrayList<>();
                    ArrayList<String> owner_name = new ArrayList<>();
                    ArrayList<String> owner_img = new ArrayList<>();
                    ArrayList<String> discovery_location = new ArrayList<>();

                    jsonObject = new JSONObject(json);
                    jsonArray = jsonObject.getJSONArray("discoveries");
                    int i = 0;
                    discoveries.clear();
                    owner_name.clear();
                    owner_img.clear();
                    discovery_location.clear();
                    while (jsonArray.length() > i) {
                        jsonObject = jsonArray.getJSONObject(i);

                        int discovery_id = jsonObject.getInt("discovery_id");
                        int pet_id = jsonObject.getInt("discovery.pet_id");
                        int user_id = jsonObject.getInt("discovery.user_id");
                        Double discovery_latitude = jsonObject.getDouble("discovery_latitude");
                        Double discovery_longitude = jsonObject.getDouble("discovery_longitude");
                        int discovery_added_at = jsonObject.getInt("discovery_added_at");

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

                        discoveries.add(new Discovery(discovery_id,
                                pet_id,
                                user_id,
                                discovery_latitude,
                                discovery_longitude,
                                discovery_added_at,
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

                        owner_img.add(MyConfig.BASE_URL + user_picture);
                        owner_name.add(user_firstname + " " +user_lastname);


                        //REVERSE GEOCODE
                        Geocoder geocoder = new Geocoder(PetUpdates.this);
                        List<Address> matches = geocoder.getFromLocation(discovery_latitude, discovery_longitude,1);
                        Address bestMatch = (matches.isEmpty()) ? null : matches.get(0);

                        discovery_location.add(bestMatch != null ? bestMatch.getAddressLine(0) : "Location Not Found");

                        // ADD MARKERS
                        LatLng latLng = new LatLng(discovery_latitude,discovery_longitude);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(user_firstname + " " +user_lastname));
                        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }

                        //mMap.setMyLocationEnabled(true);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));


                        finders.add(new FindersDetails(pet_picture, pet_name, pet_description, pet_breed, user_picture, bestMatch != null ? bestMatch.getAddressLine(0) : "Location Not Found",user_firstname + " " + user_lastname, user_contact_no, user_address, user_email));

                        i++;
                    }


                    ListView listView = (ListView) findViewById(R.id.discover_list);
                    CustomListDiscovery customListDiscovery = new CustomListDiscovery(PetUpdates.this, owner_img, owner_name, discovery_location);
                    listView.setAdapter(customListDiscovery);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            finder_details(finders.get(position));
                            //Toast.makeText(getApplicationContext(), "Position : " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                        }
                    });



                }catch(JSONException je){
                    // NO DISCOVERIES FOUND
                    Toast.makeText(PetUpdates.this, "No updates for this pet", Toast.LENGTH_SHORT).show();
                    finish();
                    je.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
            case "pet_found":{
                //DO REPORT MISSING
                JSONObject jsonObject;
                try{
                    jsonObject = new JSONObject(json);
                    Boolean success = jsonObject.getBoolean("success");
                    String result = jsonObject.getString("message");

                    Toast.makeText(PetUpdates.this, result, Toast.LENGTH_SHORT).show();
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
                case "populate_mypets" :{
                    contentValues.put("user_id", GLOBAL.getCurrent_user_login());
                    return contentValues;
                }
                case "get_discoveries":{
                    contentValues.put("pet_id", GLOBAL.getUpdates_on_pet_id());
                    return contentValues;
                }
                case "pet_found" :{
                    contentValues.put("pet_id",GLOBAL.getUpdates_on_pet_id());
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
