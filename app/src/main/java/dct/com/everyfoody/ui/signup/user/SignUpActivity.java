package dct.com.everyfoody.ui.signup.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;
import com.kakao.util.exception.KakaoException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.ui.signup.KakaoSignupActivity;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.kakao_signup)LoginButton kakaobtn;

    public SessionCallback callback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.kakao_signup_btn)
    public void onClickKakaoSignUp(View view){

        if (callback == null) {
            callback = new SessionCallback();
            Session.getCurrentSession().addCallback(callback);
        }
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this);
        kakaobtn.performClick();
    }

    public class SessionCallback implements ISessionCallback {


        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
            Log.d("kakaokkkk", "?");

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.d("kakaokkkk", "??");

            if (exception != null) {
                Log.d("kakao_session_error", exception.toString());
            }
//            FacebookSdk.sdkInitialize(SignUpActivity.this);
//            callbackManager = CallbackManager.Factory.create();
            setContentView(R.layout.activity_login);
        }
    }

    protected void redirectSignupActivity() {

        final Intent intent = new Intent(this, KakaoSignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
//            callbackManager.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }
}
