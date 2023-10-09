package com.project.aplikasiwisata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.project.aplikasiwisata.model.Wisata;

import java.util.List;

public class WisataAdapter extends ArrayAdapter<Wisata> {
    private List<Wisata> wisataList;
    private LayoutInflater inflater;

    public WisataAdapter(@NonNull Context context, int resource, @NonNull List<Wisata> objects) {
        super(context, resource, objects);
        this.wisataList = objects;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_wisata, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.image_wisata);
            holder.textViewName = convertView.findViewById(R.id.text_nama);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Wisata wisata = wisataList.get(position);
        holder.imageView.setImageResource(getImageResourceId(wisata.getImage()));
        holder.textViewName.setText(wisata.getName());

        return convertView;
    }

    private int getImageResourceId(String imageName) {
        // Mendapatkan resource ID gambar berdasarkan nama gambar
        return R.drawable.default_image;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textViewName;
    }
}
