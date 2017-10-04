package dct.com.everyfoody.ui.home.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.ui.detail.DetailActivity;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_fab)
    FloatingActionButton fab;
    @BindView(R.id.main_rcv)
    RecyclerView mainRecycler;
    @BindView(R.id.toolbar_guest)
    Toolbar guestToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setToolbar();
        setRecycler();
        getInitData();

    }

    private void setRecycler(){
        mainRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mainRecycler.setAdapter(new TruckRecyclerAdapter());
    }

    private void setToolbar() {
        guestToolbar.setTitle("");
        setSupportActionBar(guestToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, guestToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void getInitData(){
        /*TODO
        서버와 통신부 추가!!
         */
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

        if(id == R.id.menu_notify_true){
            Intent detailIntent = new Intent(this, DetailActivity.class);
            startActivity(detailIntent);
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

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent detailIntent = new Intent(view.getContext(), DetailActivity.class);
            startActivity(detailIntent);
        }
    };

}
