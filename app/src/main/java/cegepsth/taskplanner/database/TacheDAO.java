package cegepsth.taskplanner.database;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import cegepsth.taskplanner.model.Task;
import cegepsth.taskplanner.utils.PreferencesManager;
import cegepsth.taskplanner.utils.SessionManager;
import cegepsth.taskplanner.utils.Tool;
import cegepsth.taskplanner.utils.WebServiceClient;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Maxim on 2/12/2016.
 * Implémentation du DAO pour les tâches
 */
public class TacheDAO implements ITacheDAO {
    private static final String GET_ALL_SCRIPT_NAME = "GetAllTask.php";
    private static final String GET_SCRIPT_NAME = "GetTask.php";
    private static final String INSERT_SCRIPT_NAME = "InsertTask.php";
    private static final String UPDATE_SCRIPT_NAME = "UpdateTask.php";
    private static final String DELETE_SCRIPT_NAME = "DeleteTask.php";


    /**
     *
     * Retrouve tous les tâches de l'utiltateur sur la BD MySql
     *
     * @return Array de tâches de l'utilisateur
     */
    @Override
    public Task[] getTaches(final DAOCallback callback) {

        RequestParams requestParams = new RequestParams();
        requestParams.put("Token", PreferencesManager.getInstance().getSessionToken());
        requestParams.put("UserId", String.valueOf(PreferencesManager.getInstance().getUserId()));

        WebServiceClient webServiceClient = new WebServiceClient();
        webServiceClient.get(GET_ALL_SCRIPT_NAME, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                int succes;
                try {
                    // Valide si la requête est ok
                    succes = response.getInt(WebServiceClient.TAG_SUCCESS);

                    if (succes == 1) {
                        //parse le json
                        String str = response.getJSONArray("Task").toString();
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        ArrayList<Task> lst = new ArrayList<>(Arrays.asList(gson.fromJson(str, Task[].class)));

                        callback.setResponse(lst);
                        callback.run();
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
        return new Task[0];
    }

    /**
     *
     * Retrouve la tâche avec le ID demandé sur la BD MySql
     *
     * @param id Le ID de la tache a retourner
     * @return La tache
     */
    @Override
    public Task getTache(int id, final DAOCallback callback) {

        RequestParams requestParams =  new RequestParams();
        requestParams.put("Token", PreferencesManager.getInstance().getSessionToken());
        requestParams.put("UserId", String.valueOf(PreferencesManager.getInstance().getUserId()));
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
                        String str = response.getJSONObject("Task").toString();
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        Task p = gson.fromJson(str, Task.class);

                        callback.setResponse(p);
                        callback.run();
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
        return null;
    }

    /**
     *
     * Envoie une requête au service web pourajouter une tâche sur la BD MySQL
     *
     * @param tache La tache à ajouter
     */
    @Override
    public void insertTache(final Task tache) {

        RequestParams requestParams = tache.toRequestParams();
        requestParams.put("Token", PreferencesManager.getInstance().getSessionToken());
        requestParams.put("UserId", PreferencesManager.getInstance().getUserId());

        WebServiceClient webServiceClient = new WebServiceClient();
        webServiceClient.get(INSERT_SCRIPT_NAME, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                int succes;
                try {
                    // Valide si la requête est ok
                    succes = response.getInt(WebServiceClient.TAG_SUCCESS);

                    if (succes == 1) {
                        tache.Id = response.getInt("id");
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
                int i = 0;
            }
        });
    }

    /**
     *
     * Envoie une requête au service web pour mettre à jour une tâche sur la BD MySQL
     *
     * @param tache La tache à mettre à jour
     */
    @Override
    public void updateTache(final Task tache) {

        RequestParams requestParams = tache.toRequestParams();
        requestParams.put("Token", PreferencesManager.getInstance().getSessionToken());
        requestParams.put("UserId", PreferencesManager.getInstance().getUserId());

        WebServiceClient webServiceClient = new WebServiceClient();
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
     *
     * Envoie une requête au service web pour effacer une tache sur la BD MySQL
     *
     * @param tache La tache à supprimer
     */
    @Override
    public void deleteTache(final Task tache) {

        RequestParams requestParams =tache.toRequestParams();
        requestParams.put("Token", PreferencesManager.getInstance().getSessionToken());
        requestParams.put("UserId", PreferencesManager.getInstance().getUserId());

        WebServiceClient webServiceClient = new WebServiceClient();
        webServiceClient.get(DELETE_SCRIPT_NAME,requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                int succes;
                try {
                    // Valide si la requête est ok
                    succes = response.getInt(WebServiceClient.TAG_SUCCESS);

                    if (succes == 1) {
                        tache.Id = 0;
                        tache.ProjectId = 0;
                        tache.UserId = 0;
                        tache.Closed = null;
                        tache.Deadline = null;
                        tache.Description = null;
                        tache.Created = null;
                        tache.IsClosed = null;
                        tache.Name = null;
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
