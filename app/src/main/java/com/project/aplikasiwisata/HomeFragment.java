package com.project.aplikasiwisata;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.aplikasiwisata.database.DatabaseContract;
import com.project.aplikasiwisata.database.DatabaseHelper;
import com.project.aplikasiwisata.model.Wisata;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private ListView listViewWisata;
    private List<Wisata> wisataList;

    public HomeFragment() {
        // Diperlukan konstruktor kosong
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inisialisasi daftar wisata
        wisataList = getWisataListFromDatabase();

        // Inisialisasi ListView
        listViewWisata = view.findViewById(R.id.list_wisata);
        WisataAdapter adapter = new WisataAdapter(requireContext(), R.layout.list_item_wisata, wisataList);
        listViewWisata.setAdapter(adapter);

        // Menangani klik pada item ListView
        listViewWisata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Mendapatkan wisata yang diklik
                Wisata clickedWisata = wisataList.get(position);

                // Menuju ke halaman detail wisata
                navigateToDetailWisata(clickedWisata);
            }
        });

        return view;
    }

    private List<Wisata> getWisataListFromDatabase() {
        // Memperoleh daftar wisata dari DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Wisata> wisataList = new ArrayList<>();

        Cursor cursor = db.query(
                DatabaseContract.Wisata.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Wisata.COLUMN_ID));
            String nama = cursor.getString(cursor.getColumnIndex(DatabaseContract.Wisata.COLUMN_NAME));
            String deskripsi = cursor.getString(cursor.getColumnIndex(DatabaseContract.Wisata.COLUMN_DESCRIPTION));
            String gambar = cursor.getString(cursor.getColumnIndex(DatabaseContract.Wisata.COLUMN_IMAGE));
            String lokasi = cursor.getString(cursor.getColumnIndex(DatabaseContract.Wisata.COLUMN_LOCATION));

            Wisata wisata = new Wisata(id, nama, deskripsi, gambar, lokasi);
            wisataList.add(wisata);
        }

        cursor.close();
        db.close();

        return wisataList;
    }

    private void navigateToDetailWisata(Wisata wisata) {
        // Buat bundle untuk menyimpan data wisata yang dikirim ke halaman detail
        Bundle bundle = new Bundle();
        bundle.putInt("id_wisata", wisata.getId());
        bundle.putString("nama", wisata.getName());
        bundle.putString("deskripsi", wisata.getDescription());
        bundle.putString("gambar", wisata.getImage());
        bundle.putString("lokasi", wisata.getLocation());

        // Buat instance halaman detail wisata dan set bundle
        DetailWisataFragment detailWisataFragment = new DetailWisataFragment();
        detailWisataFragment.setArguments(bundle);

        // Ganti halaman fragment dengan halaman detail wisata
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailWisataFragment)
                .addToBackStack(null)
                .commit();
    }


}
