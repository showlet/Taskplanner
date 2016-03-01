package cegepsth.taskplanner.tablayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import cegepsth.taskplanner.model.Task;
import cegepsth.taskplanner.tablayout.tabs.TabTacheDetails;
import cegepsth.taskplanner.tablayout.tabs.TabTacheOverview;

/**
 * Created by Maxim on 2/17/2016.
 *
 * Le pager adapter pour le détail d'une tâches
 */
public class TacheViewPagerAdapter extends FragmentStatePagerAdapter {

    private String mTitres[];
    private Task mTache;


    /**
     *CTOR
     * @param fragmentManager fragmentManager
     * @param titres Les titres pour les tab
     */
    public TacheViewPagerAdapter(FragmentManager fragmentManager,Task tache, String titres[]) {
        super(fragmentManager);

        this.mTitres = titres;
        this.mTache = tache;

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
            TabTacheOverview tabTacheOverview = new TabTacheOverview();
            tabTacheOverview.setTache(mTache);
            return tabTacheOverview;
        }
        else if(position == 1)
        {
            TabTacheDetails tabTacheDetails = new TabTacheDetails();
            tabTacheDetails.setTache(mTache);
            return tabTacheDetails;
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