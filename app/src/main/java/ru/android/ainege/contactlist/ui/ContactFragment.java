package ru.android.ainege.contactlist.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import ru.android.ainege.contactlist.Gravatar;
import ru.android.ainege.contactlist.R;
import ru.android.ainege.contactlist.provider.ContactListContract;

public class ContactFragment extends Fragment {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String EMAIL = "email";

    private EditText mName;
    private EditText mSurname;
    private EditText mEmail;
    private ImageView mAvatar;

    public static ContactFragment newInstance(long id, String name, String surname, String email) {
        Bundle args = new Bundle();
        args.putLong(ID, id);
        args.putString(NAME, name);
        args.putString(SURNAME, surname);
        args.putString(EMAIL, email);

        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact, container, false);

        mName = (EditText) v.findViewById(R.id.name_editText);
        mSurname = (EditText) v.findViewById(R.id.surname_editText);
        mEmail = (EditText) v.findViewById(R.id.email_editText);
        mAvatar = (ImageView) v.findViewById(R.id.avatar);

        if (getArguments() != null) {
            mName.setText(getArguments().getString(NAME));
            mSurname.setText(getArguments().getString(SURNAME));
            mEmail.setText(getArguments().getString(EMAIL));

            loadImage();
        }

        Button ok = (Button) v.findViewById(R.id.ok_button);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveData()) {
                    slideDown();
                }
            }
        });

        Button cancel = (Button) v.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideDown();
            }
        });
        return v;
    }

    private boolean saveData() {
        if (isValid()) {
            loadImage();
            ContentValues contentValue = new ContentValues();
            contentValue.put(ContactListContract.Contacts.COLUMN_NAME, mName.getText().toString());
            contentValue.put(ContactListContract.Contacts.COLUMN_SURNAME, mSurname.getText().toString());
            contentValue.put(ContactListContract.Contacts.COLUMN_EMAIL, mEmail.getText().toString());
            if (getArguments() == null) {
                getActivity().getContentResolver().insert(ContactListContract.Contacts.CONTENT_URI, contentValue);
            } else {
                getActivity().getContentResolver().update(
                        Uri.parse(ContactListContract.Contacts.CONTENT_URI + "/" + getArguments().getLong(ID)),
                        contentValue, null, null);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean isValid() {
        boolean result = true;
        if (!TextUtils.isEmpty(mName.getText().toString().trim())) {
            if (mName.getError() != null) {
                mName.setError(null);
            }
        } else {
            mName.setError(getResources().getText(R.string.error_name));
            result = false;
        }

        String email = mEmail.getText().toString().trim();
        if (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (mEmail.getError() != null) {
                mEmail.setError(null);
            }
        } else {
            mEmail.setError(getResources().getText(R.string.error_email));
            result = false;
        }
        return result;
    }

    private void loadImage() {
        String gravatarUrl = Gravatar.getUri(mEmail.getText().toString().trim());
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(gravatarUrl, mAvatar);
    }

    private void slideDown() {
        TranslateAnimation trans = new TranslateAnimation(0, 0, 0, 1280);
        trans.setDuration(200);
        trans.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().remove(fm.findFragmentById(R.id.fragment_contact_container)).commit();
            }
        });
        getView().startAnimation(trans);
    }
}
