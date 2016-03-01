package cegepsth.taskplanner.database;

import cegepsth.taskplanner.model.Project;

/**
 * Created by Maxim on 2/12/2016.
 * Interface pour le DAO des projets
 */
public interface IProjetDAO {

    Project[] getProjets(DAOCallback callback);
    Project getProjet(int Id, DAOCallback callback);

    void insertProjet(final Project projet);
    void updateProjet(final Project projet);
    void deleteProjet(final Project projet); //Pas utilisé
}
