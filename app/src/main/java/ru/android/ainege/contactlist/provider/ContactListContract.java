package ru.android.ainege.contactlist.provider;

import android.content.ContentResolver;
import android.net.Uri;

public final class ContactListContract {
    public static final String AUTHORITY =  "ru.android.ainege.provider.ContractList";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Contacts {
        public static final String PATH = "contacts";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ContactListContract.CONTENT_URI, PATH);

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/contacts";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/contact";
    }
}
