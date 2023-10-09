package com.project.aplikasiwisata;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.aplikasiwisata.database.DatabaseContract;
import com.project.aplikasiwisata.database.DatabaseHelper;
import com.project.aplikasiwisata.model.Comment;
import com.project.aplikasiwisata.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class DetailWisataFragment extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap googleMap;
    private double latitude;
    private double longitude;
    private TextView textViewNama, textViewDeskripsi;
    private ListView listViewComment;
    private List<Comment> commentList;
    private int idWisata;
    private SessionManager sessionManager;

    public DetailWisataFragment() {
        // Diperlukan konstruktor kosong
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_wisata, container, false);

        // Inisialisasi tampilan
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        textViewNama = view.findViewById(R.id.text_nama);
        textViewDeskripsi = view.findViewById(R.id.text_deskripsi);

        // Mendapatkan data wisata dari bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            idWisata = bundle.getInt("id_wisata");
            String nama = bundle.getString("nama");
            String deskripsi = bundle.getString("deskripsi");
            String gambar = bundle.getString("gambar");
            String lokasi = bundle.getString("lokasi");

            // Menampilkan data wisata ke tampilan
            textViewNama.setText(nama);
            textViewDeskripsi.setText(deskripsi);

            // Mendapatkan nilai latitude dan longitude dari lokasi
            String[] latLng = lokasi.split(",");
            if (latLng.length == 2) {
                latitude = Double.parseDouble(latLng[0]);
                longitude = Double.parseDouble(latLng[1]);
            }

            // Menampilkan daftar komentar berdasarkan id_wisata
            listViewComment = view.findViewById(R.id.list_comment);
            commentList = getCommentListFromDatabase(idWisata);
            CommentAdapter adapter = new CommentAdapter(requireContext(), R.layout.list_item_comment, commentList);
            listViewComment.setAdapter(adapter);

            // Menambahkan click listener untuk data komentar
            listViewComment.setOnItemClickListener((parent, view1, position, id) -> {
                Comment selectedComment = commentList.get(position);

                // Memeriksa apakah komentar dimiliki oleh pengguna yang sedang login
                if (selectedComment.getUserId() == sessionManager.getUserId()) {
                    showDeleteDialog(selectedComment);
                } else {
                    showAlertDialog("Anda tidak dapat menghapus komentar ini karena bukan milik Anda.");
                }
            });
        }

        // Mendapatkan instance SessionManager
        sessionManager = new SessionManager(requireContext());

        // Mengatur tindakan tombol tambah komentar
        Button buttonAddComment = view.findViewById(R.id.button_add_comment);
        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()) {
                    if (hasUserCommented(idWisata)) {
                        // Pengguna telah memberikan komentar sebelumnya
                        showAlertDialog("Anda telah memberikan komentar untuk wisata ini.");
                    } else {
                        // Pengguna dapat menambahkan komentar baru
                        navigateToAddComment();
                    }
                } else {
                    // Pengguna belum login, arahkan ke halaman login
                    navigateToLogin();
                }
            }
        });

        return view;

    }

    private int getImageResourceId(String imageName) {
        // Mendapatkan resource ID gambar berdasarkan nama gambar
       return R.drawable.default_image;
    }

    private List<Comment> getCommentListFromDatabase(int wisataId) {
        // Memperoleh daftar komentar dari DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Comment> commentList = new ArrayList<>();

        String selection = DatabaseContract.Comment.COLUMN_WISATA_ID + " = ?";
        String[] selectionArgs = { String.valueOf(wisataId) };

        Cursor cursor = db.query(
                DatabaseContract.Comment.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Comment.COLUMN_ID));
            int userId = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Comment.COLUMN_USER_ID));
            String isi = cursor.getString(cursor.getColumnIndex(DatabaseContract.Comment.COLUMN_CONTENT));
            int rating = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Comment.COLUMN_RATING));

            Comment comment = new Comment(id, userId, wisataId, isi, rating);
            commentList.add(comment);
        }

        cursor.close();
        db.close();

        return commentList;
    }

    private boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    private boolean hasUserCommented(int wisataId) {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DatabaseContract.Comment.COLUMN_USER_ID + " = ? AND " +
                DatabaseContract.Comment.COLUMN_WISATA_ID + " = ?";
        String[] selectionArgs = { String.valueOf(sessionManager.getUserId()), String.valueOf(wisataId) };

        Cursor cursor = db.query(
                DatabaseContract.Comment.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean hasCommented = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return hasCommented;
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing, just dismiss the dialog
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void navigateToAddComment() {
        // Implementasi navigasi ke halaman tambah komentar di sini
        if (isLoggedIn()) {
            if (hasUserCommented(idWisata)) {
                showAlertDialog("Anda telah memberikan komentar untuk wisata ini.");
            } else {
                // Navigasi ke halaman tambah komentar
                Intent intent = new Intent(requireContext(), AddCommentActivity.class);
                intent.putExtra("id_wisata", idWisata);
                startActivity(intent);
            }
        } else {
            showAlertDialog("Anda harus login terlebih dahulu untuk menambahkan komentar.");
            navigateToLogin();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void showDeleteDialog(Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Apakah Anda ingin menghapus komentar ini?")
                .setPositiveButton("Ya", (dialog, id) -> {
                    // Panggil method untuk menghapus komentar dari database
                    deleteComment(comment);
                })
                .setNegativeButton("Tidak", (dialog, id) -> {
                    // Do nothing, just dismiss the dialog
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteComment(Comment comment) {
        int loggedInUserId = sessionManager.getUserId(); // Mendapatkan ID pengguna yang sedang login

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = DatabaseContract.Comment.COLUMN_ID + " = ? AND " +
                DatabaseContract.Comment.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(comment.getId()), String.valueOf(loggedInUserId) };

        int deletedRows = db.delete(DatabaseContract.Comment.TABLE_NAME, selection, selectionArgs);
        if (deletedRows > 0) {
            // Komentar berhasil dihapus
            // Refresh daftar komentar
            commentList.remove(comment);
            CommentAdapter adapter = (CommentAdapter) listViewComment.getAdapter();
            adapter.notifyDataSetChanged();
        } else {
            // Gagal menghapus komentar
            showAlertDialog("Gagal menghapus komentar. Silakan coba lagi.");
        }

        db.close();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Tambahkan marker pada lokasi wisata
        LatLng location = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(location));

        // Posisi kamera pada lokasi wisata
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
