package com.demo.mdb.spring2017finalassessment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.constraint.Placeholder;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TabbedActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_out) {
            FirebaseAuth.getInstance().signOut();
            finish(); // Closes this activity (which means the user goes back to the LoginActivity activity)
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlayFragment extends Fragment implements View.OnClickListener {

        public static final String AUTO_COMPLETE_KEY = "autoCompleteKey";

        Button playGameButton;
        CheckBox autoCompleteCheckBox;

        SharedPreferences sharedPreferences;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_play, container, false);
            playGameButton = (Button) view.findViewById(R.id.playButton);
            playGameButton.setOnClickListener(this);

            return view;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.playButton:
                    Intent intent = new Intent(getContext(), GameActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    public static class ScoresFragment extends Fragment {
        /* TODO Part 7
         * Implement the ScoresFragment. You will only need to implement one method.
         * This fragment uses the fragment_scores.xml layout. Initialize the RecyclerView and add a
         * LinearLayoutManager with a scores adapter that has empty data (check its constructor.)
         * Add a listener to update the adapter in real time. Note: ONLY ADD THE GAMES OF YOUR OWN
         * USER. Points will be deducted if you load everything.
         */
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_scores, container, false);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.scoresRecyclerView);
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager();
            //Create linear laoyut manager
            //Set adapter
            //use FirebaseAuth.getInstance().getCurrentUser().getUid() to find the UID which gets only this user data
            //recyclerView.setAdapter(linear layout manager)
            //initialize ScoresAdapter with empty list of game objects
            //Use FirebaseDatabase.getInstance() and then use the uid and then for loop through games to populate adapter

            return view;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            /* TODO Part 3
             * Set the first screen to the play fragment, and the second to the scores fragment
             */
            switch(position) {
                case 1:
                    return new ScoresFragment();
                default:
                case 0:
                    return new PlayFragment();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
            }
            return null;
        }
    }
}
