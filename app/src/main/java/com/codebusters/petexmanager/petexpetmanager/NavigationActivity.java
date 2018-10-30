package com.codebusters.petexmanager.petexpetmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SuperTask.TaskListener {

    /*Holds the View Flipper Layout*/
    ViewFlipper viewFlipper;

    Context mContext;

    CircleImageView mUser_image;
    TextView mUser_name;
    TextView mUser_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        /*Getting reference for the content navigation layout*/
        viewFlipper = (ViewFlipper) findViewById(R.id.app_bar_include).findViewById(R.id.vf);

        mContext = this.getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SuperTask.execute(NavigationActivity.this, "logout", MyConfig.URL_STR + "logout");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_mypets) {
            disableReadNfc();
            viewFlipper.setDisplayedChild(0);
            //new getAdoptionData().execute();
            SuperTask.execute(NavigationActivity.this, "populate_mypets", MyConfig.URL_STR+"getJson_adoption");
        } else if (id == R.id.nav_newsandnotif){
            disableReadNfc();
            viewFlipper.setDisplayedChild(1);
            SuperTask.execute(NavigationActivity.this, "populate_newsnotif", MyConfig.URL_STR+"getJson_adoption_isMissing");
        } else if (id == R.id.nav_scannfc) {

            viewFlipper.setDisplayedChild(2);
        }
        /*
        else if (id == R.id.nav_assign_nfc) {
            viewFlipper.setDisplayedChild(3);
        }

        else if (id == R.id.nav_settings) {
            intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_contactus) {
            intent = new Intent(getApplicationContext(), ContactusActivity.class);
            startActivity(intent);
        }
        */
        else if (id == R.id.nav_about) {
            disableReadNfc();
            intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SuperTask.execute(NavigationActivity.this, "populate_mypets", MyConfig.URL_STR+"getJson_adoption");
        SuperTask.execute(NavigationActivity.this, "get_user", MyConfig.URL_STR+"getJson_user");
        SuperTask.execute(NavigationActivity.this, "get_all_pets", MyConfig.URL_STR+"getJson_all_adoption");
    }

    public void disableReadNfc(){
        //STOP NFC SCANNING
        Button btn_startScanning = (Button) findViewById(R.id.startButton);

        btn_startScanning.setText("Start Scanning");
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    /*=============================== MY PETS ===============================*/
    JSONArray jsonArray;
    JSONObject jsonObject;
    GridView gridView;
    ArrayList<Adoption> pets = new ArrayList<>();

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mypet_contextmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem itm){
        Intent intent;
        AdapterView.AdapterContextMenuInfo info;
        int listPosition;

        switch(itm.getItemId()){
            case (R.id.view_details):
                info = (AdapterView.AdapterContextMenuInfo) itm.getMenuInfo();
                listPosition = info.position;
                intent = new Intent(NavigationActivity.this, PetPreviewActivity.class);
                intent.putExtra("pos", listPosition);
                startActivity(intent);
                return true;
            case (R.id.edit_details):
                info = (AdapterView.AdapterContextMenuInfo) itm.getMenuInfo();
                listPosition = info.position;
                intent = new Intent(NavigationActivity.this, PetEditActivity.class);
                intent.putExtra("pos", listPosition);
                startActivity(intent);
                return true;
            case (R.id.report_missing):
                info = (AdapterView.AdapterContextMenuInfo) itm.getMenuInfo();
                listPosition = info.position;
                if(pets.get(listPosition).getAdoption_isMissing()){
                    Toast.makeText(getApplicationContext(), "This pet is previously reported to be lost.", Toast.LENGTH_SHORT).show();
                }else{
                    intent = new Intent(NavigationActivity.this, ReportMissingActivity.class);
                    intent.putExtra("pos", listPosition);
                    startActivity(intent);
                }
                return true;
            case (R.id.assign_petex_tag):
                info = (AdapterView.AdapterContextMenuInfo) itm.getMenuInfo();
                listPosition = info.position;
                intent = new Intent(NavigationActivity.this, AssignPetexTag.class);
                intent.putExtra("pos", listPosition);
                startActivity(intent);
                return true;

            case (R.id.pet_updates):
                info = (AdapterView.AdapterContextMenuInfo) itm.getMenuInfo();
                listPosition = info.position;
                intent = new Intent(NavigationActivity.this, PetUpdates.class);
                intent.putExtra("pos", listPosition);
                startActivity(intent);
                return true;
            default: return super.onContextItemSelected(itm);
        }
    }

    /*=======================================================================*/
    /*============================ NEWS AND NOTIF ===========================*/
    ArrayList<MissingPetDetails> missingPets = new ArrayList<>();
    ListView listView;

    public static class MissingPetDetails{
        String modalpet_img;
        String modalpet_name;
        String modalpet_desc;
        String modalpet_breed;
        String modalowner_img;
        String modalowner_name;
        String modalowner_contactno;
        String modalowner_address;
        String modalowner_email;

        public MissingPetDetails(String modalpet_img, String modalpet_name, String modalpet_desc, String modalpet_breed, String modalowner_img, String modalowner_name, String modalowner_contactno, String modalowner_address, String modalowner_email) {
            this.modalpet_img = modalpet_img;
            this.modalpet_name = modalpet_name;
            this.modalpet_desc = modalpet_desc;
            this.modalpet_breed = modalpet_breed;
            this.modalowner_img = modalowner_img;
            this.modalowner_name = modalowner_name;
            this.modalowner_contactno = modalowner_contactno;
            this.modalowner_address = modalowner_address;
            this.modalowner_email = modalowner_email;
        }
    }


    public String petType(String str){
        return str.equals("canine") ? "dog" : "cat";
    }

    public static class MissingPetDetailDialogFragment extends DialogFragment {

        static MissingPetDetailDialogFragment newInstance(MissingPetDetails missingPetDetails){
            MissingPetDetailDialogFragment f = new MissingPetDetailDialogFragment();
            Bundle args = new Bundle();
            args.putString("modalpet_img", missingPetDetails.modalpet_img);
            args.putString("modalpet_name", missingPetDetails.modalpet_name);
            args.putString("modalpet_desc", missingPetDetails.modalpet_desc);
            args.putString("modalpet_breed", missingPetDetails.modalpet_breed);
            args.putString("modalowner_img", missingPetDetails.modalowner_img);
            args.putString("modalowner_name", missingPetDetails.modalowner_name);
            args.putString("modalowner_contactno", missingPetDetails.modalowner_contactno);
            args.putString("modalowner_address", missingPetDetails.modalowner_address);
            args.putString("modalowner_email", missingPetDetails.modalowner_email);
            f.setArguments(args);
            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.missingpet_detail_layout, null);

            TextView modalpet_name = (TextView) v.findViewById(R.id.modalpet_name);
            ImageView modalpet_img = (ImageView) v.findViewById(R.id.modalpet_img);
            TextView modalpet_desc = (TextView) v.findViewById(R.id.modalpet_desc);
            TextView modalpet_breed = (TextView) v.findViewById(R.id.modalpet_breed);
            ImageView modalowner_img = (ImageView) v.findViewById(R.id.modalowner_img);
            TextView modalowner_name = (TextView) v.findViewById(R.id.modalowner_name);
            TextView modalowner_contactno = (TextView) v.findViewById(R.id.modalowner_contactno);
            TextView modalowner_address = (TextView) v.findViewById(R.id.modalowner_address);
            TextView modalowner_email = (TextView) v.findViewById(R.id.modalowner_email);

            modalpet_name.setText(getArguments().getString("modalpet_name"));
            Picasso.with(v.getContext()).load(MyConfig.BASE_URL + getArguments().getString("modalpet_img")).into(modalpet_img);
            modalpet_desc.setText(getArguments().getString("modalpet_desc"));
            modalpet_breed.setText(getArguments().getString("modalpet_breed"));
            Picasso.with(v.getContext()).load(MyConfig.BASE_URL + getArguments().getString("modalowner_img")).into(modalowner_img);
            modalowner_name.setText(getArguments().getString("modalowner_name"));
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

    public void missingpet_details(MissingPetDetails missingPetDetails){
        DialogFragment missingPetDetail = MissingPetDetailDialogFragment.newInstance(missingPetDetails);
        missingPetDetail.show(getFragmentManager(), "missingPetDetail");
    }


    /*=======================================================================*/
    /*========================= SCAN PETEX TAG ==============================*/
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

    public void startScanning(View v){
        Button btn_startScanning = (Button) findViewById(R.id.startButton);

        if(String.valueOf(btn_startScanning.getText()).equals("Cancel")){
            btn_startScanning.setText("Start Scanning");
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            nfcAdapter.disableForegroundDispatch(this);
        }else if(String.valueOf(btn_startScanning.getText()).equals("Start Scanning")){
            btn_startScanning.setText("Cancel");
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
    }

    @Override
    protected void onNewIntent(Intent intent) {

        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            String hold_nfc = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));

            Boolean nfcExisted = false;
            Adoption detectedPet = null;
            for(int i = 0; i < all_pets.size();i++){
                if(all_pets.get(i).getPet_nfc_tag().equals(hold_nfc)){
                    //NFC EXIST
                    nfcExisted = true;
                    detectedPet = all_pets.get(i);
                    break;
                }
            }

            if(!nfcExisted){
                Toast.makeText(NavigationActivity.this, "Cannot find a pet with this NFC",Toast.LENGTH_SHORT).show();
            }else{
                Intent new_intent = new Intent(getApplicationContext(), DetectedPet.class);
                GLOBAL.setDetected_adoption_id(detectedPet.getAdoption_id());
                startActivity(new_intent);
            }
        }
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

    /*=======================================================================*/
    /*========== ASYNCTASKS FOR GETTING DATA FROM DB in JSON FORMAT =========*/


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
                                user_updated_at));
                        i++;
                    }

                    CustomGrid adapter = new CustomGrid(NavigationActivity.this, pets);

                    gridView = (GridView) findViewById(R.id.petGrid);

                    //grid.setAdapter(null);
                    gridView.setAdapter(adapter);

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //Toast.makeText(NavigationActivity.this, "You have clicked " + position, Toast.LENGTH_SHORT).show();
                        }
                    });
                    registerForContextMenu(gridView);

                }catch (Exception e){
                    //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            }
            case "populate_newsnotif":{
                ArrayList<Adoption> adoption = new ArrayList<>();
                ArrayList<String> missingpet_name = new ArrayList<>();
                ArrayList<String> missingpet_img = new ArrayList<>();
                ArrayList<String> missingpet_desc = new ArrayList<>();
                missingPets.clear();
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

                        adoption.add(new Adoption(adoption_id,
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

                        missingpet_name.add(pet_name);
                        missingpet_img.add(MyConfig.BASE_URL + pet_picture);
                        missingpet_desc.add("A " + petType(pet_specie) + " is lost! It is " + pet_description);
                        missingPets.add(new MissingPetDetails(pet_picture, pet_name, pet_description, pet_breed, user_picture, user_firstname + " " + user_lastname, user_contact_no, user_address, user_email));
                        i++;
                    }

                    listView = (ListView) findViewById(R.id.missingpet_list);
                    CustomList customList = new CustomList(NavigationActivity.this, missingpet_img, missingpet_name, missingpet_desc);
                    listView.setAdapter(customList);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            missingpet_details(missingPets.get(position));
                            //Toast.makeText(getApplicationContext(), "Position : " + String.valueOf(position), Toast.LENGTH_LONG).show();
                        }
                    });
                }catch (Exception e){
                    //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            }
            case "get_user":{
                try{
                    jsonObject = new JSONObject(json);
                    jsonArray = jsonObject.getJSONArray("user");
                    int i = 0;
                    jsonObject = jsonArray.getJSONObject(i);
                    jsonObject = jsonArray.getJSONObject(i);
                    int user_id = jsonObject.getInt("user_id");

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

                    /*SET NAVIGATION USER DETAIL*/
                    mUser_image = (CircleImageView)findViewById(R.id.user_image);
                    mUser_name = (TextView)findViewById(R.id.user_name);
                    mUser_email = (TextView)findViewById(R.id.user_email);
                    Picasso.with(getApplicationContext()).load(MyConfig.BASE_URL + String.valueOf(user_picture)).into(mUser_image);
                    mUser_name.setText(String.valueOf(user_firstname + " " + user_lastname));
                    mUser_email.setText(String.valueOf(user_email));

                }catch (Exception e){
                    //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                break;
            }
            case "get_all_pets":{
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
            case "logout":{
                GLOBAL.clear();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
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
                case "populate_newsnotif" : {
                    return contentValues;
                }
                case "get_user":{
                    contentValues.put("user_id", GLOBAL.getCurrent_user_login());
                    return contentValues;
                }
                case "get_all_pets":{
                    return contentValues;
                }
                case "logout":{
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
