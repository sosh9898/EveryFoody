package dct.com.everyfoody.ui.home.user;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.base.util.ToastMaker;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.MainList;
import dct.com.everyfoody.model.SideMenu;
import dct.com.everyfoody.request.NetworkService;
import dct.com.everyfoody.ui.bookmark.BookmarkActivity;
import dct.com.everyfoody.ui.detail.DetailActivity;
import dct.com.everyfoody.ui.home.MapClipDataHelper;
import dct.com.everyfoody.ui.home.owner.OwnerHomeActivity;
import dct.com.everyfoody.ui.login.LoginActivity;
import dct.com.everyfoody.ui.reservation.ReservationActivity;
import dct.com.everyfoody.ui.signup.SignUpMainActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dct.com.everyfoody.ui.login.LoginActivity.EXPIRED_OWNER;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_GUEST;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_NON_AUTH_OWNER;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_OWNER;


public class MainActivity extends WhiteThemeActivity {

    private static final int MAP_CLIP_COUNT = 8;
    private static final int REQUEST_CODE_FOR_LOGIN = 201;
    private static final int REQUEST_CODE_FOR_SIGNUP = 202;
    public static final int TOGGLE_CHECKED = 501;
    public static final int TOGGLE_UNCHECKED = 502;

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
    @BindView(R.id.main_nested_scrollview)
    NestedScrollView nestedScrollView;
    @BindView(R.id.my_location)
    ImageView myLocImage;

    public static final String TAG = MainActivity.class.getSimpleName();

    private TruckRecyclerAdapter adapter;
    private SettingRecyclerAdapter settingAdapter;
    private NetworkService networkService;
    private ImageView[] mapClipImageViews;
    private TextView[] mapClipTextViews;
    private DefaultDrawer defaultDrawer;
    private LoggedDrawer loggedDrawer;
    private int lastClickedMapPosition = 3;
    private MainList mainList;
    private List<MainList.TruckList> truckLists;
    private List<SideMenu.BookMark> bookMarkList;
    private LocationManager locationManager;
    private double lat, lng;
    private String address;

    public static final int EXIST_NOTI = 802;
    public static final int NONEXIST_NOTI = 801;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setInitSetting();
        getMainData(3);
        setToolbar();
        bindDrawerEvent();
        checkPermission();
        outoLogin();
        setRecycler();
        MapClipDataHelper.initialize();
        setFab();

        Log.d("token", SharedPreferencesService.getInstance().getPrefStringData("auth_token"));
        Log.d("fcm", SharedPreferencesService.getInstance().getPrefStringData("fcm_token"));

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

