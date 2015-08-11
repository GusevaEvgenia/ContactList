package ru.android.ainege.contactlist.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import ru.android.ainege.contactlist.R;

public class ContactListActivity extends AppCompatActivity {

    FragmentManager mFm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        mFm = getFragmentManager();
        Fragment fragment = mFm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = new ContactListFragment();
            mFm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        DisplayImageOptions defaultOptions  = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.loading)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_contact) {
            Fragment newFragment = new ContactFragment();
            FragmentTransaction transaction = mFm.beginTransaction();

            if(mFm.findFragmentById(R.id.fragment_contact_container) == null){
                transaction.setCustomAnimations(R.animator.slide_up, 0)
                        .add(R.id.fragment_contact_container, newFragment);
            } else {
                transaction.replace(R.id.fragment_contact_container, newFragment);
            }
            transaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
