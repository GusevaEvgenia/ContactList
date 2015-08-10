package ru.android.ainege.contactlist.ui;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import ru.android.ainege.contactlist.R;
import ru.android.ainege.contactlist.db.ContactTable;
import ru.android.ainege.contactlist.provider.ContactListContract;

public class ContactListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private CursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ContactsAdapter(R.layout.list_item, null);
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact_list, container, false);
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ContactListContract.Contacts.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private class ContactsAdapter extends ResourceCursorAdapter {

        public ContactsAdapter(int layout, Cursor c) {
            super(getActivity(), layout, c, 0);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ImageView photo = (ImageView) view.findViewById(R.id.avatar);
            photo.setImageResource(R.drawable.default_avatar);

            TextView name = (TextView) view.findViewById(R.id.name);
            name.setText(cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_NAME)));

            TextView price = (TextView) view.findViewById(R.id.email);
            price.setText(cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_EMAIL)));
        }
    }
}
