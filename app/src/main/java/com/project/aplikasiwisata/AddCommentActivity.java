package com.project.aplikasiwisata;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.aplikasiwisata.database.DatabaseContract;
import com.project.aplikasiwisata.database.DatabaseHelper;
import com.project.aplikasiwisata.session.SessionManager;

public class AddCommentActivity extends AppCompatActivity {
    private EditText editTextComment;
    private Button buttonSubmit;
    private SessionManager sessionManager;
    private int idWisata;
    private RadioGroup radioGroupRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        editTextComment = findViewById(R.id.edit_comment);
        radioGroupRating = findViewById(R.id.radio_group_rating);
        buttonSubmit = findViewById(R.id.button_submit);

        sessionManager = new SessionManager(this);

        // Mendapatkan nilai idWisata dari halaman sebelumnya
        idWisata = getIntent().getIntExtra("id_wisata", 0);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button_submit) {
                    if (sessionManager.isLoggedIn()) {
                        String comment = editTextComment.getText().toString().trim();
                        int selectedRatingId = radioGroupRating.getCheckedRadioButtonId();
                        int rating = -1; // Inisialisasi nilai rating dengan -1 (tidak ada rating yang dipilih)

                        if (selectedRatingId != -1) {
                            RadioButton selectedRatingButton = findViewById(selectedRatingId);
                            String ratingText = selectedRatingButton.getText().toString();
                            rating = Integer.parseInt(ratingText);
                        }
                        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

                        // Get a writable database instance
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        // Validate the comment
                        if (comment.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please enter your comment", Toast.LENGTH_SHORT).show();
                        } else {
                            // Save the comment to the database or perform any other necessary actions
                            int userId = sessionManager.getUserId(); // Mendapatkan userId dari SessionManager
                            saveCommentToDatabase(db, userId, idWisata, comment, rating); // Pass userId and idWisata to the method


                            // Return to the previous page
                            finish();
                            recreate();
                        }
                    } else {
                        // User is not logged in, navigate to the login activity
                        navigateToLogin();
                    }
                }
            }
        });
    }

    // Metode saveCommentToDatabase yang disesuaikan
    public void saveCommentToDatabase(SQLiteDatabase db, int userId, int wisataId, String comment, int rating) {
        // Perubahan: Menghilangkan parameter rating karena rating akan diperoleh dari input pengguna
        ContentValues values = new ContentValues();
        values.put("id_user", userId);
        values.put("id_wisata", wisataId);
        values.put("isi_comment", comment);
        values.put("rating_comment", rating);
        // Tambahkan logika lain yang diperlukan untuk menyimpan komentar ke dalam database
        db.insert("comment", null, values);

        Log.d("AddCommentActivity", "Rating: " + rating); // Cek nilai rating di logcat
        Toast.makeText(getApplicationContext(), "Komentar berhasil ditambahkan", Toast.LENGTH_SHORT).show();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(AddCommentActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}