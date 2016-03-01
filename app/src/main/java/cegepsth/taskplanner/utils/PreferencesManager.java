package cegepsth.taskplanner.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Maxim on 12/7/2015.
 * Preferences Manager is a singleton wrapping the app SharedPreferences.
 */
public class PreferencesManager {
    private static final String TAG = "PreferencesManager";
    private static PreferencesManager mInstance;
    private final SharedPreferences mSharedPreferences;


    private static final String PREF_NAME = "APP_PREFERENCES";

    public static final String USER_ID_KEY = "USER_ID";
    public static final String USER_NAME_KEY = "USER_NAME";
    public static final String USER_EMAIL_KEY = "USER_EMAIL";
    public static final String SESSION_TOKEN_KEY = "SESSION_TOKEN";
    public static final String SESSION_LOGIN_STATUS = "SESSION_LOGIN_STATUS";


    /**
     *
     * CTOR
     *
     * @param context Context de l'application
     */
    private PreferencesManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     *
     * Permet d'initialiser le singleton pour les préférences
     *
     * @param context Context de l'application
     */
    public static synchronized void initializeInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(Context) method first.");
        }
        return mInstance;
    }

    /**
     *
     * Set le id de l'utilisateur
     *
     * @param value La valeur
     */
    public void setUserId(int value) {
        mSharedPreferences.edit()
                .putInt(USER_ID_KEY, value)
                .apply();
    }

    /**
     *
     * Set le token de session
     *
     * @param value La valeur
     */
    public void setSessionToken(String value) {
        mSharedPreferences.edit()
                .putString(SESSION_TOKEN_KEY, value)
                .apply();
    }

    /**
     *
     * Set le nom de l'utilisateur
     *
     * @param value La valeur
     */
    public void setUserName(String value) {
        mSharedPreferences.edit()
                .putString(USER_NAME_KEY, value)
                .apply();
    }

    /**
     *
     * Set le email de l'utilisateur
     *
     * @param value La valeur
     */
    public void setUserEmail(String value) {
        mSharedPreferences.edit()
                .putString(USER_EMAIL_KEY, value)
                .apply();
    }

    /**
     *
     * Set le statut de connexion de l'utilisateur
     *
     * @param value La valeur
     */
    public void setLoginStatus(boolean value) {
        mSharedPreferences.edit()
                .putBoolean(SESSION_LOGIN_STATUS, value)
                .apply();
    }


    /**
     *
     * Retrouve le id de l'utilisateur
     *
     * @return Le id de l'utilisateur
     */
    public int getUserId() {
        return mSharedPreferences.getInt(USER_ID_KEY, 0);
    }


    /**
     *
     * Retrouve le nom de l'utilisateur
     *
     * @return Le nom de l'utilisateur
     */
    public String getUserName() {
        return mSharedPreferences.getString(USER_NAME_KEY, "Invalid Name");
    }

    public String getUserEmail() {
        return mSharedPreferences.getString(USER_EMAIL_KEY, "Invalid Email");
    }

    /**
     *
     * Retrouve le token de session de l'utilisateur
     *
     * @return Le token de session de l'utilisateur
     */
    public String getSessionToken() {
        return mSharedPreferences.getString(SESSION_TOKEN_KEY, "Invalid Token");
    }

    /**
     *
     * Retrouve le statut de connexion de l'utilisateur
     *
     * @return Le statut de connexion de l'utilisateur
     */
    public boolean getLoginStatus() {
        return  mSharedPreferences.getBoolean(SESSION_LOGIN_STATUS,false);
    }


    /**
     *
     * Retire la valeur et la clé des préférences
     *
     * @param key La clé
     */
    public void remove(String key) {
        mSharedPreferences.edit()
                .remove(key)
                .apply();
    }

    /**
     * Efface toutes les préférences de l'utilisateur
     */
    public void clear() {
        mSharedPreferences.edit()
                .clear()
                .apply();
    }


}