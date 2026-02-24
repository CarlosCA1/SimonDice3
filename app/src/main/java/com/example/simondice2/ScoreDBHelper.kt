package com.example.simondice2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ScoreDBHelper(context: Context) :
    SQLiteOpenHelper(context, "scores.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE scores (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                puntos INTEGER NOT NULL
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS scores")
        onCreate(db)
    }
}
