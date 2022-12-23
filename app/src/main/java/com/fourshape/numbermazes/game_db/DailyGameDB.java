package com.fourshape.numbermazes.game_db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.fourshape.numbermazes.custom_calender.data_formats.DateData;
import com.fourshape.numbermazes.utils.MakeLog;

import java.util.ArrayList;

public class DailyGameDB extends SQLiteOpenHelper {

    private final static String TAG = "DailyGameDB";
    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "daily_game.db";

    private static final String TABLE_CREATE_SQL = "CREATE TABLE " + DbTables.DAILY_GAMES_TABLE + " "
            + "( " + DbCols.ID + " INTEGER PRIMARY KEY,"
            + DbCols.DAY + " INTEGER,"
            + DbCols.MONTH + " INTEGER,"
            + DbCols.YEAR + " INTEGER,"
            + DbCols.STATUS + " INTEGER,"
            + DbCols.GAME_DATA + " TEXT )";

    public DailyGameDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbTables.DAILY_GAMES_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void updateData (int refId, int day, int month, int year, int status, String gameData) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbCols.DAY, day);
        contentValues.put(DbCols.MONTH, month);
        contentValues.put(DbCols.YEAR, year);
        contentValues.put(DbCols.STATUS, status);
        contentValues.put(DbCols.GAME_DATA, gameData);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(DbTables.DAILY_GAMES_TABLE, contentValues, DbCols.ID + " = ?", new String[]{String.valueOf(refId)});
        db.close();

    }

    @SuppressLint("Range")
    public int getLastRowId () {

        String SQL = "SELECT " + DbCols.ID + " FROM " + DbTables.DAILY_GAMES_TABLE + " ORDER BY " + DbCols.ID + " DESC LIMIT 1";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(SQL, null);

        if (cursor == null) {
            MakeLog.error(TAG, "NULL Cursor.");
            database.close();
            return -1;
        }

        if (cursor.getCount() == 0) {
            MakeLog.error(TAG, "0 Record found.");
            cursor.close();
            database.close();
            return -1;
        }

        try {

            cursor.moveToFirst();

            int Id = -1;
            while (!cursor.isAfterLast()) {
                Id = cursor.getInt(cursor.getColumnIndex(DbCols.ID));
                cursor.moveToNext();
            }

            return Id;

        } catch (Exception e) {
            MakeLog.exception(e);
        } finally {

            cursor.close();
            database.close();
        }

        return -1;

    }

    @SuppressLint("Range")
    public String getGameSessionData (int id) {

        String RECORD_FETCH_QUERY = "SELECT " + DbCols.GAME_DATA + " FROM " + DbTables.DAILY_GAMES_TABLE + " WHERE " + DbCols.ID + "=" + id;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(RECORD_FETCH_QUERY, null);

        if (cursor == null) {
            MakeLog.error(TAG, "NULL Cursor in Game Session retrieval.");
            database.close();
            return null;
        }

        if (cursor.getCount() == 0) {
            MakeLog.error(TAG, "0 Record found in Game Session retrieval.");
            cursor.close();
            database.close();
            return null;
        }

        try {

            cursor.moveToFirst();

            String data = "";

            while (!cursor.isAfterLast()) {

                data = cursor.getString(cursor.getColumnIndex(DbCols.GAME_DATA));

                cursor.moveToNext();

            }

            return data;

        } catch (Exception e) {
            MakeLog.exception(e);
        } finally {

            cursor.close();
            database.close();
        }

        return null;

    }

    public void closeDB () {
        this.close();
    }

    @SuppressLint("Range")
    public ArrayList<ArrayList<Object>> getAllRecords () {

        ArrayList<ArrayList<Object>> record = null;

        SQLiteDatabase database = this.getReadableDatabase();

        String RECORD_FETCH_QUERY = "SELECT " + DbCols.ID  + ", " + DbCols.DAY + ", " + DbCols.MONTH + ", " + DbCols.YEAR + ", " + DbCols.STATUS + " FROM " + DbTables.DAILY_GAMES_TABLE;

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(RECORD_FETCH_QUERY, null);

        if (cursor == null) {
            MakeLog.error(TAG, "NULL Cursor.");
            database.close();
            return null;
        }

        if (cursor.getCount() == 0) {
            MakeLog.error(TAG, "0 Record found.");
            cursor.close();
            database.close();
            return null;
        }

        try {

            record = new ArrayList<>();

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                int id = cursor.getInt(cursor.getColumnIndex(DbCols.ID));
                int day = cursor.getInt(cursor.getColumnIndex(DbCols.DAY));
                int month = cursor.getInt(cursor.getColumnIndex(DbCols.MONTH));
                int year = cursor.getInt(cursor.getColumnIndex(DbCols.YEAR));
                int status = cursor.getInt(cursor.getColumnIndex(DbCols.STATUS));

                record.add(new ArrayList<>());
                record.get(record.size()-1).add(id);
                record.get(record.size()-1).add(new DateData(year, month, day));
                record.get(record.size()-1).add(status);

                cursor.moveToNext();

            }

            return record;

        } catch (Exception e) {
            MakeLog.exception(e);
        } finally {

            cursor.close();
            database.close();
        }

        return record;

    }

    public void addRecord (DateData dateData, int gameStatus, String gameSessionData) {

        int day = dateData.getDay();
        int month = dateData.getMonth();
        int year = dateData.getYear();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbCols.DAY, day);
        contentValues.put(DbCols.MONTH, month);
        contentValues.put(DbCols.YEAR, year);
        contentValues.put(DbCols.STATUS, gameStatus);
        contentValues.put(DbCols.GAME_DATA, gameSessionData);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DbTables.DAILY_GAMES_TABLE, null, contentValues);
        db.close();

    }


}
