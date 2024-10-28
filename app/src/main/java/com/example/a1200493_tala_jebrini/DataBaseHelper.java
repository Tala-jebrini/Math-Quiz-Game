package com.example.a1200493_tala_jebrini;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends android.database.sqlite.SQLiteOpenHelper {

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE User(ID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Score INTEGER)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", user.getUsername());
        contentValues.put("Score", user.getUserScore());

        // Log the values being inserted
        Log.d("DB_INSERT", "Inserting user: Name=" + user.getUsername() + ", Score=" + user.getUserScore());

        long result = sqLiteDatabase.insert("User", null, contentValues);
        if (result == -1) {
            Log.d("DB_ERROR", "Error inserting data");
        } else {
            Log.d("DB_SUCCESS", "Data inserted successfully");
        }
        sqLiteDatabase.close();
    }


    public boolean userExists(String username) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM User WHERE Name = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{username});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void updateUserScore(String username, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Score", score);

        // Update the row where the Name (username) matches
        db.update("User", values, "Name = ?", new String[]{username});
        db.close();
    }

    public Cursor getTop5Users() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        // Query to select top 5 users ordered by Score in descending order
        return sqLiteDatabase.rawQuery("SELECT Name, Score FROM User ORDER BY Score DESC LIMIT 5", null);
    }


    public int getTotalNumberOfPlayers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM User", null);
        int count = 0;

        // Check if cursor is not null and move to the first result
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0); // Get the count from the i column
            }
            cursor.close();
        }

        return count;
    }


    public Cursor getScoresByUsername(String username) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        // Use a parameterized query to prevent SQL injection
        return sqLiteDatabase.rawQuery("SELECT * FROM User WHERE Name = ?", new String[]{username});
    }


    public double getAverageScore() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT AVG(Score) FROM User", null);
        double averageScore = 0;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                averageScore = cursor.getDouble(0); // Get the average score
            }
            cursor.close();
        }

        return averageScore;
    }

    public Cursor getHighestScore() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        // Select ID, Name, and the maximum Score from the User table
        return sqLiteDatabase.rawQuery("SELECT ID, Name, Score FROM User ORDER BY Score DESC LIMIT 1", null);
    }


}