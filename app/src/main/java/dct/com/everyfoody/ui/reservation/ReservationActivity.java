package dct.com.everyfoody.ui.reservation;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;

public class ReservationActivity extends WhiteThemeActivity {
    @BindView(R.id.reservation_toolbar)Toolbar reservationToolbar;
    @BindView(R.id.reservation_rcv)RecyclerView reserveRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        ButterKnife.bind(this);
        setToolbar();


    }

    private void setToolbar(){
        setSupportActionBar(reservationToolbar);
        reservationToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_white));
        reservationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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


}
