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
import java.util.Calendar;

import cegepsth.taskplanner.database.DAOCallback;
import cegepsth.taskplanner.database.ProjetDAO;
import cegepsth.taskplanner.model.Project;
import cegepsth.taskplanner.model.Task;

/**
 * Created by Showlet on 2016-02-12.
 */
public class ListeTodayTachesFragment extends Fragment {

    public static final String TAG = "ListeTodayTachesFragment";

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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getContext().getString(R.string.Actionbar_today));
        _recyclelst_tache = (RecyclerView) view.findViewById(R.id.recycler_tache);
        initialiserRecycler();
        commencerRafraichissement();

        return view;
    }

    private void initialiserRecycler()
    {
        _recyclelst_tache.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        _recyclelst_tache.setLayoutManager(llm);

        _recyclelst_tache.addOnItemTouchListener(new RecyclerEventListener(getActivity(), _recyclelst_tache, new RecyclerEventListener.IEventListener() {
            @Override
            public void onClick(View view, int position) {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(DetailsTacheFragment.TAG);
                if (fragment == null) {
                    fragment = new DetailsTacheFragment();
                    ((DetailsTacheFragment)fragment).setTache(((TacheAdapter) _recyclelst_tache.getAdapter()).getItem(position));
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

    private void commencerRafraichissement()
    {
        new ProjetDAO().getProjets(new DAOCallback() {
            @Override
            public void run() {
                try {
                    _recyclelst_tache.setAdapter(new TacheAdapter(filtrerTachespourAujourdhui(((ArrayList<Project>) this.getResponse()))));
                } catch (Exception ex) {
                    Toast.makeText(ListeTodayTachesFragment.this.getContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private ArrayList<Task> filtrerTachespourAujourdhui(ArrayList<Project> lst)
    {
        ArrayList<Task> lstTache = new ArrayList<>();

        if (lst != null)
            for (Project p : lst)
                for(Task t : p.lstTask)
                    if(t.Deadline != null && t.Deadline.getDate() == Calendar.getInstance().getTime().getDate()){
                        lstTache.add(t);
                    }

        return lstTache;
    }
}
