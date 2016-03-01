package cegepsth.taskplanner;

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

import cegepsth.taskplanner.database.ITacheDAO;
import cegepsth.taskplanner.database.TacheDAO;
import cegepsth.taskplanner.model.Task;
import cegepsth.taskplanner.tablayout.SlidingTabLayout;
import cegepsth.taskplanner.tablayout.TacheViewPagerAdapter;

/**
 * Created by Showlet on 2016-02-12.
 */
public class DetailsTacheFragment extends android.support.v4.app.Fragment {
    public static final String TAG = "DetailsTacheFragment";

    /*DAO*/
    private ITacheDAO mTacheDAO;

    private ViewPager mViewPager;
    private TacheViewPagerAdapter mTacheViewPagerAdapter;
    private SlidingTabLayout mTabsLayout;
    private FloatingActionButton mCloseButton;
    private FloatingActionButton mEditButton;
    private FloatingActionButton mDeleteButton;
    private Boolean mOverlayMenuVisible;
    private Task mTache;

    // Modifier Taches
    private CalendarView calModifTache;
    private TextView txtModifTacheNom;
    private TextView txtModifTacheDescription;
    private FrameLayout modifierTache;
    private Button btnModifierTache;
    private ImageView imgDim;
    private String dateTache;



    /**
     * Permet de setter la tache à afficher.
     * @param tache La tache à afficher.
     */
    public void setTache(Task tache)
    {
        mTache = tache;
    }

    /**
     *
     * À la création du fragment
     *
     * @param savedInstanceState Les saved instance
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTacheDAO = new TacheDAO();
        mOverlayMenuVisible = false;
    }

    /**
     *
     * À la création de la vue
     *
     * @param inflater Le layout inflater
     * @param container Le container pour le fragment
     * @param savedInstanceState Les saved instance
     * @return La vue
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_tache,container,false);

        getActivity().findViewById(R.id.toolbar).setElevation(0);

        if(mTache != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mTache.Name);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        String titresTab[]={getString(R.string.tabOverview),getString(R.string.tabMessages)};
        mTacheViewPagerAdapter =  new TacheViewPagerAdapter(getActivity().getSupportFragmentManager(),mTache,titresTab);

        // Assigning ViewPager View and setting the adapter
        mViewPager = (ViewPager) view.findViewById(R.id.tabLayoutPager);
        mViewPager.setAdapter(mTacheViewPagerAdapter);

        // Assiging the Sliding Tab Layout View
        mTabsLayout = (SlidingTabLayout) view.findViewById(R.id.tabs);
        mTabsLayout.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        mTabsLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        mTabsLayout.setViewPager(mViewPager);

        //On setup le bouton pour fermer une tache.
        mCloseButton = (FloatingActionButton) view.findViewById(R.id.fabCloseTask);
        mCloseButton.setBackgroundTintList(getResources().getColorStateList(R.color.fab_new_color_state_list));
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeTache();
            }
        });

        //On setup le bouton pour editer une tache
        mEditButton = (FloatingActionButton) view.findViewById(R.id.fabEditTask);
        mEditButton.setBackgroundTintList(getResources().getColorStateList(R.color.fab_edit_color_state_list));
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initEdit();
            }
        });

        //On setup le bouton pour delete une tache
        mDeleteButton = (FloatingActionButton) view.findViewById(R.id.fabDeleteTask);
        mDeleteButton.setBackgroundTintList(getResources().getColorStateList(R.color.fab_close_color_state_list));
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTache();
            }
        });
        //return super.onCreateView(inflater, container, savedInstanceState);


        calModifTache = (CalendarView) view.findViewById(R.id.calModifierTache);
        calModifTache.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                month += 1;
                dateTache = year + "-" + (month) + "-" + dayOfMonth;
            }//met
        });
        txtModifTacheNom = (TextView) view.findViewById(R.id.txtModifierNomTache);
        txtModifTacheDescription = (TextView) view.findViewById(R.id.txtModifierDescriptionTache);
        modifierTache = (FrameLayout) view.findViewById(R.id.modifierTacheLayout);
        btnModifierTache = (Button) view.findViewById(R.id.btnModifierTache);
        imgDim = (ImageView) view.findViewById(R.id.imgdim);
        imgDim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeOverlayMenu();
            }
        });
        btnModifierTache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTache();
            }
        });

        return view;
    }

    /**
     *  Ferme une tâche et sauve la modification sur le server;
     */
    private void closeTache()
    {
        mTache.IsClosed = true;
        mTache.Closed = new Date();
        mTacheDAO.updateTache(mTache);
    }

    /**
     * Affiche le menu pour editer une tache
     */
    private void initEdit() {
        txtModifTacheDescription.setText(mTache.Description);
        txtModifTacheNom.setText(mTache.Name);
        if (mTache.Deadline != null)
            calModifTache.setDate(mTache.Deadline.getTime());
        else
            calModifTache.setDate(new Date().getTime());
        modifierTache.setVisibility(View.VISIBLE);
        imgDim.setVisibility(View.VISIBLE);
        mOverlayMenuVisible = true;
    }


    /**
     * Edit une tâche et sauvegarde les modifications dans la BD
     */
    private void editTache()
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

        mTache.Name = txtModifTacheNom.getText().toString();
        mTache.Description = txtModifTacheDescription.getText().toString();
        mTache.Deadline = date;
        mTacheDAO.updateTache(mTache);

        imgDim.setVisibility(View.GONE);
        modifierTache.setVisibility(View.GONE);
        mTacheDAO.updateTache(mTache);
    }

    /**
     * Éfface une tâche sur le serveur.
     */
    private void deleteTache()
    {
        mTacheDAO.deleteTache(mTache);
        getActivity().onBackPressed();
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
    public void closeOverlayMenu()
    {
        imgDim.setVisibility(View.GONE);
        modifierTache.setVisibility(View.GONE);
        mOverlayMenuVisible = false;
    }
}

