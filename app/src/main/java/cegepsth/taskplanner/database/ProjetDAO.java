package cegepsth.taskplanner.database;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cegepsth.taskplanner.model.Project;
import cegepsth.taskplanner.utils.PreferencesManager;
import cegepsth.taskplanner.utils.SessionManager;
import cegepsth.taskplanner.utils.Tool;
import cegepsth.taskplanner.utils.WebServiceClient;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Maxim on 2/12/2016.
 * Implémentation du DAO pour les Projets
 */
public class ProjetDAO implements IProjetDAO {
    private static final String GET_ALL_SCRIPT_NAME = "GetAllProject.php";
    private static final String GET_SCRIPT_NAME = "GetProject.php";
    private static final String INSERT_SCRIPT_NAME = "InsertProject.php";
    private static final String UPDATE_SCRIPT_NAME = "UpdateProject.php";
    private static final String DELETE_SCRIPT_NAME = "";

    /**
     * Envoie une requête au service web pour obtenir tous les projets de l'utilisateur sur la BD MySQL
     */
    @Override
    public Project[] getProjets(final DAOCallback callback) {

        WebServiceClient webServiceClient = new WebServiceClient();

        RequestParams params = new RequestParams();
        params.put("Token", PreferencesManager.getInstance().getSessionToken());
        params.put("UserId", PreferencesManager.getInstance().getUserId());

        webServiceClient.get(GET_ALL_SCRIPT_NAME, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                int succes;
                try {
                    // Valide si la requête est ok
                    succes = response.getInt(WebServiceClient.TAG_SUCCESS);

                    if (succes == 1) {

                        //parse le json
                        String str = response.getJSONArray("Projects").toString();
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        ArrayList<Project> lst = new ArrayList<>(Arrays.asList(gson.fromJson(str, Project[].class)));

                        callback.setResponse(lst);
                        callback.run();

                    } else {
                        if(response.getInt("error") == 1001)
                            SessionManager.getInstance().logout();
                        System.out.println("Erreur " + response.getInt("error"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("Erreur");
            }
        });

        return new Project[0];
    }

    /**
     *
     * Envoie une requête au service web pour obtenir le projet sur la BD MySQL qui correspon au ID
     *
     * @param id Le id du projet à retrouver
     */
    @Override
    public Project getProjet(int id, final DAOCallback callback){

        RequestParams requestParams =  new RequestParams();
        requestParams.put("Token", PreferencesManager.getInstance().getSessionToken());
        requestParams.put("UserId", PreferencesManager.getInstance().getUserId());
        requestParams.put("Id",id);

        WebServiceClient webServiceClient = new WebServiceClient();
        webServiceClient.get(GET_SCRIPT_NAME,requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                int succes;
                try {
                    // Valide si la requête est ok
                    succes = response.getInt(WebServiceClient.TAG_SUCCESS);

                    if (succes == 1) {
                        //parse le json
                        String str = response.getJSONObject("Projects").toString();
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        Project p = gson.fromJson(str, Project.class);

                        callback.setResponse(p);
                        callback.run();
                        //out.Deadline = response.get
                        //out.lstTask = respose.get
                    } else {
                        if(response.getInt("error") == 1001)
                            callback.run();
                            SessionManager.getInstance().logout();
                        //TODO: REQUÊTE PAS OK GERER LE FAIL
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //TODO: REQUÊTE PAS OK GERER LE FAIL
                callback.run();
            }
        });
        return null;
    }

    /**
     *
     * Envoie une requête au service web pour ajouter un projet sur la BD MySQL
     *
     * @param projet Le projet à ajouter
     */
    @Override
    public void insertProjet(final Project projet) {
        WebServiceClient webServiceClient = new WebServiceClient();

        RequestParams requestParams = projet.toRequestParams();
        requestParams.put("Token", PreferencesManager.getInstance().getSessionToken());
        requestParams.put("UserId", PreferencesManager.getInstance().getUserId());


        final int projetId = 0;
        webServiceClient.get(INSERT_SCRIPT_NAME, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                int succes;
                try {
                    // Valide si la requête est ok
                    succes = response.getInt(WebServiceClient.TAG_SUCCESS);

                    if (succes == 1) {
                        projet.Id = response.getInt("id");
                    } else {
                        if(response.getInt("error") == 1001)
                            SessionManager.getInstance().logout();
                        //TODO: REQUÊTE PAS OK GERER LE FAIL
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //TODO: REQUÊTE PAS OK GERER LE FAIL
                projet.Id = 0;
            }
        });
    }

    /**
     *
     * Envoie une requête au service web pour mettre à jour un projet sur la BD MySQL
     *
     * @param projet Le projet à mettre à jour
     */
    @Override
    public void updateProjet(final Project projet) {

        WebServiceClient webServiceClient = new WebServiceClient();

        RequestParams requestParams = projet.toRequestParams();
        requestParams.put("Token", PreferencesManager.getInstance().getSessionToken());
        requestParams.put("UserId", PreferencesManager.getInstance().getUserId());

        webServiceClient.get(UPDATE_SCRIPT_NAME,requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                int succes;
                try {
                    // Valide si la requête est ok
                    succes = response.getInt(WebServiceClient.TAG_SUCCESS);

                    if (succes == 1) {
                        //TODO: REQUÊTE OK
                    } else {
                        //TODO: REQUÊTE PAS OK GERER LE FAIL
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //TODO: REQUÊTE PAS OK GERER LE FAIL
            }
        });
    }


    /**
     * On ne l'utilise pas mais elle est la pour le futur.
     * Envoie une requête au service web pour effacer un projet sur la BD MySQL
     *
     * @param projet Le projet à supprimer
     */
    @Override
    public void deleteProjet(final Project projet) {

        RequestParams requestParams = projet.toRequestParams();
        requestParams.put("Token", PreferencesManager.getInstance().getSessionToken());
        requestParams.put("UserId", String.valueOf(PreferencesManager.getInstance().getUserId()));

        WebServiceClient webServiceClient = new WebServiceClient();
        webServiceClient.get(DELETE_SCRIPT_NAME,requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                int succes;
                try {
                    // Valide si la requête est ok
                    succes = response.getInt(WebServiceClient.TAG_SUCCESS);

                    if (succes == 1) {
                        projet.Id = 0;
                        projet.Deadline = null;
                        projet.lstTask = null;
                        projet.Description = null;
                        projet.Name = null;
                        projet.OwnerId = 0;
                    } else {
                        //TODO: REQUÊTE PAS OK GERER LE FAIL
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //TODO: REQUÊTE PAS OK GERER LE FAIL
            }
        });
    }
}
