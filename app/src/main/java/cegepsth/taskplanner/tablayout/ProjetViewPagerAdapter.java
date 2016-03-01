package cegepsth.taskplanner.tablayout;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cegepsth.taskplanner.model.Project;
import cegepsth.taskplanner.model.Task;
import cegepsth.taskplanner.tablayout.tabs.TabProjectOverview;
import cegepsth.taskplanner.tablayout.tabs.TabProjectTasks;

/**
 * Created by Maxim on 2/14/2016.
 *
 * Le pager adapter pour le détail d'un projet.
 */
public class ProjetViewPagerAdapter extends FragmentStatePagerAdapter {

    private String mTitres[];
    private Project mProjet;


    /**
     *CTOR
     * @param fragmentManager fragmentManager
     * @param titres Les titres pour les tab
     */
    public ProjetViewPagerAdapter(FragmentManager fragmentManager,Project project, String titres[]) {
        super(fragmentManager);

        this.mTitres = titres;
        this.mProjet = project;

    }

    public Project getProjet()
    {
        return mProjet;
    }

    /**
     * Retourne le fragment pour la tab demandé
     * @param position La position de la tab
     * @return Le fragment de la tab
     */
    @Override
    public Fragment getItem(int position) {

        if(position == 0)
        {
            TabProjectOverview tabProjectOverview = new TabProjectOverview();
            tabProjectOverview.setProjet(mProjet);
            return tabProjectOverview;
        }
        else if(position == 1)
        {
            TabProjectTasks tabProjectTasks = new TabProjectTasks();
            tabProjectTasks.setTaches(mProjet.lstTask);
            return tabProjectTasks;
        }

        return null;
    }


    /**
     * Retourne le nom de la tabulation à l'index
     * @param position La position de la tab
     * @return Le nom de la tab
     */
    @Override
    public String getPageTitle(int position) {
        return mTitres[position];
    }


    /**
     *
     * Retourne le nombre de tabulation
     *
     * @return Le nombre de tabulations
     */
    @Override
    public int getCount() {
        return mTitres.length;
    }
}
