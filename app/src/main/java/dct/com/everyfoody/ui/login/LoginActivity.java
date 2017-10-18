package dct.com.everyfoody.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.Login;
import dct.com.everyfoody.model.UserInfo;
import dct.com.everyfoody.request.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends WhiteThemeActivity {

    public static final String LOGIN_RESULT = "login-result-key";     //로그인 결과를 Intent로 보낼 때, IntExtra의 key값
    public static final int RESULT_GUEST = 401;     //value값1
    public static final int RESULT_OWNER = 402;     //value값2
    public static final int RESULT_NON_AUTH_OWNER = 403;  // 미인증 사업자
    public static final int EXPIRED_OWNER = 404;          // 만료된 사업

    public static final int LOGIN_KAKAO = 101;
    public static final int LOGIN_FACEBOOK = 102;

    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SharedPreferencesService.getInstance().load(this);
        networkService = ApplicationController.getInstance().getNetworkService();


    }

    @OnClick(R.id.facebook_login_btn)
    public void facebookLoginClick(View view){
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("owner5");
        userInfo.setUid("111");
        userInfo.setCategory(LOGIN_KAKAO);

        Call<Login> loginCall2 = networkService.userLogin(userInfo);

        loginCall2.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if(response.isSuccessful()){
                    SharedPreferencesService.getInstance().setPrefData("auth_token", response.body().getResultData().getToken());
                    SharedPreferencesService.getInstance().setPrefData("user_name", response.body().getResultData().getName());
                    int userStatus = response.body().getResultData().getCategory();

                    Intent loginResult = new Intent();
                    Log.d("?????", userStatus+"");
                    if(userStatus == RESULT_GUEST) {
                        SharedPreferencesService.getInstance().setPrefData("user_status", userStatus);
                        loginResult.putExtra(LOGIN_RESULT, userStatus);
                        setResult(RESULT_OK, loginResult);
                        finish();
                    }
                    else{
                        SharedPreferencesService.getInstance().setPrefData("user_status", userStatus);
                        loginResult.putExtra(LOGIN_RESULT, userStatus);
                        setResult(RESULT_OK, loginResult);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.d("sdfsdf", t.toString());
            }
        });

    }

    @OnClick(R.id.kakao_login_btn)
    public void kakaoLoginClick(View view){
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("test1");
        userInfo.setUid("111");
        userInfo.setCategory(LOGIN_KAKAO);

        Call<Login> loginCall = networkService.userLogin(userInfo);

        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if(response.isSuccessful()){
                    SharedPreferencesService.getInstance().setPrefData("auth_token", response.body().getResultData().getToken());
                    SharedPreferencesService.getInstance().setPrefData("user_name", response.body().getResultData().getName());
                    int userStatus = response.body().getResultData().getCategory();

                    Intent loginResult = new Intent();
                    if(userStatus == RESULT_GUEST) {
                        SharedPreferencesService.getInstance().setPrefData("user_status", userStatus);
                        loginResult.putExtra(LOGIN_RESULT, userStatus);
                        setResult(RESULT_OK, loginResult);
                        finish();
                    }
                    else{
                        SharedPreferencesService.getInstance().setPrefData("user_status", userStatus);
                        loginResult.putExtra(LOGIN_RESULT, userStatus);
                        setResult(RESULT_OK, loginResult);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.d("sdfsdf", t.toString());
            }
        });


    }
}
