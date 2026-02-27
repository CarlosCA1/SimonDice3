package com.example.simondice2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ScoreDBHelper(context: Context) :
    SQLiteOpenHelper(context, "scores.db", null, 6) {

    override fun onCreate(db: SQLiteDatabase) {
        //Añado "fecha" para gestionar los empates y saber cuándo se consiguió cada récord.
        db.execSQL(
            """
            CREATE TABLE scores (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                puntos INTEGER NOT NULL,
                fecha DATETIME DEFAULT CURRENT_TIMESTAMP
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS scores")
        onCreate(db)
    }
}