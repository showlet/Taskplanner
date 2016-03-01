package cegepsth.taskplanner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import cegepsth.taskplanner.database.DAOCallback;
import cegepsth.taskplanner.database.ProjetDAO;
import cegepsth.taskplanner.model.Project;
import cegepsth.taskplanner.model.Task;

/**
 * Created by Showlet on 2016-02-12.
 */
public class ListeTachesFragment extends android.support.v4.app.Fragment {
    public static final String TAG = "ListeTachesFragment";

    private RecyclerView _recyclelst_tache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liste_taches, container, false);
        getActivity().findViewById(R.id.toolbar).setElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.Actionbar_tasks));
        _recyclelst_tache = (RecyclerView) view.findViewById(R.id.recycler_tache);
        initialiserRecycler();
        commencerRafraichissement();


        return view;
    }

    /**
     * Commence a telecharge les taches
     */
    private void commencerRafraichissement()
    {
        //Fetch les taches
        new ProjetDAO().getProjets(new DAOCallback() {
            @Override
            public void run() {
                try {
                    ArrayList<Project> lst = ((ArrayList<Project>) this.getResponse());
                    ArrayList<Task> lstTache = new ArrayList<>();

                    for (Project p : lst)
                        lstTache.addAll(p.lstTask);

                    _recyclelst_tache.setAdapter(new TacheAdapter(lstTache));
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Initialise le Recycler
     */
    private void initialiserRecycler()
    {
        _recyclelst_tache.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        _recyclelst_tache.setLayoutManager(llm);

        // Ajout des évènement dans le recycler
        _recyclelst_tache.addOnItemTouchListener(new RecyclerEventListener(getActivity(), _recyclelst_tache, new RecyclerEventListener.IEventListener() {
            @Override
            public void onClick(View view, int position) {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(DetailsTacheFragment.TAG);
                if (fragment == null) {
                    fragment = new DetailsTacheFragment();
                    ((DetailsTacheFragment) fragment).setTache(((TacheAdapter) _recyclelst_tache.getAdapter()).getItem(position));
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
    }
}
