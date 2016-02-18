package com.ayman.moviesland;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;

import com.ayman.moviesland.fragments.Detailfragmentapp;
import com.ayman.moviesland.fragments.Mainfragmentapp;


public class MainActivity extends AppCompatActivity {

    private Mainfragmentapp mainfragmentapp;
    private FragmentManager fragmentmanager;
    private FragmentTransaction fragmentTransaction;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        //Check if this call is the first call or not
        if(savedInstanceState == null) {
            createMyFragment();
        }
        //Don't replace current detail fragment
        else {
            replaceMyFragment();
        }
    }

    private void createMyFragment() {
        fragmentmanager = this.getFragmentManager();
        fragmentTransaction = fragmentmanager.beginTransaction();

        mainfragmentapp = new Mainfragmentapp();
        fragmentTransaction.replace(R.id.main_fragment, mainfragmentapp);
        fragmentTransaction.commit();
        Log.d(LOG_TAG, "OnCreate() A new main fragment has been created");
    }
    private void replaceMyFragment() {
        fragmentmanager = this.getFragmentManager();
        fragmentTransaction = fragmentmanager.beginTransaction();
        fragmentTransaction.attach(new Detailfragmentapp());
        fragmentTransaction.commit();
        Log.d(LOG_TAG, "OnCreate() The old detail fragment wasn't replaced");
    }
    @Override
    public void onBackPressed() {
        if (fragmentmanager.getBackStackEntryCount() != 0) {
            fragmentmanager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
