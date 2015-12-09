package by.dev.android.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, DbHelper.DATABASE_NAME, null, DbHelper.VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table " + DbSchema.NewsTable.NAME + "("
                + DbSchema.NewsTable.Cols._ID + " integer primary key autoincrement, "
                + DbSchema.NewsTable.Cols.TITLE + ", "
                + DbSchema.NewsTable.Cols.PUB_DATE + ", "
                + DbSchema.NewsTable.Cols.DESCRIPTION + ", "
                + DbSchema.NewsTable.Cols.FULL_TEXT + ", "
                + DbSchema.NewsTable.Cols.LINK + ", "
                + DbSchema.NewsTable.Cols.GUID + ")")
        db.execSQL("create table " + DbSchema.EnclosureTable.NAME + "("
                + DbSchema.EnclosureTable.Cols._ID + " integer primary key autoincrement, "
                + DbSchema.EnclosureTable.Cols.URL + ", "
                + DbSchema.EnclosureTable.Cols.TYPE + ", "
                + DbSchema.EnclosureTable.Cols.NEWS_ID + ")")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // do nothing
    }

    companion object {

        private val VERSION = 1

        private val DATABASE_NAME = "dev_by.db"
    }
}