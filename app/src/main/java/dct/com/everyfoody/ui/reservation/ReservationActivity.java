package dct.com.everyfoody.ui.reservation;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.Reservation;
import dct.com.everyfoody.request.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationActivity extends WhiteThemeActivity {
    @BindView(R.id.reservation_toolbar)Toolbar reservationToolbar;
    @BindView(R.id.reservation_rcv)RecyclerView reserveRecycler;

    private ReserveRecyclerAdapter reserveRecyclerAdapter;
    private List<Reservation.Store> reservationList;
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        ButterKnife.bind(this);
        reservationList = new ArrayList<>();
        networkService = ApplicationController.getInstance().getNetworkService();
        SharedPreferencesService.getInstance().load(this);
        setToolbar();
        setRecycler();
        getReservationList();

    }

    private void setRecycler(){
        reserveRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reserveRecyclerAdapter = new ReserveRecyclerAdapter(reservationList, onClickListener);
        reserveRecycler.setAdapter(reserveRecyclerAdapter);
    }

    private void setToolbar(){
        reservationToolbar.setTitle("");
        setSupportActionBar(reservationToolbar);
        reservationToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_white));
        reservationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getReservationList(){
        Call<Reservation> reservationCall = networkService.getReservationList(SharedPreferencesService.getInstance().getPrefStringData("auth_token"));

        reservationCall.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        reservationList = response.body().getStore();
                        reserveRecyclerAdapter.refreshAdapter(reservationList);
                    }
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                Log.d("d????", t.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complete_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem deleteIc = menu.findItem(R.id.menu_delete);
        MenuItem completeIc = menu.findItem(R.id.menu_complete);


        return super.onPrepareOptionsMenu(menu);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

}
