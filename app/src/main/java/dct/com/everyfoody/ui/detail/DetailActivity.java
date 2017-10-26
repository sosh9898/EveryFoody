package dct.com.everyfoody.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.base.OrangeThemeActivity;
import dct.com.everyfoody.base.util.BundleBuilder;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.base.util.ToastMaker;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.StoreInfo;
import dct.com.everyfoody.request.NetworkService;
import dct.com.everyfoody.ui.detail.edit.EditActivity;
import dct.com.everyfoody.ui.detail.location.MapActivity;
import dct.com.everyfoody.ui.detail.normal.NormalFragment;
import dct.com.everyfoody.ui.detail.review.ReviewActivity;
import dct.com.everyfoody.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dct.com.everyfoody.ui.login.LoginActivity.EXPIRED_OWNER;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_GUEST;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_NON_AUTH_OWNER;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_OWNER;

public class DetailActivity extends OrangeThemeActivity {
    @BindView(R.id.detail_toolbar)
    Toolbar detailToolbar;
    @BindView(R.id.detail_main_image)
    ImageView mainImage;
    @BindView(R.id.booking_count)
    TextView bookingCount;
    @BindView(R.id.booking)
    TextView booking;
    @BindView(R.id.review_btn)
    LinearLayout reviewBtn;

    public static final String TAG = DetailActivity.class.getSimpleName();

    private int storeId;
    private NetworkService networkService;
    private StoreInfo storeInfo;
    private int bookmarkFlag;
    private int userStatus;
    private ReserveDialog reserveDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        SharedPreferencesService.getInstance().load(this);
        networkService = ApplicationController.getInstance().getNetworkService();

