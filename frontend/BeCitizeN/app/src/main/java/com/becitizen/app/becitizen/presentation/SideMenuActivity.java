package com.becitizen.app.becitizen.presentation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;
import com.becitizen.app.becitizen.presentation.controllers.ControllerUserPresentation;
import com.becitizen.app.becitizen.presentation.faq.FaqCategoriesActivity;
import com.becitizen.app.becitizen.presentation.forum.ForumCategoriesActivity;
import com.becitizen.app.becitizen.presentation.info.InformationCategoriesActivity;
import com.becitizen.app.becitizen.presentation.msg.AllConversationsActivity;
import com.becitizen.app.becitizen.presentation.user.LoggedAsGuestActivity;
import com.becitizen.app.becitizen.presentation.user.MainActivity;
import com.becitizen.app.becitizen.presentation.user.UserProfileActivity;
import com.becitizen.app.becitizen.presentation.utilities.AboutActivity;
import com.becitizen.app.becitizen.presentation.utilities.HelpActivity;
import com.becitizen.app.becitizen.presentation.utilities.UtilitiesMenu;

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
            else ControllerUserPresentation.getUniqueInstance().getLoggedUser();
        } catch (Exception e) {
            e.printStackTrace();
            goToLogin();
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("fragment") && bundle.getString("fragment").equals("UserProfileActivity")) {
            viewProfile(bundle);
        } else {
            //Set the fragment initially
            Fragment fragment = new MenuActivity();
            fragmentTransaction(fragment, "MENU_ACTIVITY");
        }

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

    private void fragmentTransaction(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
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
            int fragments = getSupportFragmentManager().getBackStackEntryCount();
            if (fragments == 1) {
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.logout))
                        .setMessage(getResources().getString(R.string.logoutMsg))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                ControllerUserPresentation.getUniqueInstance().logout();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(intent);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            } else {
                if (getFragmentManager().getBackStackEntryCount() > 1) {
                    getFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
            }
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
            try {
                if (ControllerUserPresentation.getUniqueInstance().isLoggedAsGuest()) {
                    Fragment fragment = new LoggedAsGuestActivity();
                    fragmentTransaction(fragment, "LOGGED_AS_GUEST_ACTIVITY");
                    return true;
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("loggeduser", true);
                    Fragment fragment = new UserProfileActivity();
                    fragment.setArguments(bundle);
                    fragmentTransaction(fragment, "USER_PROFILE");
                    return true;
                }
            } catch (SharedPreferencesException e) {
                e.printStackTrace();
                //Initialize MySharedPreferences
                ControllerUserPresentation.getUniqueInstance().initializeMySharedPreferences(this);
                goToLogin();
            }
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
                fragment = new InformationCategoriesActivity();
                fragmentTransaction(fragment, "INFORMATION_CATEGORY_ACTIVITY");
                break;
            case R.id.nav_faq:
                fragment = new FaqCategoriesActivity();
                fragmentTransaction(fragment, "FAQ_CATEGORY_ACTIVITY");
                break;
            case R.id.nav_utilities:
                fragment = new UtilitiesMenu();
                fragmentTransaction(fragment, "UTILITIES_MENU");
                break;
            case R.id.nav_forum:
                fragment = new ForumCategoriesActivity();
                fragmentTransaction(fragment, "FORUM_CATEGORY_ACTIVITY");
                break;
            case R.id.nav_private_messages:
                try {
                    if (ControllerUserPresentation.getUniqueInstance().isLoggedAsGuest()) {
                        fragment = new LoggedAsGuestActivity();
                        fragmentTransaction(fragment, "LOGGED_AS_GUEST_ACTIVITY");
                    } else {
                        fragment = new AllConversationsActivity();
                        fragmentTransaction(fragment, "ALL_CONVERSATIONS_ACTIVITY");
                    }
                } catch (SharedPreferencesException e) {
                    e.printStackTrace();
                    //Initialize MySharedPreferences
                    ControllerUserPresentation.getUniqueInstance().initializeMySharedPreferences(this);
                    goToLogin();
                }
                break;
            case R.id.nav_help:
                fragment = new HelpActivity();
                fragmentTransaction(fragment, "HELP_ACTIVITY");
                break;
            case R.id.nav_about:
                fragment = new AboutActivity();
                fragmentTransaction(fragment, "ABOUT_ACTIVITY");
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void viewProfile(Bundle bundle) {
        Fragment fragment = new UserProfileActivity();
        fragment.setArguments(bundle);
        fragmentTransaction(fragment, "USER_PROFILE");
    }
}
