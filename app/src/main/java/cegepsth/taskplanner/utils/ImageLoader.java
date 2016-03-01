package cegepsth.taskplanner.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by Maxim on 12/14/2015.
 * Classe utilitaire pour charger des images en tâche d'arrière plan.
 */
public class ImageLoader extends AsyncTask<String, String, Bitmap> {
    private static final String TAG = "ImageLoader";
    private final ImageView mImageview;
    private final ProgressBar mProgressBar;

    /**
     *
     * CTOR
     *
     * @param i Imageview
     * @param p Progress bar
     */
    public ImageLoader(ImageView i, ProgressBar p){
        mImageview = i;
        mProgressBar = p;
        mImageview.setVisibility(ImageView.GONE);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        mImageview.setImageBitmap(null);
    }

    /**
     *
     * Lance la background job pour charger l'image.
     *
     * @param args
     * @return L'image
     */
    protected Bitmap doInBackground(String... args) {
        Bitmap bitmap = null;
        try {
            URLConnection conn = new URL(args[0]).openConnection();
            conn.setUseCaches(true);
            bitmap = BitmapFactory.decodeStream((InputStream) conn.getContent());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     *
     * Lorsque la tâche d'arrière plan est terminé
     *
     * @param image L'image à afficher
     */
    protected void onPostExecute(Bitmap image) {

        if (image != null) {
            mProgressBar.setVisibility(ProgressBar.GONE);
            mImageview.setVisibility(ImageView.VISIBLE);
            mImageview.setImageBitmap(image);
            mImageview.setBackground(null);
        }
    }
}

