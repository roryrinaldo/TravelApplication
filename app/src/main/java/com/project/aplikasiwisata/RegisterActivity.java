package com.project.aplikasiwisata;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.aplikasiwisata.database.DatabaseContract;
import com.project.aplikasiwisata.database.DatabaseHelper;
import com.project.aplikasiwisata.session.SessionManager;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        sessionManager = new SessionManager(this);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (registerUser(name, email, password)) {
                    sessionManager.isLoggedIn();
                    sessionManager.setEmail(email);
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean registerUser(String name, String email, String password) {
        // Get an instance of the DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.User.COLUMN_NAME, name);
        values.put(DatabaseContract.User.COLUMN_EMAIL, email);
        values.put(DatabaseContract.User.COLUMN_PASSWORD, password);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DatabaseContract.User.TABLE_NAME, null, values);

        // Close the database
        db.close();

        // Return true if the registration was successful, false otherwise
        return newRowId != -1;
    }

    private void navigateToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
