package com.example.aine.cardview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        //Toast.makeText(context, "in dummy MainActivity", Toast.LENGTH_SHORT).show();
        //The following code should is used to call into the popular Tv shows screens
        // It is just a dummy MainActivity with no associated layout.
        // The following Intent call with the context changed (to suit your MainActivity) should be placed in
        // the real MainActivity
        session = new Session(this); // checks to see if the user has been logged in
        if(!session.loggedin()){ // if the session is not logged in
            logout(); // log out method run
        }
        else{
            Intent intent = new Intent(context, PopularActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            context.startActivity(intent);
        }

    }

    private void logout() // logout method
    {
        session.setLoggedin(false); // if the loggedin is not logged in
        finish(); // end the Main Activity
        startActivity(new Intent(MainActivity.this,Login.class)); // open up the Login page
    }
}
