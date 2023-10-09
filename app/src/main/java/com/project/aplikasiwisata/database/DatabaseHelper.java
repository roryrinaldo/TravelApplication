package com.project.aplikasiwisata.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.aplikasiwisata.datautil.DefaultDataUtil;
import com.project.aplikasiwisata.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "aplikasi_wisata.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.User.CREATE_TABLE);
        db.execSQL(DatabaseContract.Wisata.CREATE_TABLE);
        db.execSQL(DatabaseContract.Comment.CREATE_TABLE);

        // Menambahkan data default
        DefaultDataUtil.insertDefaultData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.Comment.DELETE_TABLE);
        db.execSQL(DatabaseContract.Wisata.DELETE_TABLE);
        db.execSQL(DatabaseContract.User.DELETE_TABLE);
        onCreate(db);
    }

    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {DatabaseContract.User.COLUMN_ID};
        String selection = DatabaseContract.User.COLUMN_EMAIL + " = ? AND " +
                DatabaseContract.User.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(DatabaseContract.User.TABLE_NAME, columns, selection,
                selectionArgs, null, null, null);
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                DatabaseContract.User.COLUMN_ID,
                DatabaseContract.User.COLUMN_EMAIL,
                DatabaseContract.User.COLUMN_PASSWORD,
                DatabaseContract.User.COLUMN_NAME,
                DatabaseContract.User.COLUMN_PHOTO
        };
        String selection = DatabaseContract.User.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };
        Cursor cursor = db.query(DatabaseContract.User.TABLE_NAME, columns, selection,
                selectionArgs, null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex(DatabaseContract.User.COLUMN_ID));
            String userEmail = cursor.getString(cursor.getColumnIndex(DatabaseContract.User.COLUMN_EMAIL));
            String userPassword = cursor.getString(cursor.getColumnIndex(DatabaseContract.User.COLUMN_PASSWORD));
            String userNama = cursor.getString(cursor.getColumnIndex(DatabaseContract.User.COLUMN_NAME));
            String userPhoto = cursor.getString(cursor.getColumnIndex(DatabaseContract.User.COLUMN_PHOTO));
            user = new User(userId, userEmail, userPassword,userNama,userPhoto);
        }
        cursor.close();
        db.close();
        return user;
    }


}