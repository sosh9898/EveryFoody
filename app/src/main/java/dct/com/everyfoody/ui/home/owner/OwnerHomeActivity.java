package dct.com.everyfoody.ui.home.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.ui.bookmark.BookmarkActivity;
import dct.com.everyfoody.ui.home.user.MainActivity;
import dct.com.everyfoody.ui.reservation.ReservationActivity;

public class OwnerHomeActivity extends WhiteThemeActivity {

    @BindView(R.id.drawer_logged)
    View drawerLogged;
    @BindView(R.id.owner_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar_owner)
    Toolbar ownerToolbar;

    private MainActivity.LoggedDrawer loggedDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home);
        ButterKnife.bind(this);
        bindDrawerEvent();
        setToolbar();
    }

    private void setToolbar() {
        ownerToolbar.setTitle("");
        setSupportActionBar(ownerToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, ownerToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void bindDrawerEvent() {
        loggedDrawer = new MainActivity.LoggedDrawer();

        ButterKnife.bind(loggedDrawer, drawerLogged);

        loggedDrawer.orderNameTextView.setText("순번내역");
        loggedDrawer.pushListTextView.setText("가게 푸시알람");

        //로그인 된 상태의 drawer
        loggedDrawer.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesService.getInstance().removeData("auth_token");
                Intent defaultHome = new Intent(OwnerHomeActivity.this, MainActivity.class);
                startActivity(defaultHome);
            }
        });
        loggedDrawer.bookMarkCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookmark = new Intent(getApplicationContext(), BookmarkActivity.class);
                startActivity(bookmark);
            }
        });
        loggedDrawer.orderCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reservationIntent = new Intent(OwnerHomeActivity.this, ReservationActivity.class);
                startActivity(reservationIntent);
            }
        });

        View.OnClickListener profileEditClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //프로필 수정
                Toast.makeText(OwnerHomeActivity.this, "프로필 수정기능 구현해야함~", Toast.LENGTH_SHORT).show();
            }
        };
        loggedDrawer.profileEditImageView.setOnClickListener(profileEditClickListener);
        loggedDrawer.profileImageView.setOnClickListener(profileEditClickListener);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.menu_notify_false || id == R.id.menu_notify_true) {
//            Intent notiIntent = new Intent(this, NotifyActivity.class);
//            startActivity(notiIntent);
//        }

        /*FIXME
        위에 주석 풀어주고 아래 지웁시다!!
         */

        if (id == R.id.menu_notify_true) {
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem notiTrue = menu.findItem(R.id.menu_notify_true);
        MenuItem notiFalse = menu.findItem(R.id.menu_notify_false);

        /*TODO
        알림이 있을 경우 false invisible  알림이 없을 경우 true invisible
         */

        return super.onPrepareOptionsMenu(menu);
    }
}
