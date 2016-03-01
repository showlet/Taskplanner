package cegepsth.taskplanner.tablayout.tabs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cegepsth.taskplanner.R;
import cegepsth.taskplanner.model.Project;
import cegepsth.taskplanner.model.Task;
import cegepsth.taskplanner.utils.Tool;

/**
 * Created by Maxim on 2/14/2016.
 *
 * Le fragment pour la tabulation des informations générique du projet (Survol)
 */
public class TabProjectOverview extends Fragment {

    private Project mProjet;

    /**
     * Setter pour le projet à afficher
     *
     * @param projet Le projet
     */
    public void setProjet(Project projet)
    {
        mProjet = projet;
    }

    /**
     * À la création de la vue
     *
     * @param inflater L'inflateur de layout
     * @param container Le container
     * @param savedInstanceState Les saved instances bundle
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_project_overview, container, false);

        int nbrAujourdhui = 0;
        int nbrRetard = 0;
        int nbrFinies = 0;

        for(Task t: mProjet.lstTask) {
            if (t.IsClosed)
                nbrFinies++;

            if (t.Deadline != null) {
                if (t.Deadline.before(new Date()) && !t.IsClosed)
                    nbrRetard++;

                if (t.Deadline != null && t.Deadline.getDate() == Calendar.getInstance().getTime().getDate())
                    nbrAujourdhui++;
            }
        }

        ((TextView) v.findViewById(R.id.txtNomProjet)).setText(mProjet.Name);
        if(mProjet.Deadline != null)
            ((TextView) v.findViewById(R.id.txtDateFin)).setText(Tool.DateToPrettyString(mProjet.Deadline));
        else
            ((TextView) v.findViewById(R.id.txtDateFin)).setText("");

        ((TextView) v.findViewById(R.id.txtDescriptionProjet)).setText(mProjet.Description);

        ((TextView) v.findViewById(R.id.txtTachesTotalProjet)).setText(mProjet.lstTask.size() + " " + getContext().getString(R.string.tache));
        ((TextView) v.findViewById(R.id.txtTachesTodayProjet)).setText(nbrAujourdhui + " " + getContext().getString(R.string.TacheToday));
        ((TextView) v.findViewById(R.id.txtTachesRetardProjet)).setText(nbrRetard + " " + getContext().getString(R.string.TacheLate));
        ((TextView) v.findViewById(R.id.txtTachesFiniesProjet)).setText(nbrFinies + " " + getContext().getString(R.string.TacheClosed));

        return v;
    }
}