package trinadhkoya.info.sharedpreferencesthroughbean;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import trinadhkoya.info.sharedpreferencesthroughbean.fragment.DoctorListFragment;
import trinadhkoya.info.sharedpreferencesthroughbean.fragment.FavoriteListFragment;


public class MainActivity extends FragmentActivity {

    DoctorListFragment pdtListFragment;
    FavoriteListFragment favListFragment;
    private Fragment contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

		/*
		 * This is called when orientation is changed.
		 */
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("content")) {
                String content = savedInstanceState.getString("content");
                if (content.equals(FavoriteListFragment.ARG_ITEM_ID)) {
                    if (fragmentManager.findFragmentByTag(FavoriteListFragment.ARG_ITEM_ID) != null) {
                        setFragmentTitle(R.string.favorites);
                        contentFragment = fragmentManager
                                .findFragmentByTag(FavoriteListFragment.ARG_ITEM_ID);
                    }
                }
            }
            if (fragmentManager.findFragmentByTag(DoctorListFragment.ARG_ITEM_ID) != null) {
                pdtListFragment = (DoctorListFragment) fragmentManager
                        .findFragmentByTag(DoctorListFragment.ARG_ITEM_ID);
                contentFragment = pdtListFragment;
            }
        } else {
            pdtListFragment = new DoctorListFragment();
            setFragmentTitle(R.string.app_name);
            switchContent(pdtListFragment, DoctorListFragment.ARG_ITEM_ID);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (contentFragment instanceof FavoriteListFragment) {
            outState.putString("content", FavoriteListFragment.ARG_ITEM_ID);
        } else {
            outState.putString("content", DoctorListFragment.ARG_ITEM_ID);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorites:
                setFragmentTitle(R.string.favorites);
                favListFragment = new FavoriteListFragment();
                switchContent(favListFragment, FavoriteListFragment.ARG_ITEM_ID);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void switchContent(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.popBackStackImmediate()) ;

        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction();
            transaction.replace(R.id.content_frame, fragment, tag);
            //Only FavoriteListFragment is added to the back stack.
            if (!(fragment instanceof DoctorListFragment)) {
                transaction.addToBackStack(tag);
            }
            transaction.commit();
            contentFragment = fragment;
        }
    }

    protected void setFragmentTitle(int resourseId) {
        setTitle(resourseId);
        getActionBar().setTitle(resourseId);

    }

    /*
     * We call super.onBackPressed(); when the stack entry count is > 0. if it
     * is instanceof DoctorListFragment or if the stack entry count is == 0, then
     * we finish the activity.
     * In other words, from DoctorListFragment on back press it quits the app.
     */
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else if (contentFragment instanceof DoctorListFragment
                || fm.getBackStackEntryCount() == 0) {
            finish();
        }
    }
}
