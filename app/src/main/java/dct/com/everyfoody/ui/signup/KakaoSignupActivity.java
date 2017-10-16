package dct.com.everyfoody.ui.signup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.request.NetworkService;
import dct.com.everyfoody.ui.login.LoginActivity;
import dct.com.everyfoody.ui.signup.user.SignUpActivity;

/**
 * Created by jyoung on 2017. 8. 16..
 */

public class KakaoSignupActivity extends Activity {
    SharedPreferencesService preferencesService;
    NetworkService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesService.load(getApplicationContext());
        service = ApplicationController.getInstance().getNetworkService();

        requestMe();
    }

    protected void requestMe(){
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user inof. msg =" + errorResult;
                Log.v("fail", message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorMessage());
                if(result == ErrorCode.CLIENT_ERROR_CODE){
                    finish();
                }else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {

                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {

                showSignup();
            }

            @Override
            public void onSuccess(final UserProfile result) {


            }
        });
    }

    protected  void showSignup(){
        redirectLoginActivity();
    }

    public void redirectMainActivity(String profileImage, String kakaoId){
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("profileImage", profileImage);
        intent.putExtra("id", kakaoId);
        startActivity(intent);
        finish();
    }

    protected  void redirectLoginActivity(){
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
