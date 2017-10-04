package dct.com.everyfoody.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.ui.home.user.MainActivity;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.truck_image)ImageView truckImage;
    @BindView(R.id.food_icon)ImageView foodIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        setAnimation();
        appStart();
    }

    private void appStart(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }, 1900);
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
