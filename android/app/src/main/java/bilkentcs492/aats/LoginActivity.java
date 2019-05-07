package bilkentcs492.aats;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "ndricim.rrapi@ug.bilkent.edu.tr:12345", "argert.boja@ug.bilkent.edu.tr:steroids"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mIDView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox mCheckBox;
    private TextView mLayout;
    AlertDialog.Builder dialog_builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mIDView = (AutoCompleteTextView) findViewById(R.id.id);
        mCheckBox = (CheckBox) findViewById(R.id.checkBox);
        mLayout =  findViewById(R.id.privacyLayout);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        dialog_builder = new AlertDialog.Builder(LoginActivity.this);
        dialog_builder.setCancelable(false);
        dialog_builder.setTitle("Privacy Policy");
        dialog_builder.setMessage(Html.fromHtml(getString(R.string.privacy_info)));

        dialog_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });


            TextView privacyButton = (TextView) findViewById(R.id.privacy_policy);
        privacyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_builder.show();
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


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
        mIDView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String id = mIDView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(id)) {
            mIDView.setError(getString(R.string.error_field_required));
            focusView = mIDView;
            cancel = true;
        }

        if(!mCheckBox.isChecked()){
            mLayout.setError("You need to agree to our privacy policies");
            focusView = mCheckBox;
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
            mAuthTask = new UserLoginTask(id, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@ug.bilkent.edu.tr") || email.contains("@bilkent.edu.tr");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
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
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String id;
        private final String mPassword;
        public static final int CONNECTION_TIMEOUT=10000;
        public static final int READ_TIMEOUT=15000;
        private String user_name ;
        private String user_surname;
        private String user_email;
        private String user_presence;
        private String user_password;
        private String professor_current_course;
        private String user_student_image;
        private String current_hour;
        private String URL = "https://bilmenu.com/aats/php/login.php";

        UserLoginTask(String id, String password) {
            this.id = id;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            return  authenticateUser(id, mPassword);

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent login_success = new Intent(LoginActivity.this, LoadDataScreen.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("user_id", id);
                mBundle.putString("user_password", user_password);
                mBundle.putString("user_email", user_email);
                mBundle.putString("user_name", user_name);
                mBundle.putString("user_surname", user_surname);
                mBundle.putString("user_presence", user_presence);
                mBundle.putString("professor_current_course", professor_current_course);
                mBundle.putString("current_hour", current_hour);
                mBundle.putString("studentImage", user_student_image);

                login_success.putExtras(mBundle);
                startActivity(login_success);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_credentials));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        // make server request for authentication, receive data and add it to variable
        private boolean authenticateUser(String id, String password) {

            ServerRequest authenticateRequest = new ServerRequest();
            // set URL
            authenticateRequest.setURL(URL);

            // set Parameters .. ex: ?id=1
            List<QueryParameter> params = new ArrayList<>();
            params.add(new QueryParameter("ID",id ));
            params.add(new QueryParameter("password",password ));
            authenticateRequest.setParams(params);

            JSONArray receiveData = authenticateRequest.requestAndFetch(LoginActivity.this);
            if(receiveData != null) {
                JSONObject json_data = null;
                String auth_result = "";
                try {
                    json_data = receiveData.getJSONObject(0);

                    auth_result = json_data.optString("response");

                } catch (final JSONException e) {
                     e.printStackTrace();
                    LoginActivity.this.runOnUiThread(new Runnable() {
                       @Override
                        public void run() {
                            Toast.makeText( LoginActivity.this, "JSONException: Error parsing JSON"+ e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    return false;
                }

                if (auth_result.equals("AUTH_FAILED")) {

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( LoginActivity.this, "WRONG CREDENTIALS", Toast.LENGTH_LONG).show();
                        }
                    });
                    return false; // authentication failed
                }else if(auth_result.equals("FAILED TO CONNECT TO MYSQL!")){

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( LoginActivity.this, "DATABASE CONNECTION ERROR, TRY LATER!", Toast.LENGTH_LONG).show();
                        }
                    });
                    return false; // authentication failed
                }else{
                    try {
                        if(json_data.optString("name") == null){
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText( LoginActivity.this, "LOGIN UNSUCCESSFUL" , Toast.LENGTH_LONG).show();
                                }
                            });
                            return false;
                        }
                        // assign data to varables
                        user_name = json_data.optString("name");
                        user_surname = json_data.getString("surname");
                        user_email = json_data.getString("email");
                        user_password = json_data.getString("password");
                        current_hour = json_data.getString("current_hour");
                        if (!json_data.getString("present").equals("-1")) {
                            user_presence = json_data.getString("present");

                            professor_current_course = json_data.optString("classID");
                            user_student_image = json_data.optString("studentImage");
                        } else {

                            user_presence = json_data.getString("present"); // user is professor, no presence
                            professor_current_course = json_data.optString("classID");

                        }
                        return true;
                    } catch (final JSONException e) {

                        e.printStackTrace();
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText( LoginActivity.this, "JSONException" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        return false;
                    }
                }
            }else{
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( LoginActivity.this, "INTERNET CONNECTION ERROR-1" , Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }
        }
    }
}

