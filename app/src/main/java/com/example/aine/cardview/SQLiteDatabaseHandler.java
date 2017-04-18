package com.example.aine.cardview;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TvShowFavouritesManager";

    // TvShow table name
    private static final String TABLE_FAVOURITES = "TvShows";

    // TvShow Table Columns names
    private static final String KEY_ID = "ID";
    private static final String KEY_TITLE = "Title";
    private static final String KEY_POPULARITY = "Popularity";
    private static final String KEY_OVERVIEW  = "Overview";
    private static final String KEY_POSTERPATH = "PosterPath";
    private static final String KEY_BACKDROPPATH = "BackdropPath";

    //User table Column names
    public static final String USER_TABLE = "users"; // user table
    public static final String COLUMN_ID = "_id"; // column id
    public static final String COLUMN_EMAIL = "email"; // email
    public static final String COLUMN_PASS = "password"; // password

    public static final String CREATE_TABLE_USERS = "CREATE TABLE " + USER_TABLE + "(" // structure of my table
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," // columnd id will have a primary key autoincrement
            + COLUMN_EMAIL + " TEXT," // email will be text
            + COLUMN_PASS + " TEXT);"; // password will be text


    private static SQLiteDatabaseHandler mInstance = null;

    public static SQLiteDatabaseHandler getInstance(Context activityContext) {

        // Get the application context from the activityContext to prevent leak
        if (mInstance == null) {
            mInstance = new SQLiteDatabaseHandler (activityContext.getApplicationContext());
        }
        return mInstance;
    }


    /**
     * Private, use the static method "SQLiteDatabaseHandler.getInstance(Context)" instead.
     */
    private SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_FAVOURITES = "CREATE TABLE " + TABLE_FAVOURITES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_POPULARITY + " REAL,"
                + KEY_OVERVIEW + " TEXT,"
                + KEY_POSTERPATH + " TEXT,"
                + KEY_BACKDROPPATH + " TEXT" +")";
        db.execSQL(CREATE_TABLE_FAVOURITES);
        db.execSQL(CREATE_TABLE_USERS);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String email, String password) { // adding the values in the user table
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email); // put email under the Column Email
        values.put(COLUMN_PASS, password);// put password under the Column Password

        long id = db.insert(USER_TABLE, null, values);
        db.close();

        //Log.d(TAG1, "user inserted" + id); // tag to ensure that the data has been inputted into the database
    }

    public boolean getUser(String email, String pass){
        //get the user from the database
        //select query which selects all from user where the email is = to input and the password is = to the input
        String selectQuery = "select * from  " + USER_TABLE + " where " +
                COLUMN_EMAIL + " = " + "'"+email+"'" + " and " + COLUMN_PASS + " = " + "'"+pass+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            return true;
        }
        cursor.close();
        db.close();

        return false;
    }




    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    // Adding new tvShowFavourites
    void addTvShowFavourites(TvShow tvShowFavourites) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, tvShowFavourites.getId()); // TvShow ID
        values.put(KEY_TITLE, tvShowFavourites.getName()); // TvShow TITLE
        values.put(KEY_POPULARITY, tvShowFavourites.getPopularity()); // TvShow POPULARITY
        values.put(KEY_OVERVIEW, tvShowFavourites.getOverview()); // TvShow OVERVIEW
        values.put(KEY_POSTERPATH, tvShowFavourites.getPosterPath()); // TvShow POSTERPATH
        values.put(KEY_BACKDROPPATH, tvShowFavourites.getBackdropPath()); // TvShow BACKDROPPATH


        // Inserting Row
        db.insert(TABLE_FAVOURITES, null, values);
        db.close(); // Closing database connection

    }

    // Getting single tvShowFavourites
    TvShow getTvShowFavourites(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVOURITES, new String[] { KEY_ID,
                        KEY_TITLE, KEY_POPULARITY,KEY_OVERVIEW, KEY_POSTERPATH, KEY_BACKDROPPATH }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TvShow tvShowFavourites = new TvShow(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Double.parseDouble(cursor.getString(2)),cursor.getString(3),cursor.getString(4)
        ,cursor.getString(5));
        // return tvShowFavourites
        return tvShowFavourites;
    }

    // Getting All TvShowFavouritess
    public List<TvShow> getAllTvShowFavourites() {
        List<TvShow> tvShowFavouritesList = new ArrayList<TvShow>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TvShow tvShowFavourites = new TvShow();
                tvShowFavourites.setId(Integer.parseInt(cursor.getString(0)));
                tvShowFavourites.setName(cursor.getString(1));
                Log.d( "DEBUG","getAllTvShowFavourites: Title= " + tvShowFavourites.getName());
                tvShowFavourites.setPopularity (Double.parseDouble(cursor.getString(2)));
                Log.d( "DEBUG","getAllTvShowFavourites: Popularity=" + tvShowFavourites.getPopularity());
                tvShowFavourites.setOverview (cursor.getString(3));
                Log.d( "DEBUG","getAllTvShowFavourites: Overview=" + tvShowFavourites.getOverview());
                tvShowFavourites.setPosterPath(cursor.getString(4));
                Log.d( "DEBUG","getAllTvShowFavourites: PosterPath= " + tvShowFavourites.getPosterPath());
                tvShowFavourites.setBackdropPath(cursor.getString(5));
                Log.d( "DEBUG","getAllTvShowFavourites: BackdropPath= " + tvShowFavourites.getPosterPath());
                // Adding tvShowFavourites to list
                tvShowFavouritesList.add(tvShowFavourites);
            } while (cursor.moveToNext());
        }

        // return tvShowFavourites list
        return tvShowFavouritesList;
    }

    // Updating single tvShowFavourites
    public int updateTvShowFavourites(TvShow tvShowFavourites) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, tvShowFavourites.getName()); // TvShow TITLE
        values.put(KEY_POPULARITY, tvShowFavourites.getPopularity()); // TvShowPOPULARITY
        values.put(KEY_OVERVIEW, tvShowFavourites.getOverview()); // TvShow OVERVIEW
        values.put(KEY_POSTERPATH, tvShowFavourites.getPosterPath()); // TvShow POSTERPATH
        values.put(KEY_BACKDROPPATH, tvShowFavourites.getBackdropPath()); // TvShow BACKDROPATH
        // updating row
        return db.update(TABLE_FAVOURITES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(tvShowFavourites.getId()) });
    }

    // Deleting single tvShowFavourites
    public void deleteTvShowFavourites(TvShow tvShowFavourites) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVOURITES, KEY_ID + " = ?",
                new String[] { String.valueOf(tvShowFavourites.getId()) });
        db.close();
    }


    // Getting tvShowFavouritess Count
    public int getTvShowFavouritessCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FAVOURITES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }    

}
