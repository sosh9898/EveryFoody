package dct.com.everyfoody.ui.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.truck_image)ImageView truckImage;
    @BindView(R.id.food_icon)ImageView foodIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        setAnimation();
    }

    private void setAnimation(){
        Animation truckAnim = AnimationUtils.loadAnimation
                (getApplicationContext(),
                        R.anim.truck_move);
        Animation iconAnim = AnimationUtils.loadAnimation
                (getApplicationContext(),
                        R.anim.fade_in);

        truckImage.startAnimation(truckAnim);
        foodIcon.startAnimation(iconAnim);
        iconAnim.setStartOffset(1000);
    }
}