    private void setFab() {
        fab.setImageResource(R.drawable.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nestedScrollView.scrollTo(0, 0);
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < 100)
                    fab.hide();
                else
                    fab.show();
            }
        });
    }

    private void setInitSetting() {
        SharedPreferencesService.getInstance().load(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        ViewCompat.setNestedScrollingEnabled(mainRecycler, false);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        truckLists = new ArrayList<>();
        bookMarkList = new ArrayList<>();
    }

    private void bindDrawerEvent() {
        defaultDrawer = new DefaultDrawer();
        loggedDrawer = new LoggedDrawer();

        ButterKnife.bind(defaultDrawer, drawerDefault);
        ButterKnife.bind(loggedDrawer, drawerLogged);

        loggedDrawer.userName.setText(SharedPreferencesService.getInstance().getPrefStringData("user_name"));

        View.OnClickListener loginClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(loginIntent, REQUEST_CODE_FOR_LOGIN);
                drawerLayout.closeDrawer(GravityCompat.START);
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
                SharedPreferencesService.getInstance().removeData("auth_token", "user_status");
                drawerDefault.setVisibility(View.VISIBLE);
                drawerLogged.setVisibility(View.GONE);
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
                Intent reservationIntent = new Intent(MainActivity.this, ReservationActivity.class);
                startActivity(reservationIntent);
            }
        });

        View.OnClickListener profileEditClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                getImage();

            }
        };
        loggedDrawer.profileEditImageView.setOnClickListener(profileEditClickListener);
        loggedDrawer.profileImageView.setOnClickListener(profileEditClickListener);
    }

    private void getImage(){
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(MainActivity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        modifyProfileImage(uri);
                    }
                })
                .create();

        tedBottomPicker.show(getSupportFragmentManager());
    }

    private void modifyProfileImage(final Uri uri) {
        MultipartBody.Part body;

        if (uri == null) {
            body = null;
        } else {

            BitmapFactory.Options options = new BitmapFactory.Options();

            InputStream in = null;
            try {
                in = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());

            File photo = new File(uri.toString());

            body = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);

        }

        Call<BaseModel> profileCall = networkService.modifyProfile(SharedPreferencesService.getInstance().getPrefStringData("auth_token"),body);

        profileCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")) {
                        ToastMaker.makeShortToast(getApplicationContext(), "성공");
                        Glide.with(getApplicationContext()).load(uri).into(loggedDrawer.profileImageView);

                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {

            }
        });
    }

    private void setRecycler() {
        mainRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new TruckRecyclerAdapter(truckLists, onClickListener);
        mainRecycler.setAdapter(adapter);

        loggedDrawer.settingRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        settingAdapter = new SettingRecyclerAdapter(bookMarkList, getApplicationContext());
        loggedDrawer.settingRecycler.setAdapter(settingAdapter);


    }

    private void setToolbar() {
        guestToolbar.setTitle("");
        setSupportActionBar(guestToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, guestToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
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

    private void outoLogin() {
        if (!SharedPreferencesService.getInstance().getPrefStringData("auth_token").equals("")) {
            drawerDefault.setVisibility(View.GONE);
            drawerLogged.setVisibility(View.VISIBLE);

            loggedDrawer.orderNameTextView.setText("예약내역");
            loggedDrawer.pushListTextView.setText("즐겨찾기 푸시알람");
        }
    }

    private void getMainData(int index) {
        if (String.valueOf(lat) == null) {
            lat = 37.55;
            lng = 126.98;
        }

        Call<MainList> getMainList = networkService.getMainLists("nonLoginUser", index, lat, lng);
        getMainList.enqueue(new Callback<MainList>() {
            @Override
            public void onResponse(Call<MainList> call, Response<MainList> response) {
                if (response.isSuccessful()) {
                    mainList = response.body();
                    if (mainList.getStatus().equals("success")) {
                        truckLists = mainList.getData().getTruckLists();
                        adapter.refreshAdapter(truckLists);
                        SharedPreferencesService.getInstance().setPrefData("user_token",response.body().getData().getUserToken());

                    }
                }
            }

            @Override
            public void onFailure(Call<MainList> call, Throwable t) {
                Log.e(TAG, "error : " + t.toString());
            }
        });

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

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int tempPosition = mainRecycler.getChildPosition(view);
            Intent detailIntent = new Intent(view.getContext(), DetailActivity.class);
            detailIntent.putExtra("storeId", truckLists.get(tempPosition).getStoreID());
            startActivity(detailIntent);
        }
    };

    private View.OnClickListener mapClipClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int key = Integer.parseInt((String) view.getTag());

            if (lastClickedMapPosition == key)
                return;

            String[] locationInfo = MapClipDataHelper.getLocationTextInfo(key);
            String resultGuName;
            if (locationInfo[0].endsWith("구"))
                resultGuName = locationInfo[0];
            else
                resultGuName = locationInfo[0] + "구";

            selectedAddressName.setText("서울시 " + resultGuName);

            checkMap(key);
        }
    };

    private void checkMap(int key) {
        ImageView clickedAreaImageView = mapClipImageViews[key - 1];
        TextView clickedAreaTextView = mapClipTextViews[key - 1];
        clickedAreaTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
        clickedAreaImageView.setImageResource(MapClipDataHelper.getMapImage(key, false));

        ImageView lastClickedImageView = mapClipImageViews[lastClickedMapPosition - 1];
        TextView lastClickedTextView = mapClipTextViews[lastClickedMapPosition - 1];
        lastClickedTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        lastClickedImageView.setImageResource(MapClipDataHelper.getMapImage(lastClickedMapPosition, true));

        getMainData(key);

        lastClickedMapPosition = key;
    }

    private void loginSuccess(Intent data) {
        drawerDefault.setVisibility(View.GONE);
        drawerLogged.setVisibility(View.VISIBLE);

        int loginResult = data.getIntExtra(LoginActivity.LOGIN_RESULT, -1);

        switch (loginResult) {
            case RESULT_GUEST:
                loggedDrawer.orderNameTextView.setText("예약내역");
                loggedDrawer.pushListTextView.setText("즐겨찾기 푸시알람");
                loggedDrawer.userName.setText(SharedPreferencesService.getInstance().getPrefStringData("user_name"));
                break;
            case RESULT_OWNER:
            case RESULT_NON_AUTH_OWNER:
            case EXPIRED_OWNER:
                Intent ownerIntent = new Intent(this, OwnerHomeActivity.class);
                startActivity(ownerIntent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_FOR_LOGIN:
                if (resultCode == RESULT_OK) {
                    //로그인 성공
                    loginSuccess(data);
                } else {
                    //로그인 실패
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

    public static class LoggedDrawer {
        @BindView(R.id.profile_image)
        public CircleImageView profileImageView;
        @BindView(R.id.order_count)
        public TextView orderCountTextView;
        @BindView(R.id.order_name_text)
        public TextView orderNameTextView;
        @BindView(R.id.bookmark_count)
        public TextView bookMarkCountTextView;
        @BindView(R.id.logout_btn)
        public Button logoutButton;
        @BindView(R.id.profile_edit_btn)
        public ImageView profileEditImageView;
        @BindView(R.id.push_alarm_list_text)
        public TextView pushListTextView;
        @BindView(R.id.user_setting_rcv)
        public RecyclerView settingRecycler;
        @BindView(R.id.user_name)
        public TextView userName;
    }

    private void checkPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
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
                        if(response.body().getSideInfo().getImageUrl() != null)
                            Glide.with(getApplicationContext()).load(response.body().getSideInfo().getImageUrl()).into(loggedDrawer.profileImageView);

                        settingAdapter.refreshAdapter(response.body().getSideInfo().getBookMarkList());
                    }
                }
            }

            @Override
            public void onFailure(Call<SideMenu> call, Throwable t) {
                Log.d("????", t.toString());
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
        Glide.with(getApplicationContext()).load(R.drawable.my_location2).into(myLocImage);

    }

    @OnClick(R.id.my_location)
    public void onClickMyloc(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(locationManager.getBestProvider(new Criteria(), true), 3000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        ToastMaker.makeShortToast(getApplicationContext(), "click");
    }

    private void getMapKey(String address) {
        String str1 = address;
        String str2[] = str1.split(" ");
        ToastMaker.makeShortToast(getApplicationContext(), "address : " + address);


        switch (str2[2].substring(0, 2)) {
            case "강서":
                checkMap(1);
                break;
            case "은평":
                checkMap(2);
                break;
            case "종로":
                checkMap(3);
                break;
            case "도봉":
                checkMap(4);
                break;
            case "동대":
                checkMap(5);
                break;
            case "동작":
                checkMap(6);
                break;
            case "서초":
                checkMap(7);
                break;
            case "강동":
                checkMap(8);
                break;
            default:
                if (lastClickedMapPosition != 3)
                    checkMap(3);
                break;
        }

    }


    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            address = getAddress(getApplicationContext(), lat, lng);

            Log.d("loc", lat + "   " + lng);

            getMapKey(address);

            ToastMaker.makeShortToast(getApplicationContext(), "loc : " + location.toString());
            Glide.with(getApplicationContext()).load(R.drawable.my_location).into(myLocImage);

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    public static String getAddress(Context mContext, double lat, double lng) {
        String nowAddress = "현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress = currentLocationAddress;

                }
            }

        } catch (IOException e) {
            ToastMaker.makeShortToast(mContext, "실패");

            e.printStackTrace();
        }
        return nowAddress;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMainData(lastClickedMapPosition);
    }
}
