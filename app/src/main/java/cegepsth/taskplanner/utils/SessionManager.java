package cegepsth.taskplanner.utils;

import android.content.Context;
import android.content.Intent;
import cegepsth.taskplanner.auth.LoginActivity;


/**
 * Classe utilitaire pour gérer la session d'un utilisateur.
 *
 * Created by Maxim on 12/11/2015.
 */
public class SessionManager {
    private static final String TAG = "SessionManager";
    private static SessionManager mInstance;
    private final Context mContext;
    private int mCurrentUserId;


    /**
     *
     * CTOR
     *
     * @param context Context de l'application
     */
    private SessionManager(Context context) {
        mContext = context;
    }

    /**
     *
     * Permet d'initialiser l'instance du singleton pour le session manager
     *
     * @param context Context de l'application
     */
    public static synchronized void initializeInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SessionManager(context);
        }
    }

    /**
     *
     * Permet d'obtenir l'instance du singleton du session  manager.
     *
     * @return L'instance du singleton
     */
    public static synchronized SessionManager getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(Context) method first.");
        }
        return mInstance;
    }


    /**
     *
     * Permet de créer une session de connexion sur l'application avec
     * les informations de l'utilisateur
     *
     * @param id  Le id de l'utilisateur
     * @param name Le nom de l'utilisateur
     * @param email Le email de l'utilisateur
     * @param token Le token de session de l'utilisateur
     */
    public void createLoginSession(int id, String name, String email, String token){
        // On le status de connexion à vrai
        PreferencesManager.getInstance().setLoginStatus(true);

        // On enregistre les informations de connexion de l'utilisateur
        PreferencesManager.getInstance().setUserId(id);
        PreferencesManager.getInstance().setUserName(name);
        PreferencesManager.getInstance().setUserEmail(email);
        PreferencesManager.getInstance().setSessionToken(token);
    }

    /**
     *
     * Vérifie sur l'utilisateur est connecté. S'il est connecté
     * on ne fait rien. S'il ne l'est pas on le redirige vers l'activité de
     * login
     *
     * */
    public boolean checkLogin(){
        // Vérifie sur l'utilisateur est connecté
        if(!this.isUserLoggedIn()){

            // On le redirige vers le login
            Intent intent = new Intent(mContext, LoginActivity.class);

            // On ferme les autres activités
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Flag pour commencer la nouvelle activité
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // On start l'activité de login
            mContext.startActivity(intent);

            return true;
        }
        return false;
    }

    /**
     *
     * Déconnecte l'utilisateur en détruisant les informations de connection automatique
     *
     * */
    public void logout(){

        //On déconnecte le l'utilisateur
        PreferencesManager.getInstance().setLoginStatus(false);

        // On éfface les informations de connexion automatique
        PreferencesManager.getInstance().remove(PreferencesManager.USER_ID_KEY);
        PreferencesManager.getInstance().remove(PreferencesManager.USER_NAME_KEY);
        PreferencesManager.getInstance().remove(PreferencesManager.USER_EMAIL_KEY);
        PreferencesManager.getInstance().remove(PreferencesManager.SESSION_TOKEN_KEY);


        // On redirige vers l'activité de login
        Intent intent = new Intent(mContext, LoginActivity.class);

        // On ferme les autres activités
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Flag pour commencer une nouvelle activité
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // On lance l'activité de login
        mContext.startActivity(intent);
    }

    /**
     *
     * Vérifie le statut de connexion
     *
     * @return Le statut de connexion de l'utilisateur
     */
    public boolean isUserLoggedIn(){
        return PreferencesManager.getInstance().getLoginStatus();
    }

    /**
     *
     * Vérifie si on peut connecter automatiquement l'utilisateur
     *
     * @return Si la connexion automatique est possible
     */
    public boolean isAutoLoginAvailable(){
        return PreferencesManager.getInstance().getSessionToken() != "Invalid Token" &&
               PreferencesManager.getInstance().getUserId() != 0;
    }

    public int getCurrentUserId()
    {
        return mCurrentUserId;
    }
}