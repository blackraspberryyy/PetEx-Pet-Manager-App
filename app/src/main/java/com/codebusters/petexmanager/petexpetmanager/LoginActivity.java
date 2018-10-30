package com.codebusters.petexmanager.petexpetmanager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements SuperTask.TaskListener{

    //ID's to identify REQUEST NFC permission request
    private static final int READ_CONTACTS= 0;
    public static final int PERMISSION_CODE = 200;

    //UI VAIRABLES
    Button mEmailSignInButton;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private View mImageHeaderView;
    private View mImageFooterView;

    private UserLoginTask mAuthTask = null;

    //Current USER ID
    Boolean logged_success;
    String logged_result;
    String logged_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TypefaceProvider.registerDefaultIconSets();

        logged_success = false;
        logged_result = "";
        logged_user_id = "0";

        //Ask Permission if Permissions is not granted.
        askPermission();

        //FINDING VIEWS
        mLoginFormView = findViewById(R.id.login_form);
        mImageHeaderView = findViewById(R.id.image_header);
        mImageFooterView = findViewById(R.id.image_footer);
        mProgressView = findViewById(R.id.login_progress);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        //ASSIGNING LISTENERS
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //DO SOMETHING ON CLICK
                attemptLogin();
            }
        });

    }

    //onRequestPermissionsResult FUNCTION
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                boolean internetGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean nfcGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean locationCoarseGranted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean locationFineGranted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                break;
            }
        }
    }

    //ASKS PERMISSION
    public void askPermission(){
        String[] perms = {"android.permission.INTERNET", "android.permission.NFC", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
        ActivityCompat.requestPermissions(this, perms, PERMISSION_CODE);
    }

    //Use "hasPermission(String permission)" to check if teh permission is granted.
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String permission){
        if(isMarshmallow()){
            return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    private boolean isMarshmallow(){
        return (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mImageHeaderView.setVisibility(show ? View.GONE : View.VISIBLE);
            mImageHeaderView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mImageHeaderView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mImageFooterView.setVisibility(show ? View.GONE : View.VISIBLE);
            mImageFooterView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mImageFooterView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mImageHeaderView.setVisibility(show ? View.GONE : View.VISIBLE);
            mImageFooterView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * ATTEMPTING LOGIN
     */

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check if the user entered none.
        if (!isUsernameValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            SuperTask.execute(LoginActivity.this, "validate_user", MyConfig.URL_STR + "validate_user");

            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute();
        }
    }



    /**
     * USERNAME AND PASSWORD VALIDATION
     */

    private boolean isUsernameValid(String password) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return true;
    }


    /**
     * USER AUTHENTICATION CLASS
     */

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                // Simulate network access.
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return false;
            }
            return logged_success;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Toast.makeText(getApplicationContext(),"Welcome!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), logged_result, Toast.LENGTH_SHORT).show();
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * VALIDATE USER EMAIL AND PASS
     */

    /**
     * POSTING AND READING JSON
     */

    @Override
    public void onTaskRespond(String id, String json) {
            switch (id){
                case "validate_user":{
                    try{
                        JSONObject jsonObject = new JSONObject(json);
                        logged_success = jsonObject.getBoolean("success");
                        logged_result = jsonObject.getString("result");
                        logged_user_id = jsonObject.getString("user_id");

                        /*SETS THE USER ID*/
                        GLOBAL.setCurrent_user_login(Integer.parseInt(logged_user_id));

                    }catch(Exception e){
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
                case "validate_user" :{
                    contentValues.put("username", mEmailView.getText().toString());
                    contentValues.put("password", mPasswordView.getText().toString());
                    return contentValues;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

}