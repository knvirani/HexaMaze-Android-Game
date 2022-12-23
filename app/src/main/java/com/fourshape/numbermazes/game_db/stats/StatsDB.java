package com.fourshape.numbermazes.game_db.stats;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.fourshape.numbermazes.custom_calender.data_formats.DateData;
import com.fourshape.numbermazes.game_db.DbCols;
import com.fourshape.numbermazes.game_db.DbTables;
import com.fourshape.numbermazes.maze_core.GameCompletion;
import com.fourshape.numbermazes.utils.MakeLog;

import java.util.ArrayList;

public class StatsDB extends SQLiteOpenHelper {

    private final static String TAG = "StatsDB";
    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "game_stats.db";
    private final static String DB_TABLE = DbTables.STATISTICS_TABLE;
    private static int LAST_RECORD_ID = -1;

    private final static String CREATE_TABLE_SQL = "CREATE TABLE " + DB_TABLE + " "
            + "( " + DbCols.ID + " INTEGER PRIMARY KEY,"
            + DbCols.GAME_STAT_ID + " INTEGER,"
            + DbCols.GAME_COMPLETED + " INTEGER,"
            + DbCols.GAME_SCORE + " INTEGER,"
            + DbCols.PERFECT_WINS + " INTEGER,"
            + DbCols.GAME_TIME + " TIME,"
            + DbCols.DAY + " INTEGER,"
            + DbCols.MONTH + " INTEGER,"
            + DbCols.YEAR + " INTEGER " + ")";


