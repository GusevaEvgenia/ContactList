package ru.android.ainege.contactlist.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import ru.android.ainege.contactlist.R;
import ru.android.ainege.contactlist.db.ContactTable;
import ru.android.ainege.contactlist.db.DataBaseHelper;

public class ContactListContentProvider extends ContentProvider{

    static final int CONTACTS = 1;
    static final int CONTACT_ID = 2;

    static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        uriMatcher.addURI(ContactListContract.AUTHORITY, ContactListContract.Contacts.PATH, CONTACTS);
        uriMatcher.addURI(ContactListContract.AUTHORITY, ContactListContract.Contacts.PATH + "/#", CONTACT_ID);
    }

    private DataBaseHelper dataBase;

    @Override
    public boolean onCreate() {
        dataBase = new DataBaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName;
        switch (uriMatcher.match(uri)) {
            case CONTACT_ID:
                selection = prepareSelection(uri, selection);
            case CONTACTS:
                tableName = ContactTable.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_wrong_uri) + uri);
        }

        SQLiteDatabase db = dataBase.getReadableDatabase();
        Cursor cursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                return ContactListContract.Contacts.CONTENT_TYPE;
            case CONTACT_ID:
                return ContactListContract.Contacts.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_wrong_uri) + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                tableName = ContactTable.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_wrong_uri) + uri);
        }

        SQLiteDatabase db = dataBase.getWritableDatabase();
        long rowID = db.insert(tableName, null, values);

        if (rowID > 0) {
            Uri resultUri = ContentUris.withAppendedId(ContactListContract.Contacts.CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(resultUri, null);
            return resultUri;
        }
        throw new SQLException(getContext().getResources().getString(R.string.error_insert) + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName;
        switch (uriMatcher.match(uri)) {
            case CONTACT_ID:
                selection = prepareSelection(uri, selection);
            case CONTACTS:
                tableName = ContactTable.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_wrong_uri) + uri);
        }

        SQLiteDatabase db = dataBase.getWritableDatabase();
        int count = db.delete(tableName, selection, selectionArgs);

        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName;
        switch (uriMatcher.match(uri)) {
            case CONTACT_ID:
                selection = prepareSelection(uri, selection);
            case CONTACTS:
                tableName = ContactTable.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.error_wrong_uri) + uri );
        }

        SQLiteDatabase db = dataBase.getWritableDatabase();
        int count = db.update(tableName, values, selection, selectionArgs);

        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    private String prepareSelection(Uri uri, String selection) {
        String id = uri.getLastPathSegment();
        if (TextUtils.isEmpty(selection)) {
            selection = BaseColumns._ID + " = " + id;
        } else {
            selection = selection + " AND " + BaseColumns._ID + " = " + id;
        }
        return selection;
    }
}
