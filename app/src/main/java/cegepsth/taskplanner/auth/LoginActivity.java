package cegepsth.taskplanner.auth;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
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
import cegepsth.taskplanner.MainActivity;
import cegepsth.taskplanner.R;
import cegepsth.taskplanner.utils.PreferencesManager;
import cegepsth.taskplanner.utils.SessionManager;
import cegepsth.taskplanner.utils.WebServiceClient;
import cz.msebera.android.httpclient.Header;

/**
 * Activité pour se connecter sur son compte
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.txtEmail) EditText txtEmail;
    @InjectView(R.id.txtPassword) EditText txtMotDePasse;
    @InjectView(R.id.btnLogin) Button btnLogIn;
    @InjectView(R.id.btnSignup) TextView btnSignUp;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);


        btnLogIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start l'activité de signup
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        if(SessionManager.getInstance().isAutoLoginAvailable())
            autoLogin();
    }

    public void autoLogin() {
        Log.d(TAG, "AutoLogin");

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.authenticating));
        progressDialog.show();

        int id = PreferencesManager.getInstance().getUserId();
        String token = PreferencesManager.getInstance().getSessionToken();


        //Create the request params to post to the web service.
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", id);
        requestParams.put("token", token);


        //Envoie une requête au service web pour authentifier l'utilisateur.
        WebServiceClient webServiceClient = new WebServiceClient();
        webServiceClient.post("AutoLogin.php", requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                int success;
                try {
                    // Valide si la tentative d'inscription est valide
                    success = response.getInt(WebServiceClient.TAG_SUCCESS);

                    if (success == 1) {

                        //Obtient les valeurs
                        int userId = response.getInt(WebServiceClient.TAG_ID);
                        String name = response.getString(WebServiceClient.TAG_NAME);
                        String email = response.getString(WebServiceClient.TAG_EMAIL);
                        String token = response.getString(WebServiceClient.TAG_TOKEN);

                        //Crée une session
                        SessionManager.getInstance().createLoginSession(userId,name,email,token);

                        //Termine l'activité
                        onLoginSuccess();
                    } else {
                        //Obtient le message d'erreur
                        String message = response.getString(WebServiceClient.TAG_MESSAGE);
                        onLoginFailed(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                onLoginFailed(getString(R.string.unreachable_server_error_message));
                progressDialog.dismiss();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed(getString(R.string.login_failed));
            return;
        }

        btnLogIn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.authenticating));
        progressDialog.show();


        String email = txtEmail.getText().toString();
        String password = txtMotDePasse.getText().toString();


        //Ajoute les paramètres de la requête
        RequestParams requestParams = new RequestParams();
        requestParams.put("email", email);
        requestParams.put("password", password);


        //Envoie une requête au server pour authentifier l'utilisateur.
        WebServiceClient webServiceClient = new WebServiceClient();
        webServiceClient.post("Login.php", requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                int success;
                try {
                    // Vérifie si le résultat de la requête.
                    success = response.getInt(WebServiceClient.TAG_SUCCESS);

                    if (success == 1) {

                        //Read response values
                        int userId = response.getInt(WebServiceClient.TAG_ID);
                        String name = response.getString(WebServiceClient.TAG_NAME);
                        String email = response.getString(WebServiceClient.TAG_EMAIL);
                        String token = response.getString(WebServiceClient.TAG_TOKEN);

                        //Crée une session
                        SessionManager.getInstance().createLoginSession(userId,name,email,token);

                        onLoginSuccess();
                    } else {
                        //Obtient le message d'erreur
                        String message = response.getString(WebServiceClient.TAG_MESSAGE);
                        onLoginFailed(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                onLoginFailed(getString(R.string.unreachable_server_error_message));
                progressDialog.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                onLoginSuccess();
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Désactive le retourne à la main activity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        btnLogIn.setEnabled(true);

        // Débute la MainActivity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        finish();
    }

    public void onLoginFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        btnLogIn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = txtEmail.getText().toString();
        String password = txtMotDePasse.getText().toString();

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
