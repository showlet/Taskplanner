package cegepsth.taskplanner.drawer;

import android.graphics.drawable.Drawable;

/**
 * Created by Maxim on 2/10/2016.
 * Modèles pour les items du drawer.
 */
public class DrawerItem {
    private String mText;
    private Drawable mDrawable;

    /**
     * CTOR
     * @param text Le texte d'affichage
     * @param drawable L'icon de l'item
     */
    public DrawerItem(String text, Drawable drawable) {
        mText = text;
        mDrawable = drawable;
    }

    /**
     * Getter pour le texte à afficher
     * @return Le nom d'affichage de l'item
     */
    public String getText() {
        return mText;
    }

    /**
     * Setter pour le texte de l'item
     * @param text Le texte d'affichage
     */
    public void setText(String text) {
        mText = text;
    }

    /**
     * Getter pour l'icon à afficher
     * @return L'icon  de l'item
     */
    public Drawable getDrawable() {
        return mDrawable;
    }

    /**
     * Setter pour l'icon de l'item
     * @param drawable L'icon
     */
    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }
}

