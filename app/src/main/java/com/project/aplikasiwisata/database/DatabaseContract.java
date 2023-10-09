package com.project.aplikasiwisata.database;

public final class DatabaseContract {
    private DatabaseContract() {}

    public static class User {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_ID = "id_user";
        public static final String COLUMN_EMAIL = "email_user";
        public static final String COLUMN_PASSWORD = "password_user";
        public static final String COLUMN_NAME = "nama_user";
        public static final String COLUMN_PHOTO = "foto_user";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_PASSWORD + " TEXT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_PHOTO + " TEXT" +
                ")";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class Wisata {
        public static final String TABLE_NAME = "wisata";
        public static final String COLUMN_ID = "id_wisata";
        public static final String COLUMN_NAME = "nama_wisata";
        public static final String COLUMN_DESCRIPTION = "deskripsi_wisata";
        public static final String COLUMN_IMAGE = "gambar_wisata";
        public static final String COLUMN_LOCATION = "lokasi_wisata";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_IMAGE + " TEXT," +
                COLUMN_LOCATION + " TEXT" +
                ")";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class Comment {
        public static final String TABLE_NAME = "comment";
        public static final String COLUMN_ID = "id_comment";
        public static final String COLUMN_USER_ID = "id_user";
        public static final String COLUMN_WISATA_ID = "id_wisata";
        public static final String COLUMN_CONTENT = "isi_comment";
        public static final String COLUMN_RATING = "rating_comment";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USER_ID + " INTEGER," +
                COLUMN_WISATA_ID + " INTEGER," +
                COLUMN_CONTENT + " TEXT," +
                COLUMN_RATING + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " +
                User.TABLE_NAME + "(" + User.COLUMN_ID + ")," +
                "FOREIGN KEY (" + COLUMN_WISATA_ID + ") REFERENCES " +
                Wisata.TABLE_NAME + "(" + Wisata.COLUMN_ID + ")" +
                ")";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
