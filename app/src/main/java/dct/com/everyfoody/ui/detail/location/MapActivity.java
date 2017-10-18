package dct.com.everyfoody.ui.detail.location;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.ResLocation;
import dct.com.everyfoody.request.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends WhiteThemeActivity implements OnMapReadyCallback {
    @BindView(R.id.map_toolbar)Toolbar mapToolbar;

    private NetworkService networkService;
    private double mLatitude, mLongitude;
    private int storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        SharedPreferencesService.getInstance().load(this);
        Intent getId = getIntent();
        storeId = getId.getExtras().getInt("storeId");
        setToolbar();
        getLocation();
    }

    private void getLocation(){
        Call<ResLocation> locationCall = networkService.getLocation(SharedPreferencesService.getInstance().getPrefStringData("auth_token"), storeId);

        locationCall.enqueue(new Callback<ResLocation>() {
            @Override
            public void onResponse(Call<ResLocation> call, Response<ResLocation> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        mLatitude = response.body().getLocation().getStoreLatitude();
                        mLongitude = response.body().getLocation().getStoreLongitude();

                        setMap();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResLocation> call, Throwable t) {

            }
        });
    }

    private void setToolbar(){
        mapToolbar.setTitle("");
        setSupportActionBar(mapToolbar);
        mapToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        mapToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setMap(){
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng position = new LatLng(mLatitude , mLongitude);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));

        MarkerOptions mymarker = new MarkerOptions()
                .position(position);
        mymarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2));

        CircleOptions circle1KM = new CircleOptions().center(position)
                .radius(1000)
                .strokeWidth(0f)
                .fillColor(Color.parseColor("#33ff6e00"));

        map.addMarker(mymarker);
        map.addCircle(circle1KM);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_search){
            replaceFragment(RecentSearchFragment.getInstance(),null);
        }

        return super.onOptionsItemSelected(item);
    }

    public void replaceFragment(Fragment fragment, @Nullable Bundle bundle){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(R.id.map, fragment);
        transaction.commit();
    }
}
