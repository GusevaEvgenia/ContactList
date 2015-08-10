package ru.android.ainege.contactlist.ui;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import ru.android.ainege.contactlist.R;

public class ContactListFragment extends ListFragment{

    private ArrayAdapter<String> mAdapter;

    final String[] namesArray = new String[] { "Илья", "Анна", "Костя", "Виктор", "Лена"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.name, namesArray);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact_list, container, false);
        return v;
    }
}
