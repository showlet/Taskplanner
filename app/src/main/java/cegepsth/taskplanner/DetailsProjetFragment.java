package cegepsth.taskplanner;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cegepsth.taskplanner.database.IProjetDAO;
import cegepsth.taskplanner.database.ITacheDAO;
import cegepsth.taskplanner.database.ProjetDAO;
import cegepsth.taskplanner.database.TacheDAO;
import cegepsth.taskplanner.model.Project;
import cegepsth.taskplanner.model.Task;
import cegepsth.taskplanner.tablayout.ProjetViewPagerAdapter;
import cegepsth.taskplanner.tablayout.SlidingTabLayout;
import cegepsth.taskplanner.utils.SessionManager;

/**
 * Created by Showlet on 2016-02-12.
 */
public class DetailsProjetFragment extends android.support.v4.app.Fragment {

    public static final String TAG = "DetailsProjetFragment";

    /*DAO*/
    private IProjetDAO mProjetDAO;
    private ITacheDAO mTacheDAO;

    private ViewPager mViewPager;
    private ProjetViewPagerAdapter mProjetViewPagerAdapter;
    private SlidingTabLayout mTabsLayout;
    private FloatingActionButton mAddTaskButton;
    private FloatingActionButton mEditProjectButton;
    private Button CreerTacheButton;
    private Project mProject;
    private Context context;
    private FrameLayout ajouterTache;
    private ImageView dimImage;
    private CalendarView calTache;
    private TextView txtNomNouvelleTache;
    private TextView txtProjet;
    private TextView txtDescriptionNouvelleTache;
    private String dateTache;
    private Boolean mOverlayMenuVisible;

    // Modifier Projet
    private CalendarView calModifProjet;
    private TextView txtModifProjet;
    private TextView txtModifDescription;
    private String dateProjet;
    private FrameLayout modifierProjet;
    private Button ModifProjetButton;




    /**
     * Setter pour le projet à afficher
     * @param projet Le projet
     */
    public void setProjet(Project projet)
    {
        mProject = projet;
    }

