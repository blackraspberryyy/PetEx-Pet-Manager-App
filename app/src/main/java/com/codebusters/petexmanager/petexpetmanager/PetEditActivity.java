package com.codebusters.petexmanager.petexpetmanager;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PetEditActivity extends AppCompatActivity implements SuperTask.TaskListener{

    JSONArray jsonArray;
    JSONObject jsonObject;
    String JsonString;

    ArrayList<Adoption> pets = new ArrayList<>();

    int selectedPos;

    /*UI ELEMENTS*/
    ImageView editpet_img;
    TextView editpet_name;
    TextView editpet_desc;
    BootstrapButton edit_upload_button;
    BootstrapButton edit_back_button;
    BootstrapButton edit_save_changes_button;

    static final int PICK_IMAGE_REQUEST = 1;
    String filePath;
    int pet_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_pet_edit);


        selectedPos = getIntent().getIntExtra("pos", -1);

        editpet_img = (ImageView) findViewById(R.id.editpet_img);
        editpet_name = (TextView) findViewById(R.id.editpet_name);
        editpet_desc = (TextView) findViewById(R.id.editpet_desc);
        edit_upload_button = findViewById(R.id.edit_upload_button);
        edit_back_button = findViewById(R.id.edit_back_button);
        edit_save_changes_button = findViewById(R.id.edit_save_changes_button);


        SuperTask.execute(PetEditActivity.this, "populate_mypets",MyConfig.URL_STR+"getJson_adoption");
        edit_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        edit_upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBrowse();
            }
        });

        edit_save_changes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EDIT PET INFO
                imageUpload(filePath);
            }
        });
    }

    private void imageBrowse() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == PICK_IMAGE_REQUEST){
                Uri picUri = data.getData();
                filePath = getPath(picUri);
                editpet_img.setImageURI(picUri);
            }
        }
    }

    // Get Path of selected image
    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    //image_upload
    private void imageUpload(final String imagePath) {
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, MyConfig.URL_STR + "edit_pet",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String message = jObj.getString("message");

                            Boolean success = jObj.getBoolean("success");
                            if(success){
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR HERE : ", error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        if (filePath != null) {
            smr.addFile("pet_picture", imagePath);
        }
        smr.addStringParam("pet_id" ,String.valueOf(GLOBAL.getEdit_pet_id()));
        smr.addStringParam("user_id" ,String.valueOf(GLOBAL.getCurrent_user_login()));
        smr.addStringParam("pet_name", editpet_name.getText().toString());
        smr.addStringParam("pet_desc", editpet_desc.getText().toString());
        VolleyApplication.getInstance().addToRequestQueue(smr);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onTaskRespond(String id, String json) {
        switch(id){
            case "populate_mypets":{
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

                    Picasso.with(getApplicationContext()).load(MyConfig.BASE_URL + String.valueOf(pets.get(selectedPos).getPet_picture())).into(editpet_img);
                    editpet_name.setText(String.valueOf(pets.get(selectedPos).getPet_name()));
                    editpet_desc.setText(String.valueOf(pets.get(selectedPos).getPet_description()));

                    GLOBAL.setEdit_pet_id(pets.get(selectedPos).getPet_id());
                    GLOBAL.setEdit_pet_name(pets.get(selectedPos).getPet_name());
                    GLOBAL.setEdit_pet_desc(pets.get(selectedPos).getPet_description());

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
