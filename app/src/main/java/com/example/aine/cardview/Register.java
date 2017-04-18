package com.example.aine.cardview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity implements View.OnClickListener{
    private Button reg; // declaration
    private TextView tvLogin;
    private EditText etEmail, etPass;
    private SQLiteDatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // layout of screen

        db = SQLiteDatabaseHandler.getInstance(getApplicationContext()); // connecting DbHelper to the Register
        reg = (Button)findViewById(R.id.LogInBtnRegister); // Identifying all my components
        tvLogin = (TextView)findViewById(R.id.HomeTextView);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPass = (EditText)findViewById(R.id.etPass);
        reg.setOnClickListener(this); // Setting up Click Listeners
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){ // switch statement for clickingg buttons
            case R.id.LogInBtnRegister: // Login Button statement
                register(); // runs the register method
                break; // end of statement
            case R.id.HomeTextView:// Home Text View statement
                startActivity(new Intent(Register.this,Login.class));// intent to open the login page
                finish(); // closes the register page
                break;// end of statement
            default:

        }
    }

    private void register(){ // register method
        String email = etEmail.getText().toString(); // obtain email from text field
        String pass = etPass.getText().toString(); // obtain password from text field
        if (etEmail.length() == 0) // ensuring that the field is not empty
        {
            etEmail.requestFocus();
            etEmail.setError("FIELD CANNOT BE EMPTY"); // pop up message on text field
        } else if (etPass.length() == 0) // ensuring that the field is not empty
        {
            etPass.requestFocus();
            etPass.setError("FIELD CANNOT BE EMPTY");//pop up message on text field
        }
        if(email.isEmpty() && pass.isEmpty()){
            displayToast("Username/password field empty");
        }else{
            db.addUser(email,pass); // toast message identifying that the insertion of data has been completed
            displayToast("User registered");
            startActivity(new Intent(Register.this,MainActivity.class));
        }
    }

    private void displayToast(String message){ // method to create toast messages
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