        userStatus = SharedPreferencesService.getInstance().getPrefIntegerData("user_status");
        switch (userStatus) {
            case RESULT_GUEST:
                getInitData();
                getStoreInfo();
                break;
            case RESULT_OWNER:
            case RESULT_NON_AUTH_OWNER:
            case EXPIRED_OWNER:
                getMyStoreInfo();
                break;
            default:
                getInitData();
                getStoreInfo();
                break;
        }

    }

    private void getMyStoreInfo() {
        final Call<StoreInfo> getMyStoreInfo = networkService.getMyStoreInfo(SharedPreferencesService.getInstance().getPrefStringData("auth_token"));

        getMyStoreInfo.enqueue(new Callback<StoreInfo>() {
            @Override
            public void onResponse(Call<StoreInfo> call, Response<StoreInfo> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        storeInfo = response.body();
                        networkAfter();
                        reviewBtn.setVisibility(View.GONE);
                        booking.setText("후기");
                    }
                }
            }

            @Override
            public void onFailure(Call<StoreInfo> call, Throwable t) {
                Log.d("??", t.toString());
            }
        });

    }

    private void getInitData() {
        Intent getData = getIntent();
        storeId = getData.getExtras().getInt("storeId");
        networkService = ApplicationController.getInstance().getNetworkService();
    }

    private void getStoreInfo() {
        String token = SharedPreferencesService.getInstance().getPrefStringData("auth_token");
        if(token.equals(""))
            token = "nonLoginUser";

        Call<StoreInfo> getStoreInfo = networkService.getStoreInfo(token, storeId);

        getStoreInfo.enqueue(new Callback<StoreInfo>() {
            @Override
            public void onResponse(Call<StoreInfo> call, Response<StoreInfo> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        storeInfo = response.body();
                        networkAfter();
                        bookmarkFlag = storeInfo.getDetailInfo().getBasicInfo().getBookmarkCheck();
                        bookingCount.setText("대기인원 "+storeInfo.getDetailInfo().getBasicInfo().getReservationCount()+"명");
                        if(storeInfo.getDetailInfo().getBasicInfo().getReservationCheck()==1){
                            booking.setText("순번 대기중");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<StoreInfo> call, Throwable t) {
                Log.d(TAG, "error : " + t.toString());
            }
        });

    }

    private void networkAfter() {
        setToolbar();
        Gson gson = new Gson();
        String info = gson.toJson(storeInfo);
        addFragment(NormalFragment.getInstance(), BundleBuilder.create().with("storeInfo", info).build());
    }

    private void setToolbar() {
        Glide.with(this).load(storeInfo.getDetailInfo().getBasicInfo().getStoreImage()).into(mainImage);
        detailToolbar.setTitle(storeInfo.getDetailInfo().getBasicInfo().getStoreName());
        detailToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(detailToolbar);
        detailToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_white));
        detailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void addFragment(Fragment fragment, Bundle bundle) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fragment.setArguments(bundle);
        transaction.add(R.id.detail_content, fragment);
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.menu_detail_map) {
            Intent mapIntent = new Intent(this, MapActivity.class);
            mapIntent.putExtra("storeId", storeId);
            startActivity(mapIntent);
        } else if (id == R.id.menu_detail_bookmark_on || id == R.id.menu_detail_bookmark_off) {
            if (SharedPreferencesService.getInstance().getPrefIntegerData("user_status") != RESULT_GUEST) {
                Intent needLogin = new Intent(this, LoginActivity.class);
                startActivity(needLogin);
            } else {
                bookmark();
            }
        } else if (id == R.id.menu_detail_edit) {
            Intent editIntent = new Intent(this, EditActivity.class);
            Gson gson = new Gson();
            String info = gson.toJson(storeInfo);
            editIntent.putExtra("info", info);
            startActivity(editIntent);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem map = menu.findItem(R.id.menu_detail_map);
        MenuItem bookmarkOn = menu.findItem(R.id.menu_detail_bookmark_on);
        MenuItem bookmarkOff = menu.findItem(R.id.menu_detail_bookmark_off);
        MenuItem edit = menu.findItem(R.id.menu_detail_edit);

        if (SharedPreferencesService.getInstance().getPrefIntegerData("user_status") == RESULT_GUEST) {
            edit.setVisible(false);
            if (bookmarkFlag == 0) {
                bookmarkOn.setVisible(false);
                bookmarkOff.setVisible(true);
            } else if (bookmarkFlag == 1) {
                bookmarkOn.setVisible(true);
                bookmarkOff.setVisible(false);
            }
        } else if (SharedPreferencesService.getInstance().getPrefIntegerData("user_status") == RESULT_OWNER) {
            map.setVisible(false);
            bookmarkOff.setVisible(false);
            bookmarkOn.setVisible(false);
        } else {

            bookmarkOn.setVisible(false);
            edit.setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @OnClick(R.id.review_btn)
    public void reviewClick(View view) {
        Intent reviewIntent = new Intent(this, ReviewActivity.class);
        reviewIntent.putExtra("storeId", storeId);
        startActivity(reviewIntent);
    }

    @OnClick(R.id.booking)
    public void bookingClick(View view) {
        if (SharedPreferencesService.getInstance().getPrefIntegerData("user_status") != RESULT_GUEST) {
            Intent needLogin = new Intent(this, LoginActivity.class);
            startActivity(needLogin);
        }else
        reserve();
    }

    private void reserve() {
        reserveDialog = new ReserveDialog(this, reserveYes);
        reserveDialog.show();
    }

    public View.OnClickListener reserveYes = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Call<BaseModel> reserveCall = networkService.userReseve(SharedPreferencesService.getInstance().getPrefStringData("auth_token"), storeId);

            reserveCall.enqueue(new Callback<BaseModel>() {
                @Override
                public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("success")) {
                            ToastMaker.makeShortToast(getApplicationContext(), "성공");
                            booking.setText("순번 대기중");
                            reserveDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseModel> call, Throwable t) {

                }
            });
        }
    };

    private void bookmark() {

        Call<BaseModel> bookmarkCall = networkService.userBookmark(SharedPreferencesService.getInstance().getPrefStringData("auth_token"), storeId);

        bookmarkCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        ToastMaker.makeShortToast(getApplicationContext(), "성공");

                        if (bookmarkFlag == 1)
                            bookmarkFlag = 0;
                        else
                            bookmarkFlag = 1;

                        supportInvalidateOptionsMenu();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {

            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        getStoreInfo();
//    }
}
