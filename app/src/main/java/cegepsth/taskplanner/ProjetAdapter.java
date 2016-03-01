package cegepsth.taskplanner;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import cegepsth.taskplanner.model.Project;
import cegepsth.taskplanner.utils.Tool;

/**
 * Created by Showlet on 2016-02-17.
 */
public class ProjetAdapter extends RecyclerView.Adapter<ProjetAdapter.ProjetViewHolder> {
    private List<Project> _lstProjet;
    private Context _context;

    public ProjetAdapter(List<Project> lstProjet) {
        _lstProjet = lstProjet;
    }

    @Override
    public int getItemCount() {
        return _lstProjet.size();
    }

    public Project getItem(int pos) {

        return _lstProjet.get(pos);
    }

    public void addItem(Project projet) {
        _lstProjet.add(projet);
        notifyItemInserted(_lstProjet.size() - 1);
    }

    @Override
    public ProjetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.projet_item_row, parent, false);
        _context = parent.getContext();
        return new ProjetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProjetViewHolder holder, int position) {
        Project projet = _lstProjet.get(position);
        Boolean foundColor = false;

        while (!foundColor) {
            int color = new Random().nextInt(5);
            switch (color) {
                case 0:
                    holder.tvCard.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.appBlue));
                    projet.Color = R.color.appBlue;
                    break;
                case 1:
                    holder.tvCard.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.appGreen));
                    projet.Color = R.color.appGreen;
                    break;
                case 2:
                    holder.tvCard.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.appOrange));
                    projet.Color = R.color.appOrange;
                    break;
                case 3:
                    holder.tvCard.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.appPink));
                    projet.Color = R.color.appPink;
                    break;
                case 4:
                    holder.tvCard.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.appPurple));
                    projet.Color = R.color.appPurple;
                    break;
                default:
                    holder.tvCard.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.appPurple));
                    projet.Color = R.color.appPurple;
                    break;
            }

            if (position > 0) {
                if (_lstProjet.get(position - 1).Color != projet.Color)
                    foundColor = true;
                else
                    foundColor = false;
            } else
                foundColor = true;
        }

        holder.tvTitre.setText(projet.Name);
        holder.tvDescription.setText(projet.Description);
        holder.tvNombreTaches.setText(projet.lstTask.size() + " " + _context.getString(R.string.tache));

        int nbTachesClosed = 0;
        for (int i = 0; i < projet.lstTask.size(); i++) {
            if (projet.lstTask.get(i).IsClosed)
                nbTachesClosed++;
        }

        if (nbTachesClosed > 0)
            holder.tvPbar.setProgress(Math.round(((float) nbTachesClosed / (float) projet.lstTask.size()) * 100));
        else
            holder.tvPbar.setProgress(0);

        if (projet.Deadline != null)
            holder.tvDeadline.setText(_context.getString(R.string.date_fini) + " " + Tool.DateToPrettyString(projet.Deadline));
        else
            holder.tvDeadline.setText("");
    }

    /**
     * Le Projet view holder, permets de passer des valeurs au recycler view.
     */
    public static class ProjetViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvDeadline;
        protected TextView tvTitre;
        protected TextView tvDescription;
        protected TextView tvNombreTaches;
        protected CardView tvCard;
        protected ProgressBar tvPbar;

        public ProjetViewHolder(View v) {
            super(v);
            tvDeadline = (TextView) v.findViewById(R.id.txtProjetDeadline);
            tvTitre = (TextView) v.findViewById(R.id.txtProjetTitre);
            tvDescription = (TextView) v.findViewById(R.id.txtProjetDescription);
            tvNombreTaches = (TextView) v.findViewById(R.id.txtNombreTaches);
            tvCard = (CardView) v.findViewById(R.id.cardProjet);
            tvPbar = (ProgressBar) v.findViewById(R.id.prgProjetProgres);
            this.setIsRecyclable(false);
        }
    }
}
