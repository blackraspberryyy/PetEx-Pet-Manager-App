package com.codebusters.petexmanager.petexpetmanager;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class AssignPetexTag extends AppCompatActivity implements SuperTask.TaskListener{

    JSONArray jsonArray;
    JSONObject jsonObject;
    ArrayList<Adoption> pets = new ArrayList<>();

    int selectedPos;
    Adoption selectedPet;

    TextView assign_nfc_header_txt;
    BootstrapThumbnail assign_nfc_image;


    String detectedNfcTag;
    ArrayList<Adoption> all_pets = new ArrayList<>();
    private final String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(),
                    Ndef.class.getName()
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_assign_nfc);

        selectedPos = getIntent().getIntExtra("pos", -1);
        //start_scanning_button = findViewById(R.id.start_scanning_button);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            String hold_nfc = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));

            Boolean nfcExisted = false;

            for(int i = 0; i < all_pets.size();i++){
                if(all_pets.get(i).getPet_nfc_tag().equals(hold_nfc)){
                    //NFC EXIST
                    nfcExisted = true;
                    break;
                }
            }

            if(!nfcExisted){
                detectedNfcTag = hold_nfc;

                SuperTask.execute(AssignPetexTag.this, "assign_pet",MyConfig.URL_STR+"assign_nfc_to_pet");
                finish();
            }else{

                Toast.makeText(AssignPetexTag.this, "This NFC is already used.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SuperTask.execute(AssignPetexTag.this, "populate_mypets",MyConfig.URL_STR+"getJson_adoption");
        SuperTask.execute(AssignPetexTag.this, "get_all_pet",MyConfig.URL_STR+"getJson_all_adoption");

        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
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
                    selectedPet = pets.get(selectedPos);
                    assign_nfc_header_txt = findViewById(R.id.assign_nfc_header);
                    assign_nfc_image = findViewById(R.id.assign_nfc_image);

                    Picasso.with(getApplicationContext()).load(MyConfig.BASE_URL + String.valueOf(selectedPet.getPet_picture())).into(assign_nfc_image);
                    assign_nfc_header_txt.setText("Assign "+ selectedPet.getPet_name() + " an NFC tag" );


                }catch (Exception e){
                    //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            }
            case "assign_pet":{
                Toast.makeText(AssignPetexTag.this, "Successfully assigned an NFC Tag",Toast.LENGTH_SHORT).show();
                //finish();
                break;
            }
            case "get_all_pet":{
                try{
                    jsonObject = new JSONObject(json);
                    jsonArray = jsonObject.getJSONArray("adoption");
                    int i = 0;
                    all_pets.clear();
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

                        all_pets.add(new Adoption(adoption_id,
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

                    //DO SOMETHING HERE

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
                case "assign_pet":{
                    contentValues.put("pet_id", selectedPet.getPet_id());
                    contentValues.put("pet_nfc_tag", detectedNfcTag);
                    contentValues.put("user_id", GLOBAL.getCurrent_user_login());
                    return contentValues;
                }
                case "get_all_pet":{
                    return contentValues;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
