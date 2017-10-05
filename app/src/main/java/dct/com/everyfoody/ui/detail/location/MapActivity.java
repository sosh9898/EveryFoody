package dct.com.everyfoody.ui.detail.location;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import dct.com.everyfoody.R;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    @BindView(R.id.map_toolbar)Toolbar mapToolbar;

    final static double mLatitude = 37.5197889;   //위도
    final static double mLongitude = 126.9403083;  //경도

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setMap();
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
}
