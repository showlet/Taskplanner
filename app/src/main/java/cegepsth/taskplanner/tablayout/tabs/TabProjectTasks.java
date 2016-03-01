package cegepsth.taskplanner.tablayout.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cegepsth.taskplanner.DetailsTacheFragment;
import cegepsth.taskplanner.ProjetAdapter;
import cegepsth.taskplanner.R;
import cegepsth.taskplanner.RecyclerEventListener;
import cegepsth.taskplanner.TacheAdapter;
import cegepsth.taskplanner.model.Project;
import cegepsth.taskplanner.model.Task;

/**
 * Created by Maxim on 2/14/2016.
 *
 * Fragment pour afficher la tabulations avec la listes des taches pour un projet.
 */
public class TabProjectTasks extends Fragment
{
    private ArrayList<Task> mTaches;
    private RecyclerView mTacheRecycler;

    /**
     *
     * Setter pour le La liste des tâches à afficher
     *
     * @param arrayListTache La liste des tâches
     */
    public void setTaches(ArrayList<Task> arrayListTache)
    {
        mTaches = arrayListTache;
    }

    /**
     *
     * À la création de la vue.
     *
     * @param inflater Inflateur de layout
     * @param container Le viewgroup container
     * @param savedInstanceState Le bundle de saved instance
     * @return La vue
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_project_tasks, container, false);

        mTacheRecycler = (RecyclerView) v.findViewById(R.id.recycler_projet);
        initialiserRecycler();

        return v;
    }


    /**
     * Initialise le recycler view qui va contenir les tâches
     */
    private void initialiserRecycler()
    {
        mTacheRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTacheRecycler.setLayoutManager(linearLayoutManager);

        mTacheRecycler.addOnItemTouchListener(new RecyclerEventListener(getActivity(), mTacheRecycler, new RecyclerEventListener.IEventListener() {
            @Override
            public void onClick(View view, int position) {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(DetailsTacheFragment.TAG);
                if (fragment == null) {
                    fragment = new DetailsTacheFragment();
                    ((DetailsTacheFragment)fragment).setTache(((TacheAdapter) mTacheRecycler.getAdapter()).getItem(position));
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, DetailsTacheFragment.TAG).commit();
            }

            @Override
            public void onLongClick(View view, int position) {
            }

            @Override
            public void onDoubleTap(View view, int position) {
            }

            @Override
            public void onFlingLeft(View view, int position) {

            }

            @Override
            public void onFlingRight(View view, int position) {

            }
        }));

        mTacheRecycler.setAdapter(new TacheAdapter(mTaches));
    }
}