package com.lyhome.ETNews;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.lyhome.ETNews.activity.LoginActivity;
import com.lyhome.ETNews.activity.PersonalActivity;
import com.lyhome.ETNews.bean.Constant;
import com.lyhome.ETNews.bean.User;
import com.lyhome.ETNews.biz.UserProxy;
import com.lyhome.ETNews.fragment.JoyFragment;
import com.lyhome.ETNews.fragment.SettingFragment;
import com.lyhome.ETNews.fragment.TechFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mView;
    private String[] mDrawerTitles = {"科技", "娱乐", "设置"};
    private List<Fragment> mFragmentList;
    private Class[] mClasses = {TechFragment.class, JoyFragment.class, SettingFragment.class};
    private ImageView mIvPhoto;
    private TextView mTvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_main);

        mFragmentList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mFragmentList.add(null);
        }
        initView();
        selectItem(0);
    }

    private void selectItem(int position) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        for (Fragment fragment : mFragmentList) {
            if (fragment != null) {
                fragmentTransaction.hide(fragment);
            }
        }

        Fragment mFragment;

        if (mFragmentList.get(position) == null) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.TITLE, mDrawerTitles[position]);
            mFragment = Fragment.instantiate(this, mClasses[position].getName(), bundle);
            mFragmentList.set(position, mFragment);
            fragmentTransaction.add(R.id.main, mFragment);
        } else {
            mFragment = mFragmentList.get(position);
            fragmentTransaction.show(mFragment);
        }
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(mDrawerTitles[position]);
    }

    private void initView() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mView = (NavigationView) findViewById(R.id.navigation_view);
        mLayout = (DrawerLayout) findViewById(R.id.dl_left);

        mToolbar.setTitle("界面");

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.menu);

        mToggle = new ActionBarDrawerToggle(this, mLayout, mToolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mLayout.setDrawerListener(mToggle);
        setupDrawerContent(mView);
    }

    private void setupDrawerContent(NavigationView mNavigationView) {

        View header = LayoutInflater.from(this).inflate(R.layout.navigation_header, null);
        mIvPhoto = (ImageView) header.findViewById(R.id.photo_iv);
        mIvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserProxy.isLogin(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, PersonalActivity.class);
                    startActivity(intent);
                }
            }
        });

        mTvLogin = (TextView) header.findViewById(R.id.login_tv);
        if (UserProxy.isLogin(this)) {
            User user = UserProxy.getCurrentUser(this);
            mTvLogin.setText(user.getUsername());
        }

        mNavigationView.addHeaderView(header);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_tech) {
                    selectItem(0);
                } else if (itemId == R.id.nav_joy) {
                    selectItem(1);
                } else if (itemId == R.id.nav_settings) {
                    selectItem(2);
                } else if (itemId == R.id.nav_personal) {
                    startActivity(new Intent(MainActivity.this, PersonalActivity.class));
                }
                menuItem.setChecked(true);
                mLayout.closeDrawers();
                return true;
            }
        });
    }
}







