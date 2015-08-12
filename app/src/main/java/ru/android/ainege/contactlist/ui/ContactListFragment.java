package ru.android.ainege.contactlist.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import ru.android.ainege.contactlist.Gravatar;
import ru.android.ainege.contactlist.R;
import ru.android.ainege.contactlist.db.ContactTable;
import ru.android.ainege.contactlist.provider.ContactListContract;

public class ContactListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private CursorAdapter mAdapter;
    private Cursor mCursor;

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
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCursor.moveToPosition(position);

        long idContact = mCursor.getLong(mCursor.getColumnIndex(BaseColumns._ID));
        String name = mCursor.getString(mCursor.getColumnIndex(ContactTable.COLUMN_NAME));
        String surname = mCursor.getString(mCursor.getColumnIndex(ContactTable.COLUMN_SURNAME));
        String email = mCursor.getString(mCursor.getColumnIndex(ContactTable.COLUMN_EMAIL));

        FragmentManager fm = getFragmentManager();
        Fragment newFragment = ContactFragment.newInstance(idContact, name, surname, email);
        FragmentTransaction transaction = fm.beginTransaction();

        if(fm.findFragmentById(R.id.fragment_contact_container) == null){
           transaction.setCustomAnimations(R.animator.slide_up, 0).add(R.id.fragment_contact_container, newFragment);
        } else {
            transaction.replace(R.id.fragment_contact_container, newFragment);
        }
        transaction.commit();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ContactListContract.Contacts.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
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
            String mail = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_EMAIL));
            ImageView photo = (ImageView) view.findViewById(R.id.photo);

            String gravatarUrl = Gravatar.getUri(mail);
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(gravatarUrl, photo);

            TextView name = (TextView) view.findViewById(R.id.name);
            name.setText(cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_NAME)) + " " +
                    cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_SURNAME)));

            TextView email = (TextView) view.findViewById(R.id.email);
            email.setText(mail);
        }
    }
}
