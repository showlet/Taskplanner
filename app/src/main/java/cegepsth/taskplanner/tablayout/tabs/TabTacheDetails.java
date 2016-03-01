package cegepsth.taskplanner.tablayout.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cegepsth.taskplanner.R;
import cegepsth.taskplanner.model.Task;

/**
 * Created by Maxim on 2/17/2016.
 */
public class TabTacheDetails extends Fragment {

    private Task mTache;

    /**
     *
     * Setter pour le tâches à afficher
     *
     * @param task La taches
     */
    public void setTache(Task task)
    {
        mTache = task;
    }

    /**
     * À la crèation de la vue
     * @param inflater L'inflateur
     * @param container Le view group
     * @param savedInstanceState Le bundle de saved instance
     * @return La vue
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_tache_messages,container,false);

        return v;
    }
}