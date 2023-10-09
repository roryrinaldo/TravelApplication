package com.project.aplikasiwisata.datautil;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.project.aplikasiwisata.database.DatabaseContract;

public class DefaultDataUtil {

    public static void insertDefaultData(SQLiteDatabase db) {
        insertDefaultUsers(db);
        insertDefaultWisata(db);
        insertDefaultComments(db);
    }

    private static void insertDefaultUsers(SQLiteDatabase db) {
        ContentValues values1 = new ContentValues();
        values1.put(DatabaseContract.User.COLUMN_EMAIL, "user1@example.com");
        values1.put(DatabaseContract.User.COLUMN_PASSWORD, "password1");
        values1.put(DatabaseContract.User.COLUMN_NAME, "User 1");
        values1.put(DatabaseContract.User.COLUMN_PHOTO, "user1.jpg");
        db.insert(DatabaseContract.User.TABLE_NAME, null, values1);

        ContentValues values2 = new ContentValues();
        values2.put(DatabaseContract.User.COLUMN_EMAIL, "user2@example.com");
        values2.put(DatabaseContract.User.COLUMN_PASSWORD, "password2");
        values2.put(DatabaseContract.User.COLUMN_NAME, "User 2");
        values2.put(DatabaseContract.User.COLUMN_PHOTO, "user2.jpg");
        db.insert(DatabaseContract.User.TABLE_NAME, null, values2);



    }

    private static void insertDefaultWisata(SQLiteDatabase db) {
        ContentValues values1 = new ContentValues();
        values1.put(DatabaseContract.Wisata.COLUMN_NAME, "Wisata 1");
        values1.put(DatabaseContract.Wisata.COLUMN_DESCRIPTION, "Deskripsi Wisata 1");
        values1.put(DatabaseContract.Wisata.COLUMN_IMAGE, "wisata1.jpg");
        values1.put(DatabaseContract.Wisata.COLUMN_LOCATION, "0.5183888280767167, 101.45768879460802");
        db.insert(DatabaseContract.Wisata.TABLE_NAME, null, values1);

        ContentValues values2 = new ContentValues();
        values2.put(DatabaseContract.Wisata.COLUMN_NAME, "Wisata 2");
        values2.put(DatabaseContract.Wisata.COLUMN_DESCRIPTION, "Deskripsi Wisata 2");
        values2.put(DatabaseContract.Wisata.COLUMN_IMAGE, "wisata2.jpg");
        values2.put(DatabaseContract.Wisata.COLUMN_LOCATION, "0.5183888280767167, 101.45768879460802");
        db.insert(DatabaseContract.Wisata.TABLE_NAME, null, values2);
    }

    private static void insertDefaultComments(SQLiteDatabase db) {
        ContentValues values1 = new ContentValues();
        values1.put(DatabaseContract.Comment.COLUMN_USER_ID, 1);
        values1.put(DatabaseContract.Comment.COLUMN_WISATA_ID, 1);
        values1.put(DatabaseContract.Comment.COLUMN_CONTENT, "Komentar 1");
        values1.put(DatabaseContract.Comment.COLUMN_RATING, 4);
        db.insert(DatabaseContract.Comment.TABLE_NAME, null, values1);

        ContentValues values2 = new ContentValues();
        values2.put(DatabaseContract.Comment.COLUMN_USER_ID, 2);
        values2.put(DatabaseContract.Comment.COLUMN_WISATA_ID, 1);
        values2.put(DatabaseContract.Comment.COLUMN_CONTENT, "Komentar 2");
        values2.put(DatabaseContract.Comment.COLUMN_RATING, 5);
        db.insert(DatabaseContract.Comment.TABLE_NAME, null, values2);
    }
}
