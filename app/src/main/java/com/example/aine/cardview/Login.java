package com.example.aine.cardview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener{
    private Button login, register;
    private EditText etEmail, etPass;
    private SQLiteDatabaseHandler db;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = SQLiteDatabaseHandler.getInstance(getApplicationContext());
        session = new Session(this);
        login = (Button)findViewById(R.id.LogInBtn);
        register = (Button)findViewById(R.id.RegisterbtnLogin);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPass = (EditText)findViewById(R.id.etPass);
        login.setOnClickListener(this);
        register.setOnClickListener(this);

        if(session.loggedin()){ // if the session is logged in
            startActivity(new Intent(Login.this,Loading.class)); // open the loading page
            finish(); // close the Login page
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.LogInBtn: // if the login button is clicked
                login(); // run login method
                break;
            case R.id.RegisterbtnLogin: // if the register button is clicked
                startActivity(new Intent(Login.this,Register.class)); // open the register page
                break;
            default:

        }
    }

    private void login() { // login method
        String email = etEmail.getText().toString(); // obtain email from text field
        String pass = etPass.getText().toString();// obtain email from text field

        if (etEmail.length() == 0) // ensuring that the field is not empty
        {
            etEmail.requestFocus();
            etEmail.setError("FIELD CANNOT BE EMPTY");
        } else if (etPass.length() == 0) // ensuring that the field is not empty
        {
            etPass.requestFocus();
            etPass.setError("FIELD CANNOT BE EMPTY");
        }
        if (db.getUser(email, pass)) {
            session.setLoggedin(true); // if the logged is verified
            startActivity(new Intent(Login.this, Loading.class)); // opens the laoding page
            finish();// finish activity
        } else {
            Toast.makeText(getApplicationContext(), "Wrong email/password", Toast.LENGTH_SHORT).show();
            // If the verification cannot be done
        }
    }
}
