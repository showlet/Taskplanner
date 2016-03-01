package cegepsth.taskplanner.model;

import android.graphics.Color;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by vincent on 2016-02-11.
 */
public class Task extends AbstractDBModel {
    public int Id;
    public int UserId;
    public int ProjectId;
    public String Name;
    public String Description;
    public Boolean IsClosed;
    public Date Deadline;
    public Date Created;
    public Date Closed;
    public Integer Color;
}
