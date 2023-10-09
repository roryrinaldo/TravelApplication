package com.project.aplikasiwisata;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonUpdateProfile;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        // Inisialisasi tampilan
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonUpdateProfile = findViewById(R.id.buttonUpdateProfile);

        // Mendapatkan data pengguna yang akan diperbarui dari SharedPreferences
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String email = sharedPreferences.getString("email", "");

        // Menampilkan data pengguna yang akan diperbarui ke dalam EditText
        editTextName.setText(name);
        editTextEmail.setText(email);

        // Menambahkan tindakan pada tombol "Update Profile"
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengambil data yang diinputkan oleh pengguna
                String updatedName = editTextName.getText().toString().trim();
                String updatedEmail = editTextEmail.getText().toString().trim();
                String updatedPassword = editTextPassword.getText().toString().trim();

                // Mendapatkan data yang belum diubah dari SharedPreferences
                String originalName = sharedPreferences.getString("name", "");
                String originalEmail = sharedPreferences.getString("email", "");

                // Menyimpan data yang belum diubah jika data yang diinputkan kosong
                if (updatedName.isEmpty()) {
                    updatedName = originalName;
                }
                if (updatedEmail.isEmpty()) {
                    updatedEmail = originalEmail;
                }

                // Lakukan proses update profil
                updateProfile(updatedName, updatedEmail, updatedPassword);
            }
        });
    }

    private void updateProfile(String name, String email, String password) {
        // Simpan data pengguna yang diperbarui ke dalam SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.apply();

        // Tampilkan pesan sukses
        Toast.makeText(UpdateProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

        // Selesai, kembali ke halaman profil
        Intent intent = new Intent(UpdateProfileActivity.this, ProfileFragment.class);
        startActivity(intent);
        finish();
    }
}
