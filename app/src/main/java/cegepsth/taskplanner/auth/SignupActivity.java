package cegepsth.taskplanner.auth;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cegepsth.taskplanner.R;
import cegepsth.taskplanner.utils.PreferencesManager;
import cegepsth.taskplanner.utils.SessionManager;
import cegepsth.taskplanner.utils.WebServiceClient;
import cz.msebera.android.httpclient.Header;

/**
 * Activité pour créer un nouveau compte
 */
public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @InjectView(R.id.txtName)
    EditText txtNom;
    @InjectView(R.id.txtEmail)
    EditText txtEmail;
    @InjectView(R.id.txtPassword)
    EditText txtMotDePasse;
    @InjectView(R.id.btnSignup)
    Button btnSignup;
    @InjectView(R.id.btnLogin)
    TextView btnLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        //Initialize the preference manager singleton
        PreferencesManager.initializeInstance(getApplicationContext());

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed(getString(R.string.login_failed));
            return;
        }

        btnSignup.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.creating_account));
        progressDialog.show();


        //Read the user values.
        final String nom = txtNom.getText().toString();
        final String email = txtEmail.getText().toString();
        final String motdepasse = txtMotDePasse.getText().toString();



        //Create the request params to post to the web service.
        final RequestParams requestParams = new RequestParams();
        requestParams.put("name", nom);
        requestParams.put("email", email);
        requestParams.put("password", motdepasse);


        //Create a request to the web service to sign up the new user.
        WebServiceClient webServiceClient = new WebServiceClient();
        webServiceClient.post("Register.php", requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                int success;
                try {
                    // Find out whether or not the sign up was successful.
                    success = response.getInt(WebServiceClient.TAG_SUCCESS);

                    //Check if the sign up was sucessful
                    if (success == 1) {

                        //Read response values
                        int userId = response.getInt(WebServiceClient.TAG_ID);
                        String token = response.getString(WebServiceClient.TAG_TOKEN);

                        //Create a login session
                        SessionManager.getInstance().createLoginSession(userId,nom,email,token);

                        //Finish activity
                        onSignupSuccess();
                    } else {
                        //Read the error message
                        String message = response.getString(WebServiceClient.TAG_MESSAGE);

                        //Display the message with a toast.
                        onSignupFailed(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                onSignupFailed(getString(R.string.unreachable_server_error_message));
                progressDialog.dismiss();
            }
        });
    }


    public void onSignupSuccess() {
        btnSignup.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        btnSignup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = txtNom.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtMotDePasse.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            txtNom.setError(getString(R.string.invalid_name_error_message));
            valid = false;
        } else {
            txtNom.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError(getString(R.string.invalid_email_error_message));
            valid = false;
        } else {
            txtEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            txtMotDePasse.setError(getString(R.string.invalid_password_error_message));
            valid = false;
        } else {
            txtMotDePasse.setError(null);
        }

        return valid;
    }
}