package com.project.aplikasiwisata;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.project.aplikasiwisata.database.DatabaseContract;
import com.project.aplikasiwisata.database.DatabaseHelper;
import com.project.aplikasiwisata.model.Comment;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {
    private List<Comment> commentList;
    private LayoutInflater inflater;

    public CommentAdapter(@NonNull Context context, int resource, @NonNull List<Comment> objects) {
        super(context, resource, objects);
        this.commentList = objects;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_comment, parent, false);
            holder = new ViewHolder();
            holder.imageViewFotoUser = convertView.findViewById(R.id.image_foto_user);
            holder.textViewNamaUser = convertView.findViewById(R.id.text_nama_user);
            holder.textViewIsi = convertView.findViewById(R.id.text_isi);
            holder.textViewRating = convertView.findViewById(R.id.text_rating);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Comment comment = commentList.get(position);
        holder.textViewNamaUser.setText(getNamaUser(comment.getUserId()));
        holder.textViewIsi.setText(comment.getContent());
        holder.textViewRating.setText("Rating: " + comment.getRating());
        holder.imageViewFotoUser.setImageResource(getUserPhoto(comment.getUserId()));



        return convertView;
    }

    private String getNamaUser(int userId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String namaUser = "";

        Cursor cursor = null;
        try {
            String[] projection = {DatabaseContract.User.COLUMN_NAME};
            String selection = DatabaseContract.User.COLUMN_ID + " = ?";
            String[] selectionArgs = {String.valueOf(userId)};

            cursor = db.query(
                    DatabaseContract.User.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                namaUser = cursor.getString(cursor.getColumnIndex(DatabaseContract.User.COLUMN_NAME));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return namaUser;
    }

    private int getUserPhoto(int userId) {
        return R.drawable.default_user_image;
    }

    private class ViewHolder {
        ImageView imageViewFotoUser;
        TextView textViewNamaUser;
        TextView textViewIsi;
        TextView textViewRating;
    }
}
