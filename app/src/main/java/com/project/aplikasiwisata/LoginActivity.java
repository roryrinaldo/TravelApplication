package com.project.aplikasiwisata;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.aplikasiwisata.database.DatabaseHelper;
import com.project.aplikasiwisata.model.User;
import com.project.aplikasiwisata.session.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    private SessionManager sessionManager;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        dbHelper = new DatabaseHelper(this);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (isValidCredentials(email, password)) {
                    navigateToMain();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the registration activity
                navigateToRegistration();
            }
        });
    }

    private boolean isValidCredentials(String email, String password) {
        // Implement your own logic to validate the credentials against a database
        // Return true if the credentials are valid, false otherwise

        // Query the database to check if the email and password match
        if (dbHelper.checkUserCredentials(email, password)) {
            // Get the user data from the database
            User user = dbHelper.getUserByEmail(email);

            // Save the user data to the session
            sessionManager.isLoggedIn();
            sessionManager.setUserId(user.getId());
            sessionManager.setEmail(user.getEmail());

            return true;
        } else {
            return false;
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToRegistration() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}

