package cegepsth.taskplanner.drawer;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cegepsth.taskplanner.R;

/**
 * Created by Maxim on 2/10/2016.
 * Classe qui gère le fragment du drawer.
 */
public class DrawerFragment extends Fragment implements DrawerCallbacks {

    //Clé pour obtenir la dernière position sélectionnée du drawer.
    private static final String DRAWER_SAVED_POSITION = "DRAWER_SAVED_POSITION";

    //Selon les guidelines du Material design, on devrait afficher le drawer à chaques ouvertures
    //jusqu'à ce que l'utilisateur l'ouvre par lui même.
    private static final String USER_TOGGLED_DRAWER = "USER_TOGGLED_DRAWER";


    //Pointeur vers l'activité qui implémente les callbacks
    private DrawerCallbacks mCallbacks;

    //Link l'action bar au drawer
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mDrawerAdapter;
    private RecyclerView mDrawerList;
    private View mFragmentContainerView;

    private int mPositionCourrante = 0;
    private boolean mInstanceSauvegarde;
    private boolean mEstAuCourrantDuDrawer;

    /**
     * On regarde si l'utilisateur est au courrant du drawer. Si il y a une instance sauvegardé
     * on le restore avec la dernière position
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEstAuCourrantDuDrawer = sp.getBoolean(USER_TOGGLED_DRAWER, false);

        if (savedInstanceState != null) {
            mPositionCourrante = savedInstanceState.getInt(DRAWER_SAVED_POSITION);
            mInstanceSauvegarde = true;
        }
    }

    /**
     * À la création de la vue
     *
     * @param inflater           L'inflater de vue
     * @param container          Le conteneur
     * @param savedInstanceState Le bundle
     * @return La vue
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        //Retourne le recycler qui contient la liste d'item
        mDrawerList = (RecyclerView) view.findViewById(R.id.drawerItems);

        //Setup le layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDrawerList.setLayoutManager(layoutManager);
        mDrawerList.setHasFixedSize(true);

        //Ajoutes les items au drawer et set l'adapter
        final List<DrawerItem> drawerItems = getMenu();
        mDrawerAdapter = new DrawerAdapter(drawerItems);
        mDrawerAdapter.setDrawerCallbacks(this);
        mDrawerList.setAdapter(mDrawerAdapter);
        selectionnerItem(mPositionCourrante);
        return view;
    }

    /**
     * L'état du drawer (open close)
     *
     * @return Vrai si le drawer est ouvert.
     */
    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Retourn le drawer adpater
     * @return
     */
    public DrawerAdapter getDrawerAdapter() {
        return mDrawerAdapter;
    }

