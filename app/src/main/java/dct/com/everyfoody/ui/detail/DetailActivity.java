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

import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_GUEST;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_OWNER;

public class DetailActivity extends OrangeThemeActivity {
    @BindView(R.id.detail_toolbar)Toolbar detailToolbar;
    @BindView(R.id.detail_main_image)ImageView mainImage;

    public static final String TAG = DetailActivity.class.getSimpleName();

    private int storeID;
    private NetworkService networkService;
    private StoreInfo storeInfo;
    private int bookmarkFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        SharedPreferencesService.getInstance().load(getApplicationContext());
        getInitData();
        getStoreInfo();
    }

    private void getInitData(){
        Intent getData = getIntent();
        storeID = getData.getExtras().getInt("storeID");
        networkService = ApplicationController.getInstance().getNetworkService();
    }

    private void getStoreInfo(){
        Call<StoreInfo> getStoreInfo = networkService.getStoreInfo(SharedPreferencesService.getInstance().getPrefStringData("auth_token"), storeID);

        getStoreInfo.enqueue(new Callback<StoreInfo>() {
            @Override
            public void onResponse(Call<StoreInfo> call, Response<StoreInfo> response) {
                if(response.isSuccessful()){

                    if(response.body().getStatus().equals("success")){
                        storeInfo = response.body();
                        setToolbar();
                        Gson gson = new Gson();
                        String info = gson.toJson(storeInfo);
                        bookmarkFlag = storeInfo.getDetailInfo().getBasicInfo().getReservationCheck();
                        Log.d("fff", bookmarkFlag+"");
                        addFragment(NormalFragment.getInstance(), BundleBuilder.create().with("storeInfo",info).build());
                    }

                }
            }

            @Override
            public void onFailure(Call<StoreInfo> call, Throwable t) {
                Log.d(TAG, "error : "+t.toString());
            }
        });

    }

    private void setToolbar(){
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


    public void addFragment(Fragment fragment, Bundle bundle){
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


        if(id == R.id.menu_detail_map){
            Intent mapIntent = new Intent(this, MapActivity.class);
            startActivity(mapIntent);
        }
        else if(id == R.id.menu_detail_bookmark_on || id == R.id.menu_detail_bookmark_off){
            if(SharedPreferencesService.getInstance().getPrefIntegerData("user_status") != RESULT_GUEST){
                Intent needLogin = new Intent(this, LoginActivity.class);
                startActivity(needLogin);
            }
            else {
                bookmark();
            }
        }
        else if(id == R.id.menu_detail_edit){
            Intent editIntent = new Intent(this, EditActivity.class);
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

        Log.d("fd", SharedPreferencesService.getInstance().getPrefIntegerData("user_status")+"");
        if(SharedPreferencesService.getInstance().getPrefIntegerData("user_status") ==RESULT_GUEST){
            Log.d("bookmark", "1");

            edit.setVisible(false);
            if(bookmarkFlag == 0){
                bookmarkOn.setVisible(false);
                bookmarkOff.setVisible(true);
            }
            else if(bookmarkFlag == 1){
                bookmarkOn.setVisible(true);
                bookmarkOff.setVisible(false);
            }
        }
        else if(SharedPreferencesService.getInstance().getPrefIntegerData("userStatus") == RESULT_OWNER){
            Log.d("bookmark", "2");

            map.setVisible(false);
            bookmarkOff.setVisible(false);
            bookmarkOn.setVisible(false);
        }
        else{
            Log.d("bookmark", "3");

            bookmarkOn.setVisible(false);
            edit.setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @OnClick(R.id.review_btn)
    public void reviewClick(View view){
        Intent reviewIntent = new Intent(this, ReviewActivity.class);
        startActivity(reviewIntent);
    }
    @OnClick(R.id.booking)
    public void bookingClick(View view){
        reserve();
    }

    private void reserve(){
        Call<BaseModel> reserveCall = networkService.userReseve(SharedPreferencesService.getInstance().getPrefStringData("auth_token"), storeID);

        reserveCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success"))
                        ToastMaker.makeShortToast(getApplicationContext(), "성공");
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {

            }
        });
    }

    private void bookmark(){

        Call<BaseModel> bookmarkCall = networkService.userBookmark(SharedPreferencesService.getInstance().getPrefStringData("auth_token"), storeID);

        bookmarkCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")) {
                        ToastMaker.makeShortToast(getApplicationContext(), "성공");

                        if(bookmarkFlag ==1)
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
}
