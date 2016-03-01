package cegepsth.taskplanner.drawer;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cegepsth.taskplanner.R;

/**
 * Created by Maxim on 2/10/2016.
 * Adapter pour le drawer
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private List<DrawerItem> mDrawerItems;
    private DrawerCallbacks mDrawerCallbacks;
    private int mPositionSelectionnee;
    private int mPositionTouchee = -1;

    /**
     * CTOR
     *
     * @param items La liste d'item
     */
    public DrawerAdapter(List<DrawerItem> items) {
        mDrawerItems = items;
    }

    /**
     * Getter pour le pointeur vers l'activité qui implémente les callbacks
     *
     * @return
     */
    public DrawerCallbacks getDrawerCallbacks() {
        return mDrawerCallbacks;
    }

    /**
     * Setter pour le pointeur vers l'activité qui implémentera les callbacks
     *
     * @return
     */
    public void setDrawerCallbacks(DrawerCallbacks navigationDrawerCallbacks) {
        mDrawerCallbacks = navigationDrawerCallbacks;
    }

    /**
     * À la création du view holder.
     *
     * @param viewGroup Le view group
     * @param i         La position
     * @return Retourne le view holder
     */
    @Override
    public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_item_row, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                                                   @Override
                                                   public boolean onTouch(View v, MotionEvent event) {

                                                       switch (event.getAction()) {
                                                           case MotionEvent.ACTION_DOWN:
                                                               toucherPosition(viewHolder.getPosition());
                                                               return false;
                                                           case MotionEvent.ACTION_CANCEL:
                                                               toucherPosition(-1);
                                                               return false;
                                                           case MotionEvent.ACTION_MOVE:
                                                               return false;
                                                           case MotionEvent.ACTION_UP:
                                                               toucherPosition(-1);
                                                               return false;
                                                       }
                                                       return true;
                                                   }
                                               }
        );
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if (mDrawerCallbacks != null)
                                                           mDrawerCallbacks.onNavigationDrawerItemSelected(viewHolder.getPosition());
                                                   }
                                               }
        );

        return viewHolder;
    }

    /**
     * Lorsque le view holder est bindé
     *
     * @param viewHolder Le view holder
     * @param i          La position
     */
    @Override
    public void onBindViewHolder(DrawerAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.textView.setText(mDrawerItems.get(i).getText());
        viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(mDrawerItems.get(i).getDrawable(), null, null, null);

        if (mPositionSelectionnee == i || mPositionTouchee == i) {
            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(R.color.selected_gray));
        } else {
            viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    /**
     * Permet de toucher une position du drawer.
     *
     * @param position La position touchée
     */
    private void toucherPosition(int position) {
        int positionPrecedante = mPositionTouchee;
        mPositionTouchee = position;
        if (positionPrecedante >= 0)
            notifyItemChanged(positionPrecedante);
        if (position >= 0)
            notifyItemChanged(position);
    }

    /**
     * Permet de sélectionner une position dans le drawer.
     *
     * @param position La position à sélectionnée
     */
    public void selectionnerPosition(int position) {
        int positionPrecedante = mPositionSelectionnee;
        mPositionSelectionnee = position;
        notifyItemChanged(positionPrecedante);
        notifyItemChanged(position);
    }

    /**
     * Retourne le nombre d'item que le drawer contient
     *
     * @return Le nombre d'item dans le drawer
     */
    @Override
    public int getItemCount() {
        return mDrawerItems != null ? mDrawerItems.size() : 0;
    }

    /**
     * Le conteneur pour les vues du drawer
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_name);
        }
    }
}
