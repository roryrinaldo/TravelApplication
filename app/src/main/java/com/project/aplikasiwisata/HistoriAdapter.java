package com.project.aplikasiwisata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.project.aplikasiwisata.model.Comment;

import java.util.List;

public class HistoriAdapter extends ArrayAdapter<Comment> {
    private List<Comment> commentList;
    private LayoutInflater inflater;

    public HistoriAdapter(@NonNull Context context, @NonNull List<Comment> objects) {
        super(context, 0, objects);
        this.commentList = objects;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_histori, parent, false);
            holder = new ViewHolder();
            holder.textViewComment = convertView.findViewById(R.id.text_comment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Comment comment = commentList.get(position);
        holder.textViewComment.setText(comment.getContent());

        return convertView;
    }

    private static class ViewHolder {
        TextView textViewComment;
    }
}

