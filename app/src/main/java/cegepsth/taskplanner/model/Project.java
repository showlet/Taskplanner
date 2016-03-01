package cegepsth.taskplanner.model;

import android.graphics.Color;
import android.support.v7.widget.CardView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vincent on 2016-02-11.
 */
public class Project extends AbstractDBModel {
    public int Id;
    public int OwnerId;
    public String Name;
    public String Description;
    public Date Deadline;
    public int Color;

    public ArrayList<Task> lstTask;
}
