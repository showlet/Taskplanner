package cegepsth.taskplanner.model;

import com.loopj.android.http.RequestParams;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;

import cegepsth.taskplanner.utils.Tool;

/**
 * Model abstrait pour fournir des méthodes génériques à tout le modèles de données
 * Created by Maxim on 2/23/2016.
 */
public abstract class AbstractDBModel {
    /**
     *
     * Obtient le nom et la valeur de chaques propriétés de la classe. N'incluant pas les
     * propriétés des classes de base dont elle pourrait hériter. Les valeurs sont convertient
     * en clé/valeur et ajouté dans l'objet RequestParams
     *
     * @return Les paramètres de requête
     */
    public RequestParams toRequestParams(){
        RequestParams params = new RequestParams();

        Field[] fields = this.getClass().getDeclaredFields();
        for(Field field : fields){

            //Si c'est une collection on la skip. Elle ne sont pas envoyé sur le serveur
            if(Collection.class.isAssignableFrom(field.getType())) {
                continue;
            }

            //Obtient le nom du champs
            String propName = field.getName();
            try {
                //Obtient la valeur de la propriété
                String propValue = field.get(this) == null ? "" : field.get(this).toString();

                //Si c'est un champ date on le format
                if(Date.class.isAssignableFrom(field.getType()) && field.get(this) != null){
                    propValue = Tool.DateToRealString((Date)field.get(this));
                }

                params.put(propName, propValue);
            }
            catch (IllegalAccessException e)
            {
                params.put(propName, "null");
            }
        }

        return params;
    }

}
