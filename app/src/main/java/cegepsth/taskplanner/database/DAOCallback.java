package cegepsth.taskplanner.database;

import org.json.JSONObject;

/**
 * Created by vincent on 2016-02-15.
 */
public abstract class DAOCallback implements Runnable{

    private Object response;

    public void setResponse(Object r)
    {
        response = r;
    }

    public Object getResponse()
    {
        return response;
    }

}
