package cegepsth.taskplanner;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cegepsth.taskplanner.model.Task;
import cegepsth.taskplanner.utils.Tool;

/**
 * Created by Showlet on 2016-02-12.
 */
public class TacheAdapter extends RecyclerView.Adapter<TacheAdapter.TaskViewHolder> {
    private ArrayList<Task> _lstTache = new ArrayList<Task>();
    private Context _context;

    public TacheAdapter(List<Task> lstTache) {

        if (lstTache == null)
            return;

        // Order by IsClosed
        for (Task t : lstTache)
            if (!t.IsClosed)
                _lstTache.add(t);
        for (Task t : lstTache)
            if (t.IsClosed)
                _lstTache.add(t);
    }

    @Override
    public int getItemCount() {
        return _lstTache.size();
    }

    public Task getItem(int pos) {
        return _lstTache.get(pos);
    }

    public void addItem(Task task) {
        _lstTache.add(task);
        notifyItemInserted(_lstTache.size() - 1);
    }

    public void removeItem(Task task) {
        int pos = _lstTache.indexOf(task);
        _lstTache.remove(task);
        notifyItemRemoved(pos);
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.tache_item_row, parent, false);
        _context = parent.getContext();
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = _lstTache.get(position);
        Boolean foundColor = false;

        while (!foundColor) {
            int color;
            if (task.Color == null)
                color = new Random().nextInt(5);
            else {
                holder.tvCard.setCardBackgroundColor(ContextCompat.getColor(_context, task.Color));
                break;
            }
            switch (color) {
                case 0:
                    holder.tvCard.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.appBlue));
                    task.Color = R.color.appBlue;
                    break;
                case 1:
                    holder.tvCard.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.appGreen));
                    task.Color = R.color.appGreen;
                    break;
                case 2:
                    holder.tvCard.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.appOrange));
                    task.Color = R.color.appOrange;
                    break;
                case 3:
                    holder.tvCard.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.appPink));
                    task.Color = R.color.appPink;
                    break;
                case 4:
                    holder.tvCard.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.appPurple));
                    task.Color = R.color.appPurple;
                    break;
                default:
                    holder.tvCard.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.appPurple));
                    task.Color = R.color.appPurple;
                    break;
            }

            if (position > 0) {
                if (_lstTache.get(position - 1).Color != task.Color)
                    foundColor = true;
                else
                    foundColor = false;
            } else
                foundColor = true;
        }


        if (task.IsClosed) {
            holder.tvCard.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.lessjet));
            holder.tvUtilisateur.setTextColor(ContextCompat.getColor(_context, R.color.monsoon));
            holder.tvDeadline.setTextColor(ContextCompat.getColor(_context, R.color.monsoon));
            holder.tvTitre.setTextColor(ContextCompat.getColor(_context, R.color.monsoon));
        }

        holder.tvTitre.setText(task.Name);
        holder.tvUtilisateur.setText(String.valueOf(task.UserId));

        if (task.Deadline != null) {
            holder.tvDeadline.setText(_context.getString(R.string.date_fini) + " " + Tool.DateToPrettyString(task.Deadline));
            holder.tvDeadline.setVisibility(View.VISIBLE);
            holder.tvCreation.setVisibility(View.GONE);
        } else if (task.Closed != null) {
            holder.tvCreation.setText(_context.getString(R.string.date_ferme) + " " + Tool.DateToPrettyString(task.Closed));
            holder.tvCreation.setVisibility(View.VISIBLE);
            holder.tvDeadline.setVisibility(View.GONE);
        }
    }

    /**
     * Le task view holder, permets de passer des valeurs au recycler view.
     */
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvDeadline;
        protected TextView tvCreation;
        protected TextView tvTitre;
        protected TextView tvUtilisateur;
        protected CardView tvCard;

        public TaskViewHolder(View v) {
            super(v);
            tvDeadline = (TextView) v.findViewById(R.id.txtTaskDeadline);
            tvCreation = (TextView) v.findViewById(R.id.txtTaskCreation);
            tvTitre = (TextView) v.findViewById(R.id.txtTaskTitre);
            tvUtilisateur = (TextView) v.findViewById(R.id.txtTaskUtilisateur);
            tvCard = (CardView) v.findViewById(R.id.tacheCard);
            this.setIsRecyclable(false);
        }
    }
}
