package dct.com.everyfoody.ui.home.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.ui.detail.DetailActivity;
import dct.com.everyfoody.ui.home.MapClipDataHelper;
import dct.com.everyfoody.ui.login.LoginActivity;
import dct.com.everyfoody.ui.reservation.ReservationActivity;
import dct.com.everyfoody.ui.signup.SignUpMainActivity;

public class MainActivity extends WhiteThemeActivity {

    private static final int MAP_CLIP_COUNT = 8;
    private static final int REQUEST_CODE_FOR_LOGIN = 201;
    private static final int REQUEST_CODE_FOR_SIGNUP = 202;

    @BindView(R.id.main_fab)
    FloatingActionButton fab;
    @BindView(R.id.main_rcv)
    RecyclerView mainRecycler;
    @BindView(R.id.toolbar_guest)
    Toolbar guestToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.selected_address_name)
    TextView selectedAddressName;
    @BindView(R.id.drawer_default)
    View drawerDefault;
    @BindView(R.id.drawer_logged)
    View drawerLogged;

    private ImageView[] mapClipImageViews;
    private TextView[] mapClipTextViews;
    private DefaultDrawer defaultDrawer;
    private LoggedDrawer loggedDrawer;
    private int lastClickedMapPosition = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setToolbar();
        setRecycler();
        getInitData();
        bindDrawerEvent();

        MapClipDataHelper.initialize();

        mapClipTextViews = new TextView[MAP_CLIP_COUNT];
        mapClipImageViews = new ImageView[MAP_CLIP_COUNT];
        View mapClipContainer = findViewById(R.id.map_clip_container);
        for (int i = 0; i < MAP_CLIP_COUNT; i++) {
            TextView childMapClipTextView = mapClipContainer.findViewWithTag((i + 1) + "");
            ImageView childMapClipImageView = mapClipContainer.findViewWithTag("area" + (i + 1));

            childMapClipTextView.setOnClickListener(mapClipClickListener);
            mapClipTextViews[i] = childMapClipTextView;
            mapClipImageViews[i] = childMapClipImageView;
        }

    }

    private void bindDrawerEvent() {
        defaultDrawer = new DefaultDrawer();
        loggedDrawer = new LoggedDrawer();

        ButterKnife.bind(defaultDrawer, drawerDefault);
        ButterKnife.bind(loggedDrawer, drawerLogged);

        /**
         * FIXME:
         * @OnClick 사용하던가, View.OnClickListener 구현부에 switch-case 사용하던가 바꾸면 깔끔해보일듯 ㅎ
         * */
        View.OnClickListener loginClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(loginIntent, REQUEST_CODE_FOR_LOGIN);
            }
        };
        defaultDrawer.loginContainer.setOnClickListener(loginClickListener);
        defaultDrawer.pleaseLoginTextView.setOnClickListener(loginClickListener);
        defaultDrawer.signUpContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(MainActivity.this, SignUpMainActivity.class);
                startActivityForResult(signUpIntent, REQUEST_CODE_FOR_SIGNUP);
            }
        });

        //로그인 된 상태의 drawer
        loggedDrawer.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "로그아웃 구현해야함~", Toast.LENGTH_SHORT).show();
                //로그아웃
            }
        });
        loggedDrawer.bookMarkCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
                //즐겨찾기 목록
                Toast.makeText(MainActivity.this, "즐겨찾기액티비티 구현 후 스택연결 해야함", Toast.LENGTH_SHORT).show();
            }
        });
        loggedDrawer.orderCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reservationIntent = new Intent(MainActivity.this, ReservationActivity.class);
                startActivity(reservationIntent);
            }
        });

        View.OnClickListener profileEditClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //프로필 수정
                Toast.makeText(MainActivity.this, "프로필 수정기능 구현해야함~", Toast.LENGTH_SHORT).show();
            }
        };
        loggedDrawer.profileEditImageView.setOnClickListener(profileEditClickListener);
        loggedDrawer.profileImageView.setOnClickListener(profileEditClickListener);
    }

    private void setRecycler() {
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

    private void getInitData() {
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

        if (id == R.id.menu_notify_true) {
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

    private View.OnClickListener mapClipClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int key = Integer.parseInt((String) view.getTag());

            if(lastClickedMapPosition==key)
                return;

            String[] locationInfo = MapClipDataHelper.getLocationTextInfo(key);
            String resultGuName;
            if (locationInfo[0].endsWith("구"))
                resultGuName = locationInfo[0];
            else
                resultGuName = locationInfo[0] + "구";

            selectedAddressName.setText("서울시 " + resultGuName);

            ImageView clickedAreaImageView = mapClipImageViews[key - 1];
            TextView clickedAreaTextView = mapClipTextViews[key - 1];

            clickedAreaTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            clickedAreaImageView.setImageResource(MapClipDataHelper.getMapImage(key, false));

            ImageView lastClickedImageView = mapClipImageViews[lastClickedMapPosition - 1];
            TextView lastClickedTextView = mapClipTextViews[lastClickedMapPosition - 1];
            lastClickedTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            lastClickedImageView.setImageResource(MapClipDataHelper.getMapImage(lastClickedMapPosition, true));

            //TODO: 해당 구의 트럭리스트 데이터 가져오기
            /*
            *
            *
            *
            * */

            lastClickedMapPosition = key;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_FOR_LOGIN:
                if(resultCode == RESULT_OK) {
                    //로그인 성공
                    drawerDefault.setVisibility(View.GONE);
                    drawerLogged.setVisibility(View.VISIBLE);
                    
                    int loginResult = data.getIntExtra(LoginActivity.LOGIN_RESULT, -1);

                    switch (loginResult) {
                        case LoginActivity.RESULT_GUEST:
                            loggedDrawer.orderNameTextView.setText("예약내역");
                            loggedDrawer.pushListTextView.setText("즐겨찾기 푸시알람");
                            break;

                        case LoginActivity.RESULT_OWNER:
                            loggedDrawer.orderNameTextView.setText("순번내역");
                            loggedDrawer.pushListTextView.setText("가게 푸시알람");
                            break;

                        default:

                    }

                }else {
                    //로그인 실패
                }
                break;

            case REQUEST_CODE_FOR_SIGNUP:
                if(resultCode == RESULT_OK) {
                    //회원가입 성공
                }else {
                    //회원가입 실패
                }
                break;
        }
    }

    static class DefaultDrawer {
        @BindView(R.id.login_container)
        View loginContainer;
        @BindView(R.id.signup_container)
        View signUpContainer;
        @BindView(R.id.plz_login_text)
        TextView pleaseLoginTextView;
    }

    static class LoggedDrawer {
        @BindView(R.id.profile_image)
        ImageView profileImageView;
        @BindView(R.id.order_count)
        TextView orderCountTextView;
        @BindView(R.id.order_name_text)
        TextView orderNameTextView;
        @BindView(R.id.bookmark_count)
        TextView bookMarkCountTextView;
        @BindView(R.id.logout_btn)
        Button logoutButton;
        @BindView(R.id.profile_edit_btn)
        ImageView profileEditImageView;
        @BindView(R.id.push_alarm_list_text)
        TextView pushListTextView;
    }

}
