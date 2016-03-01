package cegepsth.taskplanner.database;

import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;

import com.loopj.android.http.RequestParams;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import cegepsth.taskplanner.MainActivity;
import cegepsth.taskplanner.model.Project;
import cegepsth.taskplanner.model.Task;
import cegepsth.taskplanner.utils.SessionManager;

import static org.junit.Assert.*;

/**
 * Created by Maxim on 2/20/2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DAOTest extends ActivityInstrumentationTestCase2<MainActivity> {

    int testId = 9;
    String testEmail = "junit@taskplanner.com";
    String testName = "J Unit";
    String testToken = "2a36eedadb2177722ef76c850dbf3b3c";
    int testTaskId = 112;       //ID dans la bd
    int testProjectId = 29;     //ID dans la bd

    IProjetDAO projetDAO;
    ITacheDAO tacheDAO;


    Project getProject = null;
    Task getTache = null;

    Project updatedProject = null;
    Task updatedTask = null;


    /**
     * CTOR
     */
    public DAOTest() { super(MainActivity.class); }

    /**
     * Opérations de base qui doivent être fait avant de faire un test.
     */
    private void prepare() {
        SessionManager.initializeInstance(getActivity());
        SessionManager.getInstance().createLoginSession(testId, testName, testEmail, testToken);
        projetDAO = new ProjetDAO();
        tacheDAO = new TacheDAO();
    }

    /**
     * Test l'insertion d'un projet dans la base de données
     *
     * @throws Throwable Exception pour le runnable
     */
    @Test
    public void testAInsertProjet() throws Throwable {
        /*Prépare l'environment de test*/
        prepare();

        /*On crée un projet à insert dans la BD*/
        final Project projet = new Project();
        projet.Deadline = new Date();
        projet.Description = "Unit test project description";
        projet.Name = "Unit test insert project";
        projet.OwnerId = testId;

         /*CountDownLatch pour mettre le test en sleep afin d'attendre les callback des requêtes web*/
        final CountDownLatch signal = new CountDownLatch(1);

        /*Insert un nouveau projet*/
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                projetDAO.insertProjet(projet);
            }
        });

        /*On sleep pour attendre le callback*/
        signal.await(5, TimeUnit.SECONDS);

        //Si le projet a bien été inséré le id va avoir été m-a-j
        assertNotEquals(0, projet.Id);
    }

    /**
     *
     * Test l'insertion d'une tâche dans la base de données
     *
     * @throws Throwable Exception pour le runnable
     */
    @Test
    public void testBInsertTache() throws Throwable {
        /*Prépare l'environment de test*/
        prepare();

        /*On crée une nouvelle tache à insérer dans la BD*/
        final Task tache = new Task();
        tache.Deadline = new Date();
        tache.Description = "Unit test tache description";
        tache.Name = "Unit test tache project";
        tache.ProjectId = testProjectId;

         /*CountDownLatch pour mettre le test en sleep afin d'attendre les callback des requêtes web*/
        final CountDownLatch signal = new CountDownLatch(1);

        /*Insert une nouvelle tâche*/
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                tacheDAO.insertTache(tache);
            }
        });

        /*On sleep pour attendre le callback*/
        signal.await(5, TimeUnit.SECONDS);

        //Si la tache a bien été inséré le id va avoir été m-a-j
        assertNotEquals(0, tache.Id);
    }

    /**
     * Test le READ d'un projet de la base de données
     *
     * @throws Throwable Exception pour le runnable
     */
    @Test
    public void testCGetProjet() throws Throwable {
        /*Prépare l'environment de test*/
        prepare();

         /*CountDownLatch pour mettre le test en sleep afin d'attendre les callback des requêtes web*/
        final CountDownLatch signal = new CountDownLatch(1);

        /*On GET 1 projet de la BD*/
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                projetDAO.getProjet(testProjectId, new DAOCallback() {
                    @Override
                    public void run() {
                        getProject = (Project) getResponse();
                    }
                });
            }
        });

        /*On sleep pour attendre le callback*/
        signal.await(5, TimeUnit.SECONDS);

        //On test que l'objet ait bien été retourné sans quoi il sera null
        assertNotNull(getProject);
    }

    /**
     * Test le READ d'une tâche de la base de données
     *
     * @throws Throwable Exception pour le runnable
     */
    @Test
    public void testDGetTache() throws Throwable {
        /*Prépare l'environment de test*/
        prepare();

         /*CountDownLatch pour mettre le test en sleep afin d'attendre les callback des requêtes web*/
        final CountDownLatch signal = new CountDownLatch(1);

        /*On test le GET d'une tâce*/
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                tacheDAO.getTache(testTaskId, new DAOCallback() {
                    @Override
                    public void run() {
                        getTache = (Task) getResponse();
                    }
                });
            }
        });

        /*On sleep pour attendre le callback*/
        signal.await(5, TimeUnit.SECONDS);

        //On test que l'objet ait bien été retourné sans quoi il sera null
        assertNotNull(getTache);
    }


    /**
     *
     * Test le update d'un projet de la base de données
     *
     * @throws Throwable Exception pour le runnable
     */
    @Test
    public void testEUpdateProjet() throws Throwable {
        /*Prépare l'environment de test*/
        prepare();
        String updateDesc = UUID.randomUUID().toString();
        String updateNom = UUID.randomUUID().toString();

        //On crée un projet à update
        final Project projet = new Project();
        projet.Id = testProjectId;
        projet.OwnerId = testId;
        projet.Description = updateDesc;
        projet.Name = updateNom;

         /*CountDownLatch pour mettre le test en sleep afin d'attendre les callback des requêtes web*/
        final CountDownLatch signal = new CountDownLatch(1);

        /*On met l'objet à jour dans la BD*/
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                projetDAO.updateProjet(projet);
            }
        });

        /*On sleep pour attendre le callback*/
        signal.await(5, TimeUnit.SECONDS);

        /*On get le projet pour le comparer avec les valeurs m-a-j*/
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                projetDAO.getProjet(projet.Id, new DAOCallback() {
                    @Override
                    public void run() {
                        updatedProject = (Project) getResponse();
                    }
                });
            }
        });

        /*On sleep pour attendre le callback*/
        signal.await(5, TimeUnit.SECONDS);

        /*On test que les valeurs on bien été m-a-j*/
        assertEquals(projet.Name, updatedProject.Name);
        assertEquals(projet.Description, updatedProject.Description);
    }

    /**
     * Test le update d'une tâche de la base de données
     *
     * @throws Throwable Exception pour le runnable
     */
    @Test
    public void testFUpdateTache() throws Throwable {
        /*Prépare l'environment de test*/
        prepare();

        String updateDesc = UUID.randomUUID().toString();
        String updateNom = UUID.randomUUID().toString();

        /*On crée une tâche à m-a-j*/
        final Task tache = new Task();
        tache.Id = testTaskId;
        tache.ProjectId = testProjectId;
        tache.UserId = testId;
        tache.Description = updateDesc;
        tache.Name = updateNom;

        /*CountDownLatch pour mettre le test en sleep afin d'attendre les callback des requêtes web*/
        final CountDownLatch signal = new CountDownLatch(1);

        /*Update la tâche*/
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                tacheDAO.updateTache(tache);
            }
        });

        /*On sleep pour attendre le callback*/
        signal.await(5, TimeUnit.SECONDS);

        /*Get l'objet que nous venons de m-a-j*/
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                tacheDAO.getTache(tache.Id, new DAOCallback() {
                    @Override
                    public void run() {
                        updatedTask = (Task) getResponse();
                    }
                });
            }
        });

        /*On sleep pour attendre le callback*/
        signal.await(5, TimeUnit.SECONDS);

        /*Test si l'objet a bien été m-a-j*/
        assertEquals(tache.Name, updatedTask.Name);
        assertEquals(tache.Description, updatedTask.Description);
    }


    /**
     *
     * Test l'opération delete du DAO
     *
     * @throws Throwable Exception pour le runTestOnUiThread
     */
    @Test
    public void testGDeleteTache() throws Throwable {
        /*Prépare l'environment de test*/
        prepare();

        String desc = UUID.randomUUID().toString();
        String nom = UUID.randomUUID().toString();

        /*On crée un objet à insérer pour ensuite le deleter */
        final Task tache = new Task();
        tache.ProjectId = testProjectId;
        tache.UserId = testId;
        tache.Description = desc;
        tache.Name = nom;

        /*CountDownLatch pour mettre le test en sleep afin d'attendre les callback des requêtes web*/
        final CountDownLatch signal = new CountDownLatch(1);

        /*Insert une tâche dans la BD*/
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                tacheDAO.insertTache(tache);
            }
        });

        /*On sleep pour attendre le callback*/
        signal.await(5, TimeUnit.SECONDS);

        /*On delete la tâche que nous avons insérée*/
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                tacheDAO.deleteTache(tache);
            }
        });

        /*On sleep pour attendre le callback*/
        signal.await(5, TimeUnit.SECONDS);

        /*Test si l'objet a bien été delete*/
        assertEquals(0, tache.Id);
        assertEquals(0, tache.ProjectId);
        assertEquals(0, tache.UserId);
        assertNull(tache.Closed);
        assertNull(tache.Deadline);
        assertNull(tache.Description);
        assertNull(tache.Created);
        assertNull(tache.IsClosed);
        assertNull(tache.Name);
    }
}