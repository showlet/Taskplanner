package cegepsth.taskplanner.database;

import cegepsth.taskplanner.model.Task;

/**
 * Created by Maxim on 2/12/2016.
 * Interface pour le DAO des tâches
 */
public interface ITacheDAO {
    Task[] getTaches(final DAOCallback callback);
    Task getTache(int id, final DAOCallback callback);

    void insertTache(final Task tache);
    void updateTache(final Task tache);
    void deleteTache(final Task tache);
}
