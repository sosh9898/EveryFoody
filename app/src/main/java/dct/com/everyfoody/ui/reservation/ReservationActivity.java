package dct.com.everyfoody.ui.reservation;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;

public class ReservationActivity extends WhiteThemeActivity {
    @BindView(R.id.reservation_toolbar)Toolbar reservationToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        ButterKnife.bind(this);

    }
}