    public StatsDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void markGameRecordEntry (int gameStatId, int gameTime, int gameScore, DateData date) {

        LAST_RECORD_ID = -1;

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbCols.GAME_STAT_ID, gameStatId);
        contentValues.put(DbCols.GAME_SCORE, gameScore);
        contentValues.put(DbCols.GAME_TIME, gameTime);
        contentValues.put(DbCols.DAY, date.getDay());
        contentValues.put(DbCols.MONTH, date.getMonth());
        contentValues.put(DbCols.YEAR, date.getYear());
        contentValues.put(DbCols.GAME_COMPLETED, 0);
        contentValues.put(DbCols.PERFECT_WINS, LAST_RECORD_ID);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DB_TABLE, null, contentValues);
        db.close();

        LAST_RECORD_ID = getLastRecordId();

    }

    private int getLastRecordId () {

        if (LAST_RECORD_ID == -1) {
            LAST_RECORD_ID = getLastRawId();
        }

        return LAST_RECORD_ID;
    }

    public void markPerfectWin (boolean hasPerfectWinAchieved) {

        if (LAST_RECORD_ID == -1) {
            LAST_RECORD_ID = getLastRawId();
        }

        int lastRecordId = LAST_RECORD_ID;

        if (lastRecordId != 0 && lastRecordId != -1) {

            MakeLog.info(TAG, "Game score updated.");
            ContentValues contentValues = new ContentValues();

            if (hasPerfectWinAchieved) {
                MakeLog.info(TAG, "Game has perfect win.");
                contentValues.put(DbCols.PERFECT_WINS, 1);
            }
            else
                contentValues.put(DbCols.PERFECT_WINS, 0);

            SQLiteDatabase db = this.getWritableDatabase();
            int result = db.update(DB_TABLE, contentValues, DbCols.ID + " = ? AND " + DbCols.PERFECT_WINS + " = ?", new String[]{String.valueOf(lastRecordId), "-1"});
            db.close();

            if (result > 0) {
                MakeLog.info(TAG, "Game perfect win updated.");
            } else {
                MakeLog.info(TAG, "Game perfect win can't be updated.");
            }

        } else {
            MakeLog.info(TAG, "Unable to Update game perfect wins as fetched record Id is 0 or -1.");
        }

    }

    @SuppressLint("Range")
    public ArrayList<ArrayList<Object>> getWinStreakRawData (int refId) {

        ArrayList<ArrayList<Object>> arrayLists = new ArrayList<>();

        String SQL = "SELECT " + DbCols.DAY + ", " + DbCols.MONTH + ", " + DbCols.YEAR  + ", " + DbCols.PERFECT_WINS + " FROM " + DB_TABLE + " WHERE " + DbCols.GAME_STAT_ID + "=" + refId + " AND " + DbCols.PERFECT_WINS + "=" + 1;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(SQL, null);

        if (cursor == null) {
            MakeLog.error(TAG, "NULL Cursor.");
            database.close();
            return arrayLists;
        }

        if (cursor.getCount() == 0) {
            MakeLog.error(TAG, "0 Record found.");
            cursor.close();
            database.close();
            return arrayLists;
        }

        try {

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                ArrayList<Object> objectArrayList = new ArrayList<>();

                objectArrayList.add(new DateData(cursor.getInt(cursor.getColumnIndex(DbCols.YEAR)), cursor.getInt(cursor.getColumnIndex(DbCols.MONTH)), cursor.getInt(cursor.getColumnIndex(DbCols.DAY))));
                objectArrayList.add(cursor.getInt(cursor.getColumnIndex(DbCols.PERFECT_WINS)));

                arrayLists.add(objectArrayList);

                cursor.moveToNext();

            }

        } catch (Exception e) {
            MakeLog.exception(e);
        }

        return arrayLists;
    }

    @SuppressLint("Range")
    public ArrayList<Integer> getCalculatedData (int refId) {

        ArrayList<Integer> arrayList = new ArrayList<>();

        String SQL = "SELECT COUNT(*) AS total_completed, MIN(" + DbCols.GAME_TIME + ") AS best_time, SUM(" + DbCols.PERFECT_WINS + ") AS perfect_wins, SUM(" + DbCols.GAME_SCORE + ") AS total_score" + " FROM " + DB_TABLE + " WHERE " + DbCols.GAME_STAT_ID + "=" + refId + " AND " + DbCols.GAME_COMPLETED + "=" + GameCompletion.COMPLETED;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(SQL, null);

        if (cursor == null) {
            MakeLog.error(TAG, "NULL Cursor.");
            database.close();
            return arrayList;
        }

        if (cursor.getCount() == 0) {
            MakeLog.error(TAG, "0 Record found.");
            cursor.close();
            database.close();
            return arrayList;
        }

        try {

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                arrayList.add(cursor.getInt(cursor.getColumnIndex("total_completed")));
                arrayList.add(cursor.getInt(cursor.getColumnIndex("best_time")));
                arrayList.add(cursor.getInt(cursor.getColumnIndex("perfect_wins")));
                arrayList.add(cursor.getInt(cursor.getColumnIndex("total_score")));
                cursor.moveToNext();
            }

        } catch (Exception e) {
            MakeLog.exception(e);
        } finally {

            cursor.close();
            database.close();
        }


        return arrayList;
    }

    public void updateGameStuff (int gameScore, int gameTime, int gameCompletion) {

        if (LAST_RECORD_ID == -1) {
            LAST_RECORD_ID = getLastRawId();
        }

        int lastRecordId = LAST_RECORD_ID;

        if (lastRecordId != 0 && lastRecordId != -1) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(DbCols.GAME_SCORE, gameScore);
            contentValues.put(DbCols.GAME_COMPLETED, gameCompletion);
            contentValues.put(DbCols.GAME_TIME, gameTime);

            SQLiteDatabase db = this.getWritableDatabase();
            db.update(DB_TABLE, contentValues, DbCols.ID + " = ?", new String[]{String.valueOf(lastRecordId)});
            db.close();

            MakeLog.info(TAG, "Game score updated.");

        } else {
            MakeLog.info(TAG, "Unable to Update game score as fetched record Id is 0 or -1.");
        }

    }

    @SuppressLint("Range")
    private int getLastRawId () {

        String SQL = "SELECT " + DbCols.ID + " FROM " + DB_TABLE + " ORDER BY " + DbCols.ID + " DESC LIMIT 1";
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

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(sqLiteDatabase);
    }

}
