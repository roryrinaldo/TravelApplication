package com.project.aplikasiwisata;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.aplikasiwisata.database.DatabaseHelper;
import com.project.aplikasiwisata.model.Comment;
import com.project.aplikasiwisata.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private SessionManager sessionManager;
    private List<Comment> commentList;
    private HistoriAdapter historiAdapter;
    private DatabaseHelper databaseHelper;
    public ProfileFragment() {
        // Diperlukan konstruktor kosong
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Lakukan inisialisasi tampilan
        ImageView imageViewUser = view.findViewById(R.id.image_user);
        TextView textViewEmail = view.findViewById(R.id.text_email);
        Button buttonUpdateProfile = view.findViewById(R.id.button_update_profile);
        Button buttonLogout = view.findViewById(R.id.button_logout);

        ListView listViewHistori = view.findViewById(R.id.list_histori_comment);

        databaseHelper = new DatabaseHelper(requireContext());

        // Mendapatkan data pengguna dari sesi
        sessionManager = new SessionManager(requireContext());

        String email = sessionManager.getEmail();

        // Menampilkan data pengguna
        textViewEmail.setText("Email: " + email);
        imageViewUser.setImageResource(R.drawable.default_user_image);

        // Menambahkan tindakan pada tombol "Update Data Pengguna"
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke halaman update data pengguna
                navigateToUpdateProfile();
            }
        });

        // Menambahkan tindakan pada tombol "Logout"
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout pengguna
                logoutUser();
            }
        });

        // Cek apakah pengguna telah login
        if (!sessionManager.isLoggedIn()) {
            buttonLogout.setVisibility(View.GONE);
            buttonUpdateProfile.setVisibility(View.GONE);
            textViewEmail.setVisibility(view.GONE);

            Button buttonLogin = view.findViewById(R.id.button_login);
            buttonLogin.setVisibility(View.VISIBLE);

            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToLogin();
                }
            });
        }

        commentList = new ArrayList<>();

        // Inisialisasi adapter histori komentar
        historiAdapter = new HistoriAdapter(requireContext(), commentList);

        // Set adapter pada ListView
        listViewHistori.setAdapter(historiAdapter);

        // Load histori komentar
        loadHistoriComments();


        return view;
    }

    private void navigateToUpdateProfile() {
        // Implementasi navigasi ke halaman update data pengguna di sini
        Intent intent = new Intent(requireContext(), UpdateProfileActivity.class);
        startActivity(intent);
    }

    private void navigateToLogin() {
        // Implementasi navigasi ke halaman update data pengguna di sini
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void logoutUser() {
        // Logout pengguna dengan menghapus data sesi dan kembali ke halaman login
        sessionManager.logout();
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void loadHistoriComments() {
        // Bersihkan commentList sebelum memuat histori komentar
        commentList.clear();

        // Buka database untuk membaca histori komentar
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        // Query untuk mengambil histori komentar dari tabel komentar berdasarkan id_user
        String query = "SELECT * FROM comment WHERE id_user = ?";
        String[] selectionArgs = {String.valueOf(sessionManager.getUserId())};

        // Eksekusi query
        Cursor cursor = database.rawQuery(query, selectionArgs);

        // Iterasi cursor untuk mengambil data histori komentar
        if (cursor.moveToFirst()) {
            do {
                // Ambil data komentar dari cursor
                int commentId = cursor.getInt(cursor.getColumnIndex("id_comment"));
                int userId = cursor.getInt(cursor.getColumnIndex("id_user"));
                int placeId = cursor.getInt(cursor.getColumnIndex("id_wisata"));
                String commentText = cursor.getString(cursor.getColumnIndex("isi_comment"));
                int rating = cursor.getInt(cursor.getColumnIndex("rating_comment"));

                // Buat objek Comment
                Comment comment = new Comment(commentId, userId, placeId, commentText, rating);

                // Tambahkan komentar ke commentList
                commentList.add(comment);
            } while (cursor.moveToNext());
        }

        // Tutup cursor dan database
        cursor.close();
        database.close();

        // Panggil notifyDataSetChanged() pada adapter
        historiAdapter.notifyDataSetChanged();
    }


}
