package dct.com.everyfoody.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.ui.signup.user.SignUpActivity;

public class SignUpMainActivity extends WhiteThemeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.user_signup)
    public void onClickUserSignup(View view){
        Intent userIntent = new Intent(this, SignUpActivity.class);
        startActivity(userIntent);
    }

}
