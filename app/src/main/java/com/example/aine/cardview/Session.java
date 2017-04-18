package com.example.aine.cardview;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Aaron Nicholl on 06/04/2017.
 */

public class Session
{
    SharedPreferences prefs; // Shared Preferences
    SharedPreferences.Editor editor; // Shared Preferences editor
    Context ctx; // Context

    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedin(boolean logggedin){ // Logging in process
        editor.putBoolean("loggedInmode",logggedin);
        editor.commit();
    }

    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }
}
