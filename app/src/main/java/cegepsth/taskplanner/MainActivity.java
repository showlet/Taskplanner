package cegepsth.taskplanner;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cegepsth.taskplanner.drawer.DrawerCallbacks;
import cegepsth.taskplanner.drawer.DrawerFragment;
import cegepsth.taskplanner.model.Project;
import cegepsth.taskplanner.utils.CacheManager;
import cegepsth.taskplanner.utils.PreferencesManager;
import cegepsth.taskplanner.utils.SessionManager;

/**
 * Activitée principale de l'application, elle gere la toolbar et le drawer.
 * Elle s'occupe de lancer les fragments
 */
public class MainActivity extends AppCompatActivity implements DrawerCallbacks {
    private Toolbar mToolbar;
    private DrawerFragment mDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferencesManager.initializeInstance(getApplicationContext());
        SessionManager.initializeInstance(getApplicationContext());
        CacheManager.initializeInstance(getApplicationContext());

        //Active httpCaching
        CacheManager.getInstance().enableHttpCaching();

        //Vérifie si l'utilisateur est connecté
        if (SessionManager.getInstance().checkLogin())
            finish();

        //Setup la toolbar
        initialiserToolbar();
        initialiserDrawer();
    }

    /**
     * Initialise la toolbar
     */
    private void initialiserToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    /**
     * Initialise le Drawer
     */
    private void initialiserDrawer() {
        mDrawerFragment = (DrawerFragment) getFragmentManager().findFragmentById(R.id.drawer_fragment);
        mDrawerFragment.setup(R.id.drawer_fragment, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        mDrawerFragment.setUserData(PreferencesManager.getInstance().getUserName(), PreferencesManager.getInstance().getUserEmail(), BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
    }

    /**
     * Crée le menu de la toolbar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Évènemnet appelé quand on selectionne un element dans le menu de la toolbar
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch (id) {
            case R.id.action_settings: {
                return true;
            }
            case R.id.action_logout: {
                SessionManager.getInstance().logout();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Evenement appelé quand on change item dans un drawer
     *
     * @param position
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = getSupportFragmentManager().findFragmentByTag(ListeTodayTachesFragment.TAG);
                if (fragment == null) {
                    fragment = new ListeTodayTachesFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, ListeTodayTachesFragment.TAG).commit();
                break;
            case 1:
                fragment = getSupportFragmentManager().findFragmentByTag(ListeProjetsFragment.TAG);
                if (fragment == null) {
                    fragment = new ListeProjetsFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, ListeProjetsFragment.TAG).commit();
                break;
            case 2:
                fragment = getSupportFragmentManager().findFragmentByTag(ListeTachesFragment.TAG);
                if (fragment == null) {
                    fragment = new ListeTachesFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, ListeTachesFragment.TAG).commit();
                break;
            case 3:
                // TODO: 2/17/2016 START SETTING ACTIVITY
                break;
        }
    }

    /**
     * Évènemnet appelé quand on appuie sur le boutton Back
     */
    @Override
    public void onBackPressed() {

        String tag = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getTag();

        if (mDrawerFragment.isDrawerOpen()) {
            mDrawerFragment.closeDrawer();
        } else if (tag == ListeProjetsFragment.TAG) {
            if (((ListeProjetsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).overlayMenuIsVisible()) {
                ((ListeProjetsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).closeOverlayMenu();
            } else {
                mDrawerFragment.onNavigationDrawerItemSelected(0);
            }
        } else if (tag == DetailsProjetFragment.TAG) {
            if (((DetailsProjetFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).overlayMenuIsVisible()) {
                ((DetailsProjetFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).closeOverlayMenu();
            } else {
                mDrawerFragment.onNavigationDrawerItemSelected(1);
            }
        } else if (tag == DetailsTacheFragment.TAG) {
            if (((DetailsTacheFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).overlayMenuIsVisible()) {
                ((DetailsTacheFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).closeOverlayMenu();
            } else {
                mDrawerFragment.onNavigationDrawerItemSelected(1);
            }
        } else if (tag != ListeTodayTachesFragment.TAG) {
            mDrawerFragment.onNavigationDrawerItemSelected(0);
        } else {
            super.onBackPressed();
        }

    }

}
