package ru.android.ainege.contactlist.ui;

import android.app.ListFragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import ru.android.ainege.contactlist.R;
import ru.android.ainege.contactlist.db.ContactTable;
public class ContactListFragment extends ListFragment{

    private SimpleCursorAdapter mAdapter;
    private Cursor mCursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCursor = getActivity().getContentResolver().query(Uri.parse("content://ru.android.ainege.provider.ContractList/contacts"),
                null, null, null, null);

        String[] from = new String[] { ContactTable.COLUMN_NAME, ContactTable.COLUMN_EMAIL};
        int[] to = new int[] { R.id.name, R.id.email };
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item, mCursor, from, to, 0);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact_list, container, false);
        return v;
    }


}
