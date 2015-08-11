package ru.android.ainege.contactlist.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;


public class ContactTable {
    public static final String TABLE_NAME = "Contact";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_EMAIL = "email";

    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME
            + "("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_SURNAME + " TEXT NOT NULL, "
            + COLUMN_EMAIL + " TEXT NOT NULL"
            + ");";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        initialData(db);
    }

    private static void initialData(SQLiteDatabase database) {
        String[] name = { "Илья", "Ира", "Анна", "Стас", "Лена", "Артем" };
        String[] surname = { "Иванов", "Титова", "Петрова", "Петренко", "Андреева", "Соболев" };
        String[] email = {"Илья@gmail.com", "Ира@gmail.com", "Анна@gmail.com", "Стас@gmail.com", "Лена@gmail.com", "Артем@gmail.com"};

        for(int i = 0; i < name.length; i++) {
            ContentValues contentValue = new ContentValues();
            contentValue.put(COLUMN_NAME, name[i]);
            contentValue.put(COLUMN_SURNAME, surname[i]);
            contentValue.put(COLUMN_EMAIL, email[i]);
            database.insert(TABLE_NAME, null, contentValue);
        }
    }
}
