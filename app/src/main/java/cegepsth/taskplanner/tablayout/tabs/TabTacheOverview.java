package cegepsth.taskplanner.tablayout.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cegepsth.taskplanner.R;
import cegepsth.taskplanner.model.Task;
import cegepsth.taskplanner.utils.Tool;

/**
 * Created by Maxim on 2/17/2016.
 */
public class TabTacheOverview extends Fragment {

    private Task mTache;

    /**
     * Setter pour la tâche à afficher.
     * @param task La tâches à afficher.
     */
    public void setTache(Task task)
    {
        mTache = task;
    }


    /**
     * À la création de la vue
     * @param inflater L'inflateur de layout
     * @param container Le container
     * @param savedInstanceState Le bundle de saved instance
     * @return La view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_tache_overview,container,false);

        ((TextView) v.findViewById(R.id.txtNomTache)).setText(mTache.Name);
        ((TextView) v.findViewById(R.id.txtDescriptionTache)).setText(mTache.Description);

        if(mTache.Created != null)
            ((TextView) v.findViewById(R.id.txtDateCreationTache)).setText( getContext().getString(R.string.TacheCreate) + " "  + Tool.DateToPrettyString(mTache.Created));
        else
            ((TextView) v.findViewById(R.id.txtDateCreationTache)).setText("");

        if(mTache.Deadline != null)
            ((TextView) v.findViewById(R.id.txtDateFinTache)).setText(getContext().getString(R.string.TacheDeadline) + " "  +Tool.DateToPrettyString(mTache.Deadline));
        else
            ((TextView) v.findViewById(R.id.txtDateFinTache)).setText("");

        return v;
    }
}