    /**
     * À la création
     * @param savedInstanceState les saved instance
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjetDAO = new ProjetDAO();
        mTacheDAO = new TacheDAO();
        mOverlayMenuVisible = false;
    }

    /**
     * À la création de la vue.
     * @param inflater L'inflateur de layout
     * @param container Le container
     * @param savedInstanceState Les saved instance
     * @return La vue
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Inflate la vuew et set l'élévation de la toolbar à 0 puisqu'on a un tab layout
        View view = inflater.inflate(R.layout.fragment_details_projet,container,false);
        getActivity().findViewById(R.id.toolbar).setElevation(0);


        if (mProject != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getContext().getString(R.string.Actionbar_project));

        context = container.getContext();
        // Crée le view pager adapter
        String titresTab[]={getString(R.string.tabOverview),getString(R.string.tabTasks)};
        mProjetViewPagerAdapter =  new ProjetViewPagerAdapter(getActivity().getSupportFragmentManager(),mProject,titresTab);

        // Set the view pager layout et set l'adapter
        mViewPager = (ViewPager) view.findViewById(R.id.tabLayoutPager);
        mViewPager.setAdapter(mProjetViewPagerAdapter);

        // Set le tab layout et split l'espace égal pour chaque tab
        mTabsLayout = (SlidingTabLayout) view.findViewById(R.id.tabs);
        mTabsLayout.setDistributeEvenly(true);

        // Set la couleur custom pour pour le slider de la scrollbar
        mTabsLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Set le view pager pour le tab layout
        mTabsLayout.setViewPager(mViewPager);

        //Setup le add button
        mAddTaskButton = (FloatingActionButton) view.findViewById(R.id.fabNewTask);
        mAddTaskButton.setBackgroundTintList(getResources().getColorStateList(R.color.fab_new_color_state_list));
        mAddTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNouvelleTacheOverlay();
            }
        });
        ajouterTache = (FrameLayout) view.findViewById(R.id.ajouterTacheLayout);
        dimImage = (ImageView) view.findViewById(R.id.imgDim);
        dimImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                closeOverlayMenu();
            }
        });

        calTache = (CalendarView) view.findViewById(R.id.callAjouterTache);
        txtProjet = (TextView) view.findViewById(R.id.currentProject);

        calModifProjet = (CalendarView) view.findViewById(R.id.calModifierProjet);
        txtModifProjet = (TextView) view.findViewById(R.id.txtModifierNomProjet);
        txtModifDescription = (TextView) view.findViewById(R.id.txtModifierDescriptionProjet);
        modifierProjet = (FrameLayout) view.findViewById(R.id.modifierProjetLayout);
        ModifProjetButton = (Button) view.findViewById(R.id.btnModifierProjet);
        ModifProjetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editerProjet();
            }
        });


        mEditProjectButton = (FloatingActionButton) view.findViewById(R.id.fabEditProject);
        mEditProjectButton.setBackgroundTintList(getResources().getColorStateList(R.color.fab_edit_color_state_list));
        mEditProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initModifProjet();
            }
        });

        txtNomNouvelleTache = (TextView) view.findViewById(R.id.txtAddNomTache);
        txtDescriptionNouvelleTache = (TextView) view.findViewById(R.id.txtAddDescriptionTache);

        // Calendar
        calTache.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                month += 1;
                dateTache = year + "-" + (month) + "-" + dayOfMonth;
            }//met
        });

        // Calendar Modifier projet
        calModifProjet.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                month += 1;
                dateProjet = year + "-" + (month) + "-" + dayOfMonth;
            }//met
        });

        CreerTacheButton = (Button) view.findViewById(R.id.btnCreerTache);
        CreerTacheButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ajouterTacheClick();
            }
        });

        return view;
    }


    /**
     * Prepare le overlay pour ajouter une nouvelle tache.
     */
    private void initNouvelleTacheOverlay()
    {
        Date cal = new Date();
        calTache.setMinDate(cal.getTime());
        dimImage.setVisibility(View.VISIBLE);
        ajouterTache.setVisibility(View.VISIBLE);
        mOverlayMenuVisible = true;
    }

    /**
     * Ajoute une nouvelle tache dans la bd
     */
    private void ajouterTacheClick()
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

        Task task = new Task();
        task.Name = txtNomNouvelleTache.getText().toString();
        task.Description = txtDescriptionNouvelleTache.getText().toString();
        task.Deadline = date;
        task.UserId = SessionManager.getInstance().getCurrentUserId();
        task.ProjectId = mProject.Id;

        mTacheDAO.insertTache(task);
        mProjetViewPagerAdapter.getProjet().lstTask.add(task);

        ajouterTache.setVisibility(View.GONE);
        dimImage.setVisibility(View.GONE);
    }

    /**
     * Affiche le menu pour modifier une projet
     */
    private void initModifProjet() {
        Date cal = new Date();
        calModifProjet.setMinDate(cal.getTime());
        txtModifDescription.setText(mProject.Description);
        txtModifProjet.setText(mProject.Name);
        calModifProjet.setDate(mProject.Deadline.getTime());
        dimImage.setVisibility(View.VISIBLE);
        modifierProjet.setVisibility(View.VISIBLE);
        mOverlayMenuVisible = true;
    }

    /**
     * M-a-j le projet.
     */
    private void editerProjet()
    {
        SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = ss.parse(dateProjet);
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        mProject.Name = txtModifProjet.getText().toString();
        mProject.Description = txtModifDescription.getText().toString();
        mProject.Deadline = date;
        mProjetDAO.updateProjet(mProject);

        dimImage.setVisibility(View.GONE);
        modifierProjet.setVisibility(View.GONE);
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
        modifierProjet.setVisibility(View.GONE);
        ajouterTache.setVisibility(View.GONE);
        mOverlayMenuVisible = false;
    }
}
