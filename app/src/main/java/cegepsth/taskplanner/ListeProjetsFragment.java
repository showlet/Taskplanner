package cegepsth.taskplanner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cegepsth.taskplanner.database.DAOCallback;
import cegepsth.taskplanner.database.IProjetDAO;
import cegepsth.taskplanner.database.ProjetDAO;
import cegepsth.taskplanner.model.Project;
import cegepsth.taskplanner.utils.SessionManager;

/**
 * Created by Showlet on 2016-02-12.
 */
public class ListeProjetsFragment extends android.support.v4.app.Fragment {
    public static final String TAG = "ListeProjetsFragment";

    private IProjetDAO mProjetDAO;
    private FloatingActionButton mNewProjetButton;
    private RecyclerView _recyclelst_projet;
    private SwipeRefreshLayout _swipe_layout;
    private FrameLayout frameLayout;
    private Button newProjetButton;
    private TextView txtNom;
    private TextView txtDescription;
    private ImageView dimImage;
    private CalendarView calProjet;
    private String dateTache;
    private Boolean mOverlayMenuVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjetDAO = new ProjetDAO();
        mOverlayMenuVisible = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liste_projets, container, false);
        getActivity().findViewById(R.id.toolbar).setElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        _recyclelst_projet = (RecyclerView) view.findViewById(R.id.recycler_projet);
        _swipe_layout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_projet);

        frameLayout = (FrameLayout) view.findViewById(R.id.ajouterProjetLayout);
        txtNom = (TextView) view.findViewById(R.id.txtAddNomProjet);
        txtDescription = (TextView) view.findViewById(R.id.txtAddDescriptionProjet);
        calProjet = (CalendarView) view.findViewById(R.id.calAjouterProjet);
        dimImage = (ImageView) view.findViewById(R.id.imgDim);
        dimImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                closeOverlayMenu();
            }
        });
        newProjetButton = (Button) view.findViewById(R.id.btnCreerProjet);

        //Setup le bouton pour ajouter les nouvelles tâches.
        mNewProjetButton = (FloatingActionButton) view.findViewById(R.id.fabNewProjet);
        mNewProjetButton.setBackgroundTintList(getResources().getColorStateList(R.color.fab_new_color_state_list));
        mNewProjetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNouveauProjet();
            }
        });
        newProjetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ajouterProjet();
            }
        });

        // Calendar
        calProjet.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                month += 1;
                dateTache = year + "-" + (month) + "-" + dayOfMonth;
            }//met
        });


        initialiserRecycler();
        ListeProjetsFragment.this.commencerRafraichissement();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getContext().getString(R.string.Actionbar_projects));

        return view;
    }

    // Initialise le Recycler et le swipeview
    private void initialiserRecycler() {
        _recyclelst_projet.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        _recyclelst_projet.setLayoutManager(llm);

        // Ajout des évènement dans le recycler
        _recyclelst_projet.addOnItemTouchListener(new RecyclerEventListener(getActivity(), _recyclelst_projet, new RecyclerEventListener.IEventListener() {
            @Override
            public void onClick(View view, int position) {
                Fragment fragment;
                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(DetailsProjetFragment.TAG);
                if (fragment == null) {
                    fragment = new DetailsProjetFragment();
                    ((DetailsProjetFragment) fragment).setProjet(((ProjetAdapter) _recyclelst_projet.getAdapter()).getItem(position));
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, DetailsProjetFragment.TAG).commit();
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

        _swipe_layout.setColorSchemeResources(R.color.primary);
        _swipe_layout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        ListeProjetsFragment.this.commencerRafraichissement();
                    }
                }
        );

    }

    /**
     * Commence a telecharge les projet (et les tache) de l'utilisateur connecté
     */
    private void commencerRafraichissement() {
        new ProjetDAO().getProjets(new DAOCallback() {
            @Override
            public void run() {
                try {
                    _recyclelst_projet.setAdapter(new ProjetAdapter(((ArrayList<Project>) this.getResponse())));
                } catch (Exception ex) {
                    Toast.makeText(ListeProjetsFragment.this.getContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
                _swipe_layout.setRefreshing(false);
            }
        });
    }

    /**
     * Affiche le menu pour ajouter un nouveau projet.
     */
    private void initNouveauProjet() {
        Date cal = new Date();
        calProjet.setMinDate(cal.getTime());
        dimImage.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.VISIBLE);
        mOverlayMenuVisible = true;
    }

    /**
     * Permet d'ajouter une nouvelle tâches dans la base de données
     */
    private void ajouterProjet()
    {
        SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = ss.parse(dateTache);
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (txtNom.getText().length() > 1 && txtDescription.getText().length() > 1) {
            Project projet = new Project();
            projet.OwnerId = SessionManager.getInstance().getCurrentUserId();
            projet.Name = txtNom.getText().toString();
            projet.Description = txtDescription.getText().toString();
            projet.Deadline = date;

            mProjetDAO.insertProjet(projet);

            frameLayout.setVisibility(View.GONE);
            dimImage.setVisibility(View.GONE);
            ListeProjetsFragment.this.commencerRafraichissement();
        }
        else
            Toast.makeText(getContext(), "Veuillez entrer toutes les informations.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Retourne si le overley menu pour créer une tâche est visible
     * @return True si visible
     */
    public boolean overlayMenuIsVisible(){
        return mOverlayMenuVisible;
    }


    /**
     * Ferme le menu d'overlay
     */
    public void closeOverlayMenu(){
        dimImage.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);
        mOverlayMenuVisible = false;
    }
}
