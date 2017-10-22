package dct.com.everyfoody.ui.home.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.SideMenu;
import dct.com.everyfoody.request.NetworkService;
import dct.com.everyfoody.ui.detail.DetailActivity;
import dct.com.everyfoody.ui.detail.location.MapActivity;
import dct.com.everyfoody.ui.home.owner.turn.TurnActivity;
import dct.com.everyfoody.ui.home.user.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerHomeActivity extends WhiteThemeActivity {

    @BindView(R.id.drawer_logged)
    View drawerLogged;
    @BindView(R.id.owner_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar_owner)
    Toolbar ownerToolbar;

    private MainActivity.LoggedDrawer loggedDrawer;
    private NetworkService networkService;
    private OwnerSettingRecyclerAdapter settingAdapter;
    private List<Integer> statusList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home);
        ButterKnife.bind(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        SharedPreferencesService.getInstance().load(this);
        bindDrawerEvent();
        setToolbar();
        setRecycler();

        Log.d("???", SharedPreferencesService.getInstance().getPrefStringData("auth_token"));
    }

    private void setRecycler() {
        statusList = new ArrayList<>();
        loggedDrawer.settingRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        settingAdapter = new OwnerSettingRecyclerAdapter(statusList);
        loggedDrawer.settingRecycler.setAdapter(settingAdapter);
    }

    private void setToolbar() {
        ownerToolbar.setTitle("");
        setSupportActionBar(ownerToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, ownerToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING) {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    } else {
                        openSideMenu();
                    }
                    invalidateOptionsMenu();
                }
            }

        };
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
                SharedPreferencesService.getInstance().removeData("auth_token", "user_status");
                Intent defaultHome = new Intent(OwnerHomeActivity.this, MainActivity.class);
                startActivity(defaultHome);
            }
        });
        loggedDrawer.orderCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent turnIntent = new Intent(OwnerHomeActivity.this, TurnActivity.class);
                startActivity(turnIntent);
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

    @OnClick(R.id.open_store)
    public void onClickOpen(View view) {
        Intent openIntent = new Intent(this, MapActivity.class);
        openIntent.putExtra("lat", 32.089);
        openIntent.putExtra("lng", 125.180);
        startActivity(openIntent);
    }

    @OnClick(R.id.check_turn)
    public void onClickCheckTurn(View view) {
        Intent turnIntent = new Intent(this, TurnActivity.class);
        startActivity(turnIntent);
    }

    @OnClick(R.id.my_store)
    public void onClickMyStoreInfo(View view) {
        Intent myStoreIntent = new Intent(this, DetailActivity.class);
        startActivity(myStoreIntent);
    }

    private void openSideMenu() {
        Call<SideMenu> sideMenuCall = networkService.getSideMenuInfo(SharedPreferencesService.getInstance().getPrefStringData("auth_token"),
                SharedPreferencesService.getInstance().getPrefIntegerData("user_status"));

        sideMenuCall.enqueue(new Callback<SideMenu>() {
            @Override
            public void onResponse(Call<SideMenu> call, Response<SideMenu> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        loggedDrawer.bookMarkCountTextView.setText(response.body().getSideInfo().getBmNum() + "");
                        loggedDrawer.orderCountTextView.setText(response.body().getSideInfo().getResNum() + "");
                        settingAdapter.refreshAdapter(response.body().getSideInfo().getOwnerStatus());
                    }
                }
            }

            @Override
            public void onFailure(Call<SideMenu> call, Throwable t) {
                Log.d("????", t.toString());
            }
        });
    }
}
