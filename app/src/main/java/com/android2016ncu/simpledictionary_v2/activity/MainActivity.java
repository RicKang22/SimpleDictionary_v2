package com.android2016ncu.simpledictionary_v2.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.SearchView;
import android.widget.Toast;

import com.android2016ncu.simpledictionary_v2.R;
import com.android2016ncu.simpledictionary_v2.other.aboutDialog;
import com.android2016ncu.simpledictionary_v2.util.FileUtil;
import com.android2016ncu.simpledictionary_v2.util.MyApplication;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SearchView searchView;

    //private LinearLayout searchWords_fatherLayout;
    private Fragment interpretation;
    private aboutDialog aboutDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置悬浮按钮以及点击监听
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        initServiceBar();



    }
    public void initServiceBar(){

        searchView = (SearchView) findViewById(R.id.searchWords_searchView);

        searchView.clearFocus();//清除焦点
        searchView.setSubmitButtonEnabled(true);//设置显示搜索按钮
        searchView.setIconifiedByDefault(false);//设置不自动缩小为图标

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //传入搜索值
                Bundle bundle = new Bundle();
                bundle.putString("key",query);

                interpretation = new InterpretationFragment();
                interpretation.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction
                        .replace(R.id.interpretation_fragment_replace,interpretation)
//                        .addToBackStack(null)
                        .commit();
                //清除输入框焦点
                searchView.clearFocus();


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_vocabulary) {
            startActivity(new Intent(MyApplication.getContext(),VocabularyActivity.class));
        } else if (id == R.id.nav_dailySent) {
            startActivity(new Intent(MyApplication.getContext(),DailySentActivity.class));
        } else if (id == R.id.nav_test) {

        } else if (id == R.id.nav_cleanCache) {
            new AlertDialog.Builder(this)
                    .setTitle("警告")
                    .setCancelable(false)
                    .setMessage("确定清除所有本地缓存吗?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FileUtil.getInstance().deleteFile();
                            Toast.makeText(MyApplication.getContext(), "清除成功", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        } else if (id == R.id.nav_aboutMe) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            aboutDialog = new aboutDialog(this);
            aboutDialog.show();
        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
