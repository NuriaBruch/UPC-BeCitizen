package com.becitizen.app.becitizen.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.MySharedPreferences;

public class SideMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);

        //Initialize MySharedPreferences
        ControllerUserPresentation.getUniqueInstance().initializeMySharedPreferences(this);

        try {
            if(!ControllerUserPresentation.getUniqueInstance().isLogged()) goToLogin();
        } catch (Exception e) {
            e.printStackTrace();
            goToLogin();
        }

        //Set the fragment initially
        Fragment fragment = new InsideActivity();
        fragmentTransaction(fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void fragmentTransaction(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.side_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("loggeduser", true);
            Fragment fragment = new UserProfile();
            fragment.setArguments(bundle);
            fragmentTransaction(fragment);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment;

        switch (id) {
            case R.id.nav_information:
                fragment = new InsideActivity();
                fragmentTransaction(fragment);
                break;
            case R.id.nav_faq:
                /*
                Fragment fragment = new FaqActivity();
                fragmentTransaction(fragment);
                */
                break;
            case R.id.nav_utilities:
                /*
                Fragment fragment = new UtilitiesActivity();
                fragmentTransaction(fragment);
                */
                break;
            case R.id.nav_forum:
                fragment = new CategoryThreadActivity();
                fragmentTransaction(fragment);

                //fragment = new ForumCategoriesActivity();
                //fragmentTransaction(fragment);
                break;
            case R.id.nav_private_messages:
                /*
                Fragment fragment = new PrivateMessagesActivity();
                fragmentTransaction(fragment);
                */
                break;
            case R.id.nav_settings:
                /*
                Fragment fragment = new SettingsActivity();
                fragmentTransaction(fragment);
                */
                break;
            case R.id.nav_help:
                /*
                Fragment fragment = new HelpActivity();
                fragmentTransaction(fragment);
                */
                break;
            case R.id.nav_about:
                /*
                Fragment fragment = new AboutActivity();
                fragmentTransaction(fragment);
                */
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