    /**
     * Getter pour le Actionbardrawer toggle (bouton pour toggle le drawer dans la toolbar)
     *
     * @return Le ActionBarDrawerToggle de l'action bar
     */
    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return mActionBarDrawerToggle;
    }

    /**
     * Getter pour le layout du drawer
     *
     * @return
     */
    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    /**
     * A la sélection d'un item du drawer.
     *
     * @param position La position sélectionnée
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selectionnerItem(position);
    }

    /**
     * Getter pour les items du drawer
     *
     * @return La liste des items du drawer.
     */
    public List<DrawerItem> getMenu() {
        List<DrawerItem> items = new ArrayList<DrawerItem>();

        //TODO: ADD DRAWER ITEMS HERE
        items.add(new DrawerItem(getString(R.string.today_task), getResources().getDrawable(R.drawable.ic_event_white_36dp)));
        items.add(new DrawerItem(getString(R.string.my_projects), getResources().getDrawable(R.drawable.ic_trending_up_white_36dp)));
        items.add(new DrawerItem(getString(R.string.my_tasks), getResources().getDrawable(R.drawable.ic_new_releases_white_36dp)));
        items.add(new DrawerItem(getString(R.string.action_settings), getResources().getDrawable(R.drawable.ic_settings_white_36dp)));

        return items;
    }

    /**
     * Ce qui utilise le drawer doit utiliser cette méthode pour l'initialiser
     *
     * @param fragmentId   L'Id du fragment du drawer
     * @param drawerLayout Le layout du drawer.
     * @param toolbar      La toolbar de l'activité
     */
    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentContainerView = (View) getActivity().findViewById(fragmentId).getParent();
        mDrawerLayout = drawerLayout;

        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark));

        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            /**
             * À la fermeture du drawer
             * @param drawerView La vue du drawer
             */
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) return;

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            /**
             * À l'ouverture du drawer
             * @param drawerView La vue du drawer
             */
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) return;
                if (!mEstAuCourrantDuDrawer) {
                    mEstAuCourrantDuDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(USER_TOGGLED_DRAWER, true).apply();
                }
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        //Si le user n'est pas au courrant du drawer on l'ouvre a louverture
        if (!mEstAuCourrantDuDrawer && !mInstanceSauvegarde) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Ajoute le sync state du drawer en runnable
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });


        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    /**
     * Permet de sélectionner un item.
     *
     * @param position La position à sélectionner
     */
    private void selectionnerItem(int position) {
        mPositionCourrante = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
        ((DrawerAdapter) mDrawerList.getAdapter()).selectionnerPosition(position);
    }

    /**
     * Permet d'ouvrir le drawer
     */
    public void openDrawer() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    /**
     * Permet de fermer le drawer
     */
    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mFragmentContainerView);
    }

    /**
     * Lorsque le drawer est attaché à une activité
     *
     * @param activity l'activité
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (DrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    /**
     * Lorsque le drawer est détaché
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * Sauvegarde la position sélectionnée dans les préférences
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DRAWER_SAVED_POSITION, mPositionCourrante);
    }

    /**
     * Fait suivre les nouvelles configuration à la composante pour toggle le drawer
     *
     * @param newConfig Les nouvelles configuration
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Set les données du user à afficher dans la banière du drawer.
     *
     * @param user   Le nom
     * @param email  Le email
     * @param avatar L'image
     */
    public void setUserData(String user, String email, Bitmap avatar) {
        ImageView avatarContainer = (ImageView) mFragmentContainerView.findViewById(R.id.imgAvatar);
        ((TextView) mFragmentContainerView.findViewById(R.id.txtUserEmail)).setText(email);
        ((TextView) mFragmentContainerView.findViewById(R.id.txtUsername)).setText(user);
        avatarContainer.setImageDrawable(new RoundImage(avatar));
    }

    /**
     * Retourne la vue du drawer
     *
     * @return La vue du drawer
     */
    public View getDrawer() {
        return mFragmentContainerView.findViewById(R.id.appDrawer);
    }

    /**
     * Class d'utilitaire pour dessiner une image ronde.
     */
    public static class RoundImage extends Drawable {
        private final Bitmap mBitmap;
        private final Paint mPaint;
        private final RectF mRectF;
        private final int mBitmapWidth;
        private final int mBitmapHeight;

        /**
         * CTOR
         *
         * @param bitmap L'image
         */
        public RoundImage(Bitmap bitmap) {
            mBitmap = bitmap;
            mRectF = new RectF();
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            final BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mPaint.setShader(shader);

            mBitmapWidth = mBitmap.getWidth();
            mBitmapHeight = mBitmap.getHeight();
        }

        /**
         * Dessine l'image dans un canvas
         *
         * @param canvas Le canvas qui contiendra l'image
         */
        @Override
        public void draw(Canvas canvas) {
            canvas.drawOval(mRectF, mPaint);
        }

        /**
         * Au changement des limites
         *
         * @param bounds
         */
        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            mRectF.set(bounds);
        }

        /**
         * Setter pour l'alpha
         *
         * @param alpha La valeur de l'alpha
         */
        @Override
        public void setAlpha(int alpha) {
            if (mPaint.getAlpha() != alpha) {
                mPaint.setAlpha(alpha);
                invalidateSelf();
            }
        }

        /**
         * Setter pour le filtre de couleur
         *
         * @param cf Le filre de couleur
         */
        @Override
        public void setColorFilter(ColorFilter cf) {
            mPaint.setColorFilter(cf);
        }

        /**
         * Getter pour l'opacité
         *
         * @return La valeur de l'opacité
         */
        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        /**
         * Getter pour la largeur de l'intrinsèque
         *
         * @return La largeur de l'intrinsèque
         */
        @Override
        public int getIntrinsicWidth() {
            return mBitmapWidth;
        }

        /**
         * Getter pour la hauteur de l'intrinsèque
         *
         * @return La hauteur de l'intrinsèque
         */
        @Override
        public int getIntrinsicHeight() {
            return mBitmapHeight;
        }

        /**
         * Setter pour l'anti aliasing
         *
         * @param aa La vaeur de l'anti aliasing
         */
        public void setAntiAlias(boolean aa) {
            mPaint.setAntiAlias(aa);
            invalidateSelf();
        }

        /**
         * Setter pour le filter
         *
         * @param filter La valeur du fiter
         */
        @Override
        public void setFilterBitmap(boolean filter) {
            mPaint.setFilterBitmap(filter);
            invalidateSelf();
        }

        /**
         * Setter pour le dither
         *
         * @param dither La valeur du dither
         */
        @Override
        public void setDither(boolean dither) {
            mPaint.setDither(dither);
            invalidateSelf();
        }

        /**
         * Getter pour le bitmap
         *
         * @return Le bitmap
         */
        public Bitmap getBitmap() {
            return mBitmap;
        }

    }
}